package com.puppypaws.project.service;

import com.puppypaws.project.dto.DogstagramResponseDto;
import com.puppypaws.project.model.IDogstagram;
import com.puppypaws.project.repository.DogstagramRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogstagramService {
    private final DogstagramRepository dogstagramRepository;
    private static final Logger logger = LoggerFactory.getLogger(DogstagramService.class);

    private ModelMapper modelMapper = new ModelMapper();

    public List<DogstagramResponseDto> getList(int take, int skip) {
        Long id = null;
        List<IDogstagram> dogstagrams = dogstagramRepository.getDogstagramList(id, take, skip);

        return dogstagrams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DogstagramResponseDto> getStarDogList() {
        Long id = null;
        List<IDogstagram> dogstagrams = dogstagramRepository.getStarDogstagramList(id);

        // Map IDogstagram entities to DogstagramResponseDto using ModelMapper
        return dogstagrams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DogstagramResponseDto convertToDto(IDogstagram dogstagram) {
        DogstagramResponseDto dto = modelMapper.map(dogstagram, DogstagramResponseDto.class);

        String[] imageUrls = {dogstagram.getUrl(), dogstagram.getUrl2(), dogstagram.getUrl3()};
        dto.setImageUrls(imageUrls);

        return dto;
    }
}