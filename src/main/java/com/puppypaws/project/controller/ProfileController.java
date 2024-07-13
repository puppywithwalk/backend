package com.puppypaws.project.controller;

import ch.qos.logback.classic.Logger;
import com.puppypaws.project.dto.Profile.Response.ProfileResponseDto;
import com.puppypaws.project.entity.Member;
import com.puppypaws.project.repository.MemberRepository;
import com.puppypaws.project.service.AwsS3UploadService;
import com.puppypaws.project.service.ProfileService;
import com.puppypaws.project.util.SecurityUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class ProfileController {
    private final AwsS3UploadService awsS3UploadService;
    private final MemberRepository memberRepository;
    private final ProfileService profileService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    public ProfileController(
            AwsS3UploadService awsS3UploadService,
            MemberRepository memberRepository,
            ProfileService profileService) {
        this.awsS3UploadService = awsS3UploadService;
        this.memberRepository = memberRepository;
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseDto> getProfile() {
        if (SecurityUtil.getAuthenticatedUserId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        return profileService.getProfile(SecurityUtil.getAuthenticatedUserId());
    }

    @PostMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @RequestPart(value = "profile_image", required = false) MultipartFile image,
            @RequestPart(value = "nickname", required = false) String nickname) {
        if (SecurityUtil.getAuthenticatedUserId() == null) {
            return ResponseEntity.status(400).body("UserId is null");
        }

        Optional<Member> memberOpt = memberRepository.findById(SecurityUtil.getAuthenticatedUserId());
        Member member = memberOpt.get();

        try {
            if (image != null) {
                String url = awsS3UploadService.saveFile(image, "profile");
                member.setProfileUrl(url);
            }

            if (nickname != null) {
                member.setNickname(nickname);
            }

            memberRepository.save(member);

            return ResponseEntity.ok("fin");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("profile update failed: " + e.getMessage());
        }

    }

    @PostMapping(value = "/dog-profile")
    @ResponseBody
    public ResponseEntity<String> updateDogProfile(
            @RequestPart(value = "dog_profile_image", required = false) MultipartFile image,
            @RequestPart(value = "dog_character", required = false) String dog_character1,
            @RequestPart(value = "dog_character2", required = false) String dog_character2,
            @RequestPart(value = "dog_type", required = false) String dog_type,
            @RequestPart(value = "dog_name", required = false) String dog_name) {
        if (SecurityUtil.getAuthenticatedUserId() == null) {
            return ResponseEntity.status(400).body("UserId is null");
        }

        Optional<Member> memberOpt = memberRepository.findById(SecurityUtil.getAuthenticatedUserId());
        Member member = memberOpt.get();
        logger.info(member.toString());

        try {
            if (image != null) {
                String url = awsS3UploadService.saveFile(image, "dog-profile");
                member.setDogProfileUrl(url);
            } else {
                member.setDogProfileUrl(null);
            }

            member.setDogCharacter(dog_character1);
            member.setDogCharacter2(dog_character2);

            if (dog_type != null) {
                member.setDogType(dog_type);
            }

            if (dog_name != null) {
                member.setDogName(dog_name);
            }

            memberRepository.save(member);

            return ResponseEntity.ok("fin");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("dog profile update failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/dog-profile")
    @ResponseBody
    public ResponseEntity<String> deleteDogProfile() {
        if (SecurityUtil.getAuthenticatedUserId() == null) {
            return ResponseEntity.status(400).body("UserId is null");
        }

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
