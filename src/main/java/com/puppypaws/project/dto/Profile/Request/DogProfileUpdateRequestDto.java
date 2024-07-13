package com.puppypaws.project.dto.Profile.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DogProfileUpdateRequestDto {
    private MultipartFile dog_profile_image;
    private String dog_character;
    private String dog_type;
    private String dog_name;
}
