package com.sparkplug.sparkplugbackend.user.controller;

import com.sparkplug.sparkplugbackend.posting.mapper.PostingMapper;
import com.sparkplug.sparkplugbackend.user.mapper.UserMapper;
import com.sparkplug.sparkplugbackend.posting.model.Posting;
import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import com.sparkplug.sparkplugbackend.posting.dto.response.PostingResponseDto;
import com.sparkplug.sparkplugbackend.user.model.responseDto.UserResponseDto;
import com.sparkplug.sparkplugbackend.security.SparkplugUserDetails;
import com.sparkplug.sparkplugbackend.user.service.SparkplugUsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final SparkplugUsersService usersService;
    private final UserMapper userMapper;
    private final PostingMapper postingMapper;

    public UsersController(
            SparkplugUsersService usersService,
            UserMapper userMapper,
            PostingMapper postingMapper
    ) {
        this.usersService = usersService;
        this.userMapper = userMapper;
        this.postingMapper = postingMapper;
    }

    @PostMapping("/wishlist/{id}")
    public ResponseEntity<UUID> addToWishlist(
            @PathVariable("id") UUID postingId,
            @AuthenticationPrincipal SparkplugUserDetails userDetails){
        return ResponseEntity.ok(usersService.addPostingToWishlist(userDetails.getUser().getId(), postingId));
    }

    @DeleteMapping("/wishlist/{id}")
    public ResponseEntity<UUID> removeFromWishlist(
            @PathVariable("id") UUID postingId,
            @AuthenticationPrincipal SparkplugUserDetails userDetails){
        return ResponseEntity.ok(usersService.removePostingFromWishlist(userDetails.getUser().getId(), postingId));
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<PostingResponseDto>> getWishlist(
            @AuthenticationPrincipal SparkplugUserDetails userDetails){
        List<Posting> postings = usersService.getWishlist(userDetails.getUser().getId());
        return ResponseEntity.ok(
                postings.stream()
                        .map(postingMapper::entityToResponseDto)
                        .toList()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal SparkplugUserDetails userDetails){
        SparkplugUser user = usersService.getById(userDetails.getUser().getId());
        return ResponseEntity.ok(userMapper.entityToResponseDto(user));
    }

    @GetMapping("/")
    public ResponseEntity<UserResponseDto> getByUsername(@RequestParam("username") String username){
        SparkplugUser user = usersService.getByUsername(username);
        return ResponseEntity.ok(userMapper.entityToResponseDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable("id") UUID id){
        SparkplugUser user = usersService.getById(id);
        return ResponseEntity.ok(userMapper.entityToResponseDto(user));
    }

    @GetMapping("/{id:^(?!.*\\s)[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}$}/postings")
    public ResponseEntity<List<PostingResponseDto>> getPostingsByUserId(@PathVariable("id") UUID id) {
        List<Posting> postings = usersService.getPostingsById(id);

        return ResponseEntity.ok(
                postings.stream()
                        .map(postingMapper::entityToResponseDto)
                        .toList());
    }

    @GetMapping("/{username:[a-zA-Z0-9]{1,36}}/postings")
    public ResponseEntity<List<PostingResponseDto>> getPostingsByUsername(
            @PathVariable("username") String username) {
        List<Posting> postings = usersService.getPostingsByUsername(username);

        return ResponseEntity.ok(
                postings.stream()
                        .map(postingMapper::entityToResponseDto)
                        .toList());
    }

    @PutMapping(value = "/profile-picture", consumes = {"multipart/form-data"})
    public ResponseEntity<Void> updateProfilePicture(
            @RequestPart("picture") MultipartFile picture,
            @AuthenticationPrincipal SparkplugUserDetails userDetails
    ) {
        usersService.updateProfilePicture(userDetails.getUser().getId(), picture);

        return ResponseEntity.ok().build();
    }
}
