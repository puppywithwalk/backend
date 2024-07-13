package com.puppypaws.project.controller;

import com.puppypaws.project.dto.Dogstagram.DogstagramPatchRequestDto;
import com.puppypaws.project.dto.Dogstagram.DogstagramResponseDto;
import com.puppypaws.project.entity.Dogstagram;
import com.puppypaws.project.service.DogstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class DogstagramController {

    private final DogstagramService dogstagramService;

    @Autowired
    public DogstagramController(DogstagramService dogstagramService) {
        this.dogstagramService = dogstagramService;
    }

    @GetMapping("/dogstagram")
    public List<DogstagramResponseDto> getDogstagram(
            @RequestParam(value = "take", defaultValue = "10") int take,
            @RequestParam(value = "skip", defaultValue = "0") int skip
    ){
        return dogstagramService.getList(take,skip);
    }

    @GetMapping("/dogstagram/star-dogs")
    public List<DogstagramResponseDto> getStarDogstagram(){
        return dogstagramService.getStarDogList();
    }

    @GetMapping("/dogstagram/search")
    public List<DogstagramResponseDto> searchDogstagrams(
            @RequestParam(value = "search_word", required = false) String searchWord,
            @RequestParam(value = "take", defaultValue = "10") int take,
            @RequestParam(value = "skip", defaultValue = "0") int skip) {

        return dogstagramService.searchDogstagrams(searchWord, take, skip);
    }

    @GetMapping("/dogstagram/{id}")
    public Optional<Dogstagram> getOne(
            @PathVariable(value = "id") Long id
    ){
        return dogstagramService.getOne(id);
    }

    @PostMapping("/dogstagram")
    public ResponseEntity<String> postDogstagram (
            @RequestPart(value = "image") MultipartFile image,
            @RequestPart(value = "image2", required = false) MultipartFile image2,
            @RequestPart(value = "image3", required = false) MultipartFile image3,
            @RequestPart(value = "description") String description
    ) throws IOException {
        return dogstagramService.postDogstagram(image, image2, image3, description);
    }

    @PatchMapping("/dogstagram/{id}")
    public ResponseEntity<String> patchDogstagram (
            @PathVariable(value = "id") Long id,
            @RequestBody() DogstagramPatchRequestDto dto
    ) {
        return dogstagramService.patchDogstagram(
                id,
                dto.getDescription()
        );
    }

    @DeleteMapping("/dogstagram/{id}")
    public ResponseEntity<String> deleteDogstagram (
            @PathVariable(value = "id") Long id
    ) {
        return dogstagramService.deleteDogstagram(id);
    }

}
