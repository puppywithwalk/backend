package com.puppypaws.project.service;

import com.puppypaws.project.dto.Profile.Request.ProfileCommunityDto;
import com.puppypaws.project.dto.Profile.Request.ProfileDogstagramDto;
import com.puppypaws.project.dto.Profile.Response.ProfileMemberResponseDto;
import com.puppypaws.project.dto.Profile.Response.ProfileResponseDto;
import com.puppypaws.project.entity.Community;
import com.puppypaws.project.entity.Dogstagram;
import com.puppypaws.project.entity.Member;
import com.puppypaws.project.repository.CommunityRepository;
import com.puppypaws.project.repository.DogstagramRepository;
import com.puppypaws.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final MemberRepository memberRepository;
    private final DogstagramRepository dogstagramRepository;
    private final CommunityRepository communityRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public ResponseEntity<ProfileResponseDto> getProfile(Long user_id){
        Optional<Member> member = memberRepository.findById(user_id);
        List<Dogstagram> dogstagrams = dogstagramRepository.findByMember_Id(user_id);
        List<Community> communities = communityRepository.findByMember_Id(user_id);

        ProfileMemberResponseDto memberResponseDto = this.convertToMemberDto(member);
        List<ProfileDogstagramDto> dogstagramDtos = dogstagrams.stream().map(this::convertToDogstagramDto).collect(Collectors.toList());
        List<ProfileCommunityDto> communityDtos = communities.stream().map(this::convertToCommunityDto).collect(Collectors.toList());

        ProfileResponseDto dto = new ProfileResponseDto();
        dto.setMember(memberResponseDto);
        dto.setDogstagrams(dogstagramDtos);
        dto.setCommunities(communityDtos);

        return ResponseEntity.ok(dto);
    }

    private ProfileMemberResponseDto convertToMemberDto(Optional<Member> member) {
        ProfileMemberResponseDto dto = modelMapper.map(member, ProfileMemberResponseDto.class);

        dto.setId(member.get().getId());
        dto.setEmail(member.get().getEmail());
        dto.setNickname(member.get().getNickname());
        dto.setProfileUrl(member.get().getProfileUrl());
        dto.setProvider(member.get().getProvider());
        dto.setDogName(member.get().getDogName());
        dto.setDogProfileUrl(member.get().getDogProfileUrl());
        dto.setDogType(member.get().getDogType());
        String[] dogCharacters = {member.get().getDogCharacter(),  member.get().getDogCharacter2()};
        dto.setDogCharacters(dogCharacters);
        dto.setCreatedAt(member.get().getCreatedAt());
        dto.setUpdatedAt(member.get().getUpdatedAt());

        return dto;
    }

    private ProfileDogstagramDto convertToDogstagramDto(Dogstagram dogstagram) {
        ProfileDogstagramDto dto = modelMapper.map(dogstagram, ProfileDogstagramDto.class);

        dto.setId(dto.getId());
        dto.setImage_url(dogstagram.getAttachment().getUrl());

        return dto;
    }

    private ProfileCommunityDto convertToCommunityDto(Community community){
        ProfileCommunityDto dto = modelMapper.map(community, ProfileCommunityDto.class);

        dto.setId(community.getId());
        dto.setPickupDate(community.getPickupDate());
        dto.setPickupLocation(community.getPickupLocation());

        return dto;
    }
}
