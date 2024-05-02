package com.puppypaws.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;

import java.util.Date;

@Setter
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
    private Date createdAt;
}