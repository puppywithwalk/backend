package com.puppypaws.project.controller;

import com.puppypaws.project.dto.Profile.Request.DogProfileUpdateRequestDto;
import com.puppypaws.project.dto.Profile.Request.ProfileUpdateRequestDto;
import com.puppypaws.project.dto.Profile.Response.ProfileResponseDto;
import com.puppypaws.project.entity.Member;
import com.puppypaws.project.repository.MemberRepository;
import com.puppypaws.project.service.ProfileService;
import com.puppypaws.project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final MemberRepository memberRepository;
    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseDto> getProfile() {
        return profileService.getProfile(SecurityUtil.getAuthenticatedUserId());
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<ProfileResponseDto> getUserProfile(
            @PathVariable(value = "id") Long id) {
        return profileService.getProfile(id);
    }

    @PostMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @RequestBody() ProfileUpdateRequestDto dto) {
        Optional<Member> memberOpt = memberRepository.findById(SecurityUtil.getAuthenticatedUserId());
        Member member = memberOpt.get();

        if (dto.getNickname() != null) {
            member.setNickname(dto.getNickname());
        }

        memberRepository.save(member);

        return ResponseEntity.ok("fin");
    }

    @PostMapping(value = "/image/upload")
    @ResponseBody
    public ResponseEntity<String> uploadImage(
            @RequestParam(name = "type") String type,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return this.profileService.uploadImage(type, image);
    }

    @PostMapping(value = "/dog-profile")
    @ResponseBody
    public ResponseEntity<String> updateDogProfile(
            @RequestBody() DogProfileUpdateRequestDto dto) {
        Optional<Member> memberOpt = memberRepository.findById(SecurityUtil.getAuthenticatedUserId());
        Member member = memberOpt.get();

        member.setDogCharacter(dto.getDog_character());
        member.setDogCharacter2(dto.getDog_character2());

        if (dto.getDog_type() != null) {
            member.setDogType(dto.getDog_type());
        }

        if (dto.getDog_name() != null) {
            member.setDogName(dto.getDog_name());
        }

        memberRepository.save(member);

        return ResponseEntity.ok("fin");
    }

    @DeleteMapping("/dog-profile")
    @ResponseBody
    public ResponseEntity<String> deleteDogProfile() {
        Optional<Member> memberOpt = memberRepository.findById(SecurityUtil.getAuthenticatedUserId());
        Member member = memberOpt.get();

        member.setDogName("");
        member.setDogProfileUrl("");
        member.setDogCharacter("");
        member.setDogCharacter2("");
        member.setDogType("");

        memberRepository.save(member);
        return ResponseEntity.ok("fin");

    }

}
