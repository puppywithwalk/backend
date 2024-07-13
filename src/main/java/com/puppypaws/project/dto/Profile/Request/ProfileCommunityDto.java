package com.puppypaws.project.dto.Profile.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCommunityDto {
    private Long id;
    private String pickupLocation;
    private Date pickupDate;
}
