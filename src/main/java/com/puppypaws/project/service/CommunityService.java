package com.puppypaws.project.service;

import com.puppypaws.project.dto.Community.CommunityResponseDto;
import com.puppypaws.project.dto.Community.PostCommunityRequestDto;
import com.puppypaws.project.entity.Member;
import com.puppypaws.project.repository.MemberRepository;
import com.puppypaws.project.util.SecurityUtil;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import com.puppypaws.project.entity.Community;
import com.puppypaws.project.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private ModelMapper modelMapper = new ModelMapper();

    public List<CommunityResponseDto> getList(int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<Community> result = communityRepository.findAll(pageRequest);
        return result.getContent().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public CommunityResponseDto getOne(long id){
        Optional<Community> communityOptional = communityRepository.findById(id);

        if (communityOptional.isPresent()) {
            Community community = communityOptional.get();
            return this.convertToDto(community);
        } else {
            return null;
        }
    }

    public List<CommunityResponseDto> getCommunitiesByConditions(int pageNo, int pageSize, String pickupLocation, String status, String dogType) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Page<Community> result = communityRepository.findCommunitiesByConditions(pickupLocation, status, dogType, pageRequest);
        return result.getContent().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public ResponseEntity<String> postCommunity (
            PostCommunityRequestDto postCommunityRequestDto
    ) {
        if (SecurityUtil.getAuthenticatedUserId() == null) {
            return ResponseEntity.status(500).body("UserId 확인해주세요.");
        }

        Optional<Member> member = memberRepository.findById(SecurityUtil.getAuthenticatedUserId());
        if (member.isEmpty()) {
            return ResponseEntity.status(400).body("no user");
        }

        Community newCommunity = new Community();
        newCommunity.setMember(member.get());
        newCommunity.setDescription(postCommunityRequestDto.getDescription());
        newCommunity.setPickupDate(postCommunityRequestDto.getPickup_date());
        newCommunity.setPickupLocation(postCommunityRequestDto.getPickup_location());
        newCommunity.setStatus("N");

        communityRepository.save(newCommunity);

        return ResponseEntity.ok("fin");
    }

    public ResponseEntity<String> deleteCommunity(Long id) throws BadRequestException {
        try {
            Community delelteCommunity = this.validateUserOwnership(id);

            communityRepository.delete(delelteCommunity);
            return ResponseEntity.ok("fin");
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<String> patchCommunity(
            Long id,
            PostCommunityRequestDto postCommunityRequestDto
    ) throws BadRequestException {
        try {
            Community newCommunity = this.validateUserOwnership(id);

            if (postCommunityRequestDto.getPickup_location() != null) {
                newCommunity.setPickupLocation(postCommunityRequestDto.getPickup_location());
            }

            if (postCommunityRequestDto.getDescription() != null) {
                newCommunity.setDescription(postCommunityRequestDto.getDescription());
            }

            if (postCommunityRequestDto.getPickup_date() != null) {
                newCommunity.setPickupDate(postCommunityRequestDto.getPickup_date());
            }

            communityRepository.save(newCommunity);
            return ResponseEntity.ok("fin");
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<String> patchStatus (Long id) throws BadRequestException {
        try {
            Community community = this.validateUserOwnership(id);
            community.setStatus("Y");
            communityRepository.save(community);

            return ResponseEntity.ok("fin");
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Community validateUserOwnership(Long id) throws BadRequestException {
        if (SecurityUtil.getAuthenticatedUserId() == null) {
            throw new BadRequestException("UserId 확인해주세요.");
        }

        Optional<Community> community = communityRepository.findById(id);

        if (community.isEmpty()) {
           throw new BadRequestException("community Id를 확인해주세요");
        }

        Community communityEntity = community.get();
        Long communityMemberId = communityEntity.getMember().getId();

        if (!Objects.equals(SecurityUtil.getAuthenticatedUserId(), communityMemberId)) {
            throw new BadRequestException("작성자가 아닙니다.");
        }

        return communityEntity;
    }

    private CommunityResponseDto convertToDto(Community community) {
        modelMapper.typeMap(Community.class, CommunityResponseDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getMember().getDogName(), CommunityResponseDto::setDogName);
            mapper.map(src -> src.getMember().getDogProfileUrl(), CommunityResponseDto::setDogProfileUrl);
            mapper.map(src -> src.getMember().getDogType(), CommunityResponseDto::setDogType);
            mapper.map(src -> src.getMember().getDogCharacter(), CommunityResponseDto::setDogCharacter);
            mapper.map(src -> src.getMember().getNickname(), CommunityResponseDto::setNickname);
            mapper.map(src -> src.getMember().getId(), CommunityResponseDto::setUserId);
            mapper.map(src -> src.getMember().getProfileUrl(), CommunityResponseDto::setProfileUrl);
            mapper.map(Community::getId, CommunityResponseDto::setId);
            mapper.map(Community::getPickupLocation, CommunityResponseDto::setPickupLocation);
            mapper.map(Community::getPickupDate, CommunityResponseDto::setPickupDate);
            mapper.map(Community::getStatus, CommunityResponseDto::setStatus);
            mapper.map(Community::getDescription, CommunityResponseDto::setDescription);
            mapper.map(Community::getCreatedAt, CommunityResponseDto::setCreatedAt);
        });
        return modelMapper.map(community, CommunityResponseDto.class);
    }

}
