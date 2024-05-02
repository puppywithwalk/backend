package com.puppypaws.project.service;

import com.puppypaws.project.dto.CommunityResponseDto;
import org.modelmapper.ModelMapper;
import com.puppypaws.project.entity.Community;
import com.puppypaws.project.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
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
