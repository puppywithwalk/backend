package com.puppypaws.project.controller;

import com.puppypaws.project.dto.DogstagramResponseDto;
import com.puppypaws.project.service.DogstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DogstagramController {

    private final DogstagramService dogstagramService;

    @Autowired
    public DogstagramController(DogstagramService dogstagramService) {
        this.dogstagramService = dogstagramService;
    }

    @GetMapping("/dogstagram")
    public List<DogstagramResponseDto> getDogstagram(){
        return dogstagramService.getList(10,0);
    }

    @GetMapping("/dogstagram/star-dogs")
    public List<DogstagramResponseDto> getStarDogstagram(){
        return dogstagramService.getStarDogList();
    }

    @GetMapping("/dogstagram/search")
    public List<DogstagramResponseDto> searchDogstagrams(
            @RequestParam(value = "user_id") Long userId,
            @RequestParam(value = "search_word", required = false) String searchWord,
            @RequestParam(value = "take", defaultValue = "10") int take,
            @RequestParam(value = "skip", defaultValue = "0") int skip) {

        return dogstagramService.searchDogstagrams(userId, searchWord, take, skip);
    }
}
