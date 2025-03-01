package com.sparkplug.sparkplugbackend.user.model.entity;

import com.sparkplug.sparkplugbackend.posting.model.Posting;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sparkplug_user")
public class SparkplugUser {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "Username must not be empty.")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters long.")
    private String username;

    @Column
    private String password;

    @Column
    private String authority;

    @Column
    private String profilePictureUrl;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Posting> postings;

    @ManyToMany
    @JoinTable(
            name = "posting_wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "posting_id"))
    private List<Posting> wishlistedPostings = new ArrayList<>();

    public SparkplugUser(){}

    public SparkplugUser(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotEmpty(message = "Username must not be empty.") @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long.") String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty(message = "Username must not be empty.") @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long.") String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public List<Posting> getPostings() {
        return postings;
    }

    public void setPostings(List<Posting> postings) {
        this.postings = postings;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<Posting> getWishlistedPostings() {
        return wishlistedPostings;
    }

    public void setWishlistedPostings(List<Posting> wishlistedPostings) {
        this.wishlistedPostings = wishlistedPostings;
    }
}
