package com.puppypaws.project.dto.Dogstagram;

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
    private Long user_id;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("profile_url")
    private String profile_url;

    @JsonProperty("dog_type")
    private String dog_type;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image_urls")
    private String[] imageUrls;

    @JsonProperty("is_liked")
    private Boolean is_liked;

    @JsonProperty("total_like")
    private Integer total_like;

    @JsonProperty("last_liked_nickname")
    private String last_liked_nickname;

    @JsonProperty("created_at")
    private Date created_at;
}