package com.puppypaws.project.service;

import com.puppypaws.project.dto.Community.CommunityResponseDto;
import com.puppypaws.project.dto.Community.CommunitySearchCondition;
import com.puppypaws.project.dto.Community.PostCommunityRequestDto;
import com.puppypaws.project.entity.Member;
import com.puppypaws.project.exception.ErrorCode;
import com.puppypaws.project.exception.common.NotFoundException;
import com.puppypaws.project.repository.MemberRepository;
import com.puppypaws.project.util.SecurityUtil;
import org.modelmapper.ModelMapper;
import com.puppypaws.project.entity.Community;
import com.puppypaws.project.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public List<CommunityResponseDto> getList(int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
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

    public List<CommunityResponseDto> getCommunitiesByConditions(CommunitySearchCondition communitySearchCondition, int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
        Slice<CommunityResponseDto> result = communityRepository.findCommunitiesByConditions(communitySearchCondition, pageRequest);

        return result.getContent();
    }

    @Transactional
    public ResponseEntity<String> postCommunity (PostCommunityRequestDto postCommunityRequestDto) {
        Long authenticatedUserId = SecurityUtil.getAuthenticatedUserId();

        Optional<Member> memberOptional = memberRepository.findById(authenticatedUserId);
        if (memberOptional .isEmpty()) {
           throw new NotFoundException(ErrorCode.NOT_AUTHOR);
        }

        Member member  = memberOptional.get();
        Community community = createCommunity(postCommunityRequestDto, member);

        communityRepository.save(community);

        return ResponseEntity.ok("fin");
    }

    private Community createCommunity(PostCommunityRequestDto postCommunityRequestDto, Member member) {
        return new Community(
                member,
                postCommunityRequestDto.getDescription(),
                postCommunityRequestDto.getPickup_date(),
                postCommunityRequestDto.getPickup_location(),
                "N"
        );
    }

    @Transactional
    public ResponseEntity<String> deleteCommunity(Long id) {
            Community delelteCommunity = this.validateUserOwnership(id);

            communityRepository.delete(delelteCommunity);
            return ResponseEntity.ok("fin");
    }

    @Transactional
    public ResponseEntity<String> patchCommunity(Long id, PostCommunityRequestDto requestDto) {
            Community community = this.validateUserOwnership(id);
            updateCommunityFields(requestDto, community);

            return ResponseEntity.ok("fin");
    }

    private void updateCommunityFields(PostCommunityRequestDto requestDto, Community community) {
        Optional.ofNullable(requestDto.getPickup_location())
                .ifPresent(community::setPickupLocation);

        Optional.ofNullable(requestDto.getDescription())
                .ifPresent(community::setDescription);

        Optional.ofNullable(requestDto.getPickup_date())
                .ifPresent(community::setPickupDate);
    }

    @Transactional
    public ResponseEntity<String> patchStatus (Long id) {
        Community community = this.validateUserOwnership(id);
        community.setStatus("Y");

        return ResponseEntity.ok("fin");
    }

    private Community validateUserOwnership(Long id) {
        Optional<Community> community = communityRepository.findById(id);

        if (community.isEmpty()) {
           throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        Community communityEntity = community.get();
        Long communityMemberId = communityEntity.getMember().getId();

        if (!Objects.equals(SecurityUtil.getAuthenticatedUserId(), communityMemberId)) {
            throw new  NotFoundException(ErrorCode.NOT_AUTHOR);
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
