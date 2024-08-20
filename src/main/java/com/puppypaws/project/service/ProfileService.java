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
import com.puppypaws.project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final AwsS3UploadService awsS3UploadService;
    private final MemberRepository memberRepository;
    private final DogstagramRepository dogstagramRepository;
    private final CommunityRepository communityRepository;
    private final ModelMapper modelMapper;

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

    public ResponseEntity<String> uploadImage(String type, MultipartFile image) {
        Optional<Member> memberOpt = memberRepository.findById(SecurityUtil.getAuthenticatedUserId());
        if (memberOpt.isEmpty()) {
            return ResponseEntity.status(400).body("Member not found");
        }

        Member member = memberOpt.get();

        try {
            updateProfileImage(type, image, member);
            memberRepository.save(member);
            return ResponseEntity.ok("fin");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Profile update failed: " + e.getMessage());
        }
    }


    private void updateProfileImage(String type, MultipartFile image, Member member) throws IOException {
        if (!type.equals("dog-profile") && !type.equals("profile")) {
            throw new IllegalArgumentException("Invalid profile type: " + type);
        }

        String url = (image != null) ? awsS3UploadService.saveFile(image, type) : null;

        if (type.equals("dog-profile")) {
            member.setDogProfileUrl(url);
        } else {
            member.setProfileUrl(url);
        }
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
