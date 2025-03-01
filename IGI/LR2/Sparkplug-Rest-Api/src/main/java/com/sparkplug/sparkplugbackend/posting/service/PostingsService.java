package com.sparkplug.sparkplugbackend.posting.service;

import com.sparkplug.sparkplugbackend.exception.ResourceNotFoundException;
import com.sparkplug.sparkplugbackend.minio.service.MinioFileService;
import com.sparkplug.sparkplugbackend.posting.model.ImageInfo;
import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.posting.query.PostingSpecificationsBuilder;
import com.sparkplug.sparkplugbackend.posting.repository.CarsRepository;
import com.sparkplug.sparkplugbackend.posting.repository.PostingsRepository;
import com.sparkplug.sparkplugbackend.user.repository.SparkplugUsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class PostingsService {

    private final PostingsRepository postingsRepository;
    private final CarsRepository carsRepository;
    private final SparkplugUsersRepository usersRepository;
    private final MinioFileService fileService;

    private final String POSTING_IMAGES_DIRECTORY = "posting-images";

    public PostingsService(
            PostingsRepository postingsRepository,
            CarsRepository carsRepository,
            SparkplugUsersRepository usersRepository,
            MinioFileService fileService
    ) {
        this.postingsRepository = postingsRepository;
        this.carsRepository = carsRepository;
        this.usersRepository = usersRepository;
        this.fileService = fileService;
    }

    @Transactional
    public UUID save(UUID creatorId, UUID carId, String description, BigDecimal price, List<MultipartFile> images) {
        List<String> imageUrls = fileService.uploadFiles(images, POSTING_IMAGES_DIRECTORY);

        var posting = new Posting();
        var imageInfo = new ImageInfo();
        imageInfo.setUrls(imageUrls);

        posting.setDescription(description);
        posting.setPrice(price);
        posting.setCreationDate(LocalDateTime.now());
        posting.setImages(imageInfo);
        //throwing RuntimeException because method assumes that the car and the owner are already in the database
        posting.setCar(carsRepository.findById(carId)
                .orElseThrow(() -> new RuntimeException("Could not find car with id '" + carId + "'.")));
        posting.setCreator(usersRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Could not find user with id '" + creatorId + "'.")));

        postingsRepository.save(posting);
        return posting.getId();
    }

    public List<Posting> getAll() {
        return postingsRepository.findAll();
    }

    public Posting getById(UUID id) {
        return postingsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Posting with id '" + id + "' not found."));
    }

    public UUID getCarIdByPostingId(UUID postingId) {
        return postingsRepository.findCarIdByPostingId(postingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Posting with id '" + postingId + "' not found."));
    }

    public List<Posting> getByCreatorId(UUID id) {
        return postingsRepository.findByCreatorId(id);
    }

    public List<Posting> getByPredicate(String search) {

        var builder = new PostingSpecificationsBuilder();

        if (search != null && !search.isEmpty()) {
            var pattern = Pattern.compile("([\\w.]+?)([:<>])([\\w-]+),?");
            var matcher = pattern.matcher(search);

            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }

            var spec = builder.build();
            return postingsRepository.findAll(spec);
        }

        return postingsRepository.findAll();
    }

    @Transactional
    public void deleteById(UUID postingId) {
        var posting = postingsRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Posting with id '" + postingId + "' not found."));

        var images = posting.getImages().getUrls();
        if(images != null && !images.isEmpty()) {
            fileService.deleteFiles(images);
        }

        for (var user : posting.getWishlisters()) { //may be a better way to do this
            user.getWishlistedPostings().remove(posting);
            usersRepository.save(user);
        }

        postingsRepository.delete(posting);
    }

    public void updatePriceAndDescription(UUID postingId, BigDecimal price, String description) {
        var posting = postingsRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Posting with id '" + postingId + "' not found."));

        posting.setPrice(price);
        posting.setDescription(description);

        postingsRepository.save(posting);
    }

    @Transactional
    public void updatePhotosById(UUID postingId, List<MultipartFile> images) {
        Posting posting = getById(postingId);

        List<String> oldImageUrls = posting.getImages().getUrls();
        List<String> newImageUrls = fileService.uploadFiles(images, POSTING_IMAGES_DIRECTORY);

        posting.getImages().setUrls(newImageUrls);

        if (oldImageUrls != null && !oldImageUrls.isEmpty()) {
            fileService.deleteFiles(oldImageUrls);
        }

        postingsRepository.save(posting);
    }

    public boolean isOwner(UUID postingId, UUID userId) {
        UUID ownerId = postingsRepository.findOwnerIdByPostingId(postingId)
                .orElseThrow(() -> new ResourceNotFoundException("Posting with id '" + postingId + "' not found."));
        return ownerId.equals(userId);
    }

}
