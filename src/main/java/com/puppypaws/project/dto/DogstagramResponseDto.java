package com.puppypaws.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DogstagramResponseDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("profile_url")
    private String profileUrl;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image_urls")
    private String[] imageUrls;

    @JsonProperty("is_liked")
    private Boolean isLiked;

    @JsonProperty("total_like")
    private Integer totalLike;

    @JsonProperty("last_liked_nickname")
    private String lastLikedNickname;

    @JsonProperty("created_at")
    private Date createdAt;
}