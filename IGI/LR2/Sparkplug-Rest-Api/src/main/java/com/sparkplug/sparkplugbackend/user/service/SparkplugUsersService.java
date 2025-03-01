package com.sparkplug.sparkplugbackend.user.service;

import com.sparkplug.sparkplugbackend.exception.ResourceNotFoundException;
import com.sparkplug.sparkplugbackend.minio.service.MinioFileService;
import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import com.sparkplug.sparkplugbackend.posting.repository.PostingsRepository;
import com.sparkplug.sparkplugbackend.user.repository.SparkplugUsersRepository;
import com.sparkplug.sparkplugbackend.exception.BadRequestException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class SparkplugUsersService {

    private final SparkplugUsersRepository usersRepository;
    private final PostingsRepository postingsRepository;
    private final MinioFileService fileService;

    public SparkplugUsersService(
            SparkplugUsersRepository usersRepository,
            PostingsRepository postingsRepository,
            MinioFileService fileService
    ) {
        this.usersRepository = usersRepository;
        this.postingsRepository = postingsRepository;
        this.fileService = fileService;
    }

    public SparkplugUser getByUsername(String username) {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with username '" + username + "' not found."));
    }

    public SparkplugUser getById(UUID id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id '" + id + "' not found."));
    }

    public List<Posting> getPostingsById(UUID id) {
        SparkplugUser user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id '" + id + "' not found."));

        return user.getPostings();
    }

    public List<Posting> getPostingsByUsername(String username) {
        SparkplugUser user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with username '" + username + "' not found."));

        return user.getPostings();
    }

    public UUID addPostingToWishlist(UUID userId, UUID postingId) {
        SparkplugUser user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id '" + userId + "' not found."));

        Posting posting = postingsRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Posting with id '" + postingId + "' not found."));

        if (user.getWishlistedPostings().contains(posting))
            throw new BadRequestException(
                    "Posting with id '" + postingId + "' already has already been added to wishlist.");

        if (user.getPostings().contains(posting))
            throw new BadRequestException(
                    "Cannot add posting with id '" + postingId + "' to wishlist as it belongs to this user.");

        user.getWishlistedPostings().add(posting);
        posting.getWishlisters().add(user);
        usersRepository.save(user);
        return posting.getId();
    }

    public List<Posting> getWishlist(UUID userId) {
        SparkplugUser user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id '" + userId + "' not found."));
        return user.getWishlistedPostings();
    }

    public UUID removePostingFromWishlist(UUID userId, UUID postingId) {
        SparkplugUser user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id '" + userId + "' not found."));

        Posting posting = postingsRepository.findById(postingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Posting with id '" + postingId + "' not found."));

        if (!user.getWishlistedPostings().contains(posting)) {
            throw new BadRequestException("Posting with id '" + postingId + "' does not belong to this user's wishlist.");
        }

        user.getWishlistedPostings().remove(posting);
        posting.getWishlisters().remove(user);
        usersRepository.save(user);
        return posting.getId();
    }

    @Transactional
    public void updateProfilePicture(UUID userId, MultipartFile picture) {
        picture.getContentType();

        SparkplugUser user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with id '" + userId + "' not found."));

        String oldPictureUrl = user.getProfilePictureUrl();
        String newPictureUrl = fileService.uploadFile(picture, "profile-pictures");

        user.setProfilePictureUrl(newPictureUrl);

        if (oldPictureUrl != null) {
            fileService.deleteFiles(List.of(oldPictureUrl));
        }

        usersRepository.save(user);
    }
}
