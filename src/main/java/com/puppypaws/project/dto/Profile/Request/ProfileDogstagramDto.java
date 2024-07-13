package com.puppypaws.project.dto.Profile.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDogstagramDto {
    private int id;
    private String image_url;
}
