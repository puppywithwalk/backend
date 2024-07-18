package com.puppypaws.project.dto.Community;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class CommunityResponseDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("pickup_location")
    private String pickupLocation;

    @JsonProperty("pickup_date")
    private Date pickupDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("dog_name")
    private String dogName;

    @JsonProperty("dog_profile_url")
    private String dogProfileUrl;

    @JsonProperty("dog_type")
    private String dogType;

    @JsonProperty("dog_character")
    private String dogCharacter;

    @JsonProperty("description")
    private String description;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("profile_url")
    private String profileUrl;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;


    public CommunityResponseDto(Long id, String pickupLocation, Date pickupDate, String status, String dogName, String dogProfileUrl, String dogType, String dogCharacter, String description, String nickname, Long userId, String profileUrl, LocalDateTime createdAt) {
        this.id = id;
        this.pickupLocation = pickupLocation;
        this.pickupDate = pickupDate;
        this.status = status;
        this.dogName = dogName;
        this.dogProfileUrl = dogProfileUrl;
        this.dogType = dogType;
        this.dogCharacter = dogCharacter;
        this.description = description;
        this.nickname = nickname;
        this.userId = userId;
        this.profileUrl = profileUrl;
        this.createdAt = createdAt;
    }
}