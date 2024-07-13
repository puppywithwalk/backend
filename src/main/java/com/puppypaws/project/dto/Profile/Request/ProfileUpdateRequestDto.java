package com.puppypaws.project.dto.Profile.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProfileUpdateRequestDto {
    private MultipartFile profile_image;
    private String nickname;
}


