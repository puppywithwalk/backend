package com.puppypaws.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public interface IDogstagram {
    Long getId();
    Long getUserId();
    String getNickname();
    String getProfileUrl();
    String getDescription();
    String getUrl();
    String getUrl2();
    String getUrl3();
    Boolean getIsLiked();
    Integer getTotalLike();
    String getLastLikedNickname();
    Date getCreatedAt();
}