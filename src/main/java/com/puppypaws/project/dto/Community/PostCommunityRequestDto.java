package com.puppypaws.project.dto.Community;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
public class PostCommunityRequestDto {
    private String pickup_location;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date  pickup_date;
    private String description;
}
