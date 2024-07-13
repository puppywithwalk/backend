package com.puppypaws.project.dto.Profile.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileMemberResponseDto {
    private Long id;
    private String email;
    private String provider;
    private String nickname;
    private String profileUrl;
    private String dogType;
    private String dogName;
    private String[] dogCharacters;
    private String dogProfileUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
