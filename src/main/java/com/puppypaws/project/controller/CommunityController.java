package com.puppypaws.project.controller;

import com.puppypaws.project.dto.CommunityResponseDto;
import com.puppypaws.project.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
