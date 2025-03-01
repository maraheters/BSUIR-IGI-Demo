package com.sparkplug.sparkplugbackend.posting.controller;

import com.sparkplug.sparkplugbackend.posting.dto.request.PostingRequestDto;
import com.sparkplug.sparkplugbackend.posting.mapper.CarMapper;
import com.sparkplug.sparkplugbackend.posting.mapper.PostingMapper;
import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.posting.dto.response.PostingResponseDto;
import com.sparkplug.sparkplugbackend.security.SparkplugUserDetails;
import com.sparkplug.sparkplugbackend.posting.service.CarsService;
import com.sparkplug.sparkplugbackend.posting.service.PostingsService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/postings")
public class PostingController {

    private final PostingsService postingsService;
    private final CarsService carsService;
    private final CarMapper carMapper;
    private final PostingMapper postingMapper;

    public PostingController(
            PostingsService postingsService,
            CarsService carsService,
            CarMapper carMapper,
            PostingMapper postingMapper) {
        this.postingsService = postingsService;
        this.carsService = carsService;
        this.carMapper = carMapper;
        this.postingMapper = postingMapper;
    }

    @GetMapping
    public ResponseEntity<List<PostingResponseDto>> getAllPostingsWithQuery(
            @RequestParam(value = "q", required = false) String search) {

        List<Posting> postings = postingsService.getByPredicate(search);

        return ResponseEntity.ok(postings.stream().map(postingMapper::entityToResponseDto).toList());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<List<PostingResponseDto>> getPostingsByUserId(@PathVariable("id") UUID id) {
        List<Posting> postings = postingsService.getByCreatorId(id);
        return ResponseEntity.ok(postings.stream().map(postingMapper::entityToResponseDto).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostingResponseDto> getById(@PathVariable UUID id) {
        Posting posting = postingsService.getById(id);
        return ResponseEntity.ok(postingMapper.entityToResponseDto(posting));
    }

    @GetMapping("/{id}/preview-url")
    public ResponseEntity<String> getPreviewUrlById(@PathVariable UUID id) {
        Posting posting = postingsService.getById(id);
        return ResponseEntity.ok(posting.getImages().getUrls().getFirst());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<UUID> createPosting(
            @RequestPart("car") PostingRequestDto postingRequestDto,
            @RequestPart("images") List<MultipartFile> images,
            @AuthenticationPrincipal SparkplugUserDetails userDetails) {

        UUID carId = carsService.save(carMapper.requestDtoToEntity(postingRequestDto.getCarRequest()));

        String description = postingRequestDto.getDescription();
        BigDecimal price = postingRequestDto.getPrice();

        UUID postingId = postingsService.save(
                userDetails.getUser().getId(), carId, description, price, images);

        return ResponseEntity.status(HttpStatus.CREATED).body(postingId);
    }

    @PutMapping(value = "/{id}/car", consumes = {"multipart/form-data"})
    @PreAuthorize("@postingsService.isOwner(#id, authentication.principal.user.id)")
    public ResponseEntity<Void> updateById(
            @PathVariable UUID id,
            @RequestBody PostingRequestDto postingRequestDto) {

        BigDecimal price = postingRequestDto.getPrice();
        String description = postingRequestDto.getDescription();

        UUID carId = postingsService.getCarIdByPostingId(id);
        carsService.update(carId, carMapper.requestDtoToEntity(postingRequestDto.getCarRequest()));
        postingsService.updatePriceAndDescription(id, price, description);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}/images", consumes = {"multipart/form-data"})
    @PreAuthorize("@postingsService.isOwner(#id, authentication.principal.user.id)")
    public ResponseEntity<Void> updateImagesById(
            @PathVariable UUID id,
            @RequestPart("images") List<MultipartFile> images) {

        postingsService.updatePhotosById(id, images);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@postingsService.isOwner(#id, authentication.principal.user.id)")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {

        postingsService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
