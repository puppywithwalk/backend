package com.puppypaws.project.dto.Profile.Response;

import com.puppypaws.project.dto.Profile.Request.ProfileCommunityDto;
import com.puppypaws.project.dto.Profile.Request.ProfileDogstagramDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private ProfileMemberResponseDto member;

    private List<ProfileCommunityDto> communities;

    private List<ProfileDogstagramDto> dogstagrams;
}