package com.puppypaws.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.puppypaws.project.entity.Attachment;

import java.util.Date;

public interface IDogstagram {
    Long getId();
    Long getUser_Id();
    String getNickname();
    String getProfile_Url();
    String getDescription();

    String getDog_Type();
    String getUrl();
    String getUrl2();
    String getUrl3();
    Boolean getIs_Liked();
    Integer getTotal_Like();
    String getLast_Liked_Nickname();
    Date getCreated_At();

    Attachment getAttachment();
}