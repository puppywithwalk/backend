package com.puppypaws.project.controller;

import com.puppypaws.project.dto.Community.CommunityResponseDto;
import com.puppypaws.project.dto.Community.PostCommunityRequestDto;
import com.puppypaws.project.service.CommunityService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommunityController {
    private final CommunityService communityService;

    @Autowired
    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @GetMapping("/")
    public String success(){
        return "success";
    }
    @GetMapping("/community")
    public List<CommunityResponseDto> getCommunityList(
            @RequestParam(name = "skip", defaultValue = "0") int pageNo,
            @RequestParam(name = "take", defaultValue = "10") int pageSize) {
        return communityService.getList(pageNo, pageSize);
    }
    @GetMapping("/community/{id}")
    public CommunityResponseDto getCommunityOne(
            @PathVariable(name = "id") long id
    ) {
        return communityService.getOne(id);
    }

    @GetMapping("/community/search")
    public List<CommunityResponseDto> searchCommunityList(
            @RequestParam String pickupLocation,
            @RequestParam String status,
            @RequestParam String dogType,
            @RequestParam(name = "skip", defaultValue = "0") int pageNo,
            @RequestParam(name = "take", defaultValue = "10") int pageSize) {
        return communityService.getCommunitiesByConditions(pageNo, pageSize, pickupLocation, status, dogType);
    }

    @PostMapping("/community")
    public ResponseEntity<String> postCommunity(
            @RequestBody() PostCommunityRequestDto postCommunityRequestDto
    ) {
        return communityService.postCommunity(postCommunityRequestDto);
    }

    @DeleteMapping("/community/{id}")
    public ResponseEntity<String> deleteCommunity(@PathVariable(value = "id") Long id) {
        return communityService.deleteCommunity(id);
    }

    @PatchMapping("/community/{id}")
    public ResponseEntity<String> patchCommunity(@PathVariable(value = "id") Long id, @RequestBody(required = false) PostCommunityRequestDto postCommunityRequestDto) {
        return communityService.patchCommunity(id, postCommunityRequestDto);
    }

    @PatchMapping("/community/status/{id}")
    public ResponseEntity<String> patchStatus (@PathVariable(value = "id") Long id) {
        return communityService.patchStatus(id);
    }
}
