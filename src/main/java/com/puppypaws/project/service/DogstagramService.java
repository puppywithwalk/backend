package com.puppypaws.project.service;

import com.puppypaws.project.dto.Dogstagram.DogstagramResponseDto;
import com.puppypaws.project.entity.Attachment;
import com.puppypaws.project.entity.Dogstagram;
import com.puppypaws.project.entity.DogstagramLike;
import com.puppypaws.project.entity.Member;
import com.puppypaws.project.exception.ErrorCode;
import com.puppypaws.project.exception.common.NotFoundException;
import com.puppypaws.project.model.IDogstagram;
import com.puppypaws.project.repository.DogstagramLikeRepository;
import com.puppypaws.project.repository.DogstagramRepository;
import com.puppypaws.project.repository.MemberRepository;
import com.puppypaws.project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DogstagramService {
    private final MemberRepository memberRepository;
    private final DogstagramRepository dogstagramRepository;
    private final DogstagramLikeRepository dogstagramLikeRepository;
    private final AwsS3UploadService awsS3UploadService;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(DogstagramService.class);

    public List<DogstagramResponseDto> getList(int take, int skip) {
        Long id = SecurityUtil.getAuthenticatedUserIdFromContext();
        List<IDogstagram> dogstagrams = dogstagramRepository.getDogstagramList(id, take, skip);

        return dogstagrams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DogstagramResponseDto> getStarDogList() {
        Long id = SecurityUtil.getAuthenticatedUserIdFromContext();
        List<IDogstagram> dogstagrams = dogstagramRepository.getStarDogstagramList(id);

        // Map IDogstagram entities to DogstagramResponseDto using ModelMapper
        return dogstagrams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<DogstagramResponseDto> searchDogstagrams(String searchWord, int take, int skip) {
        Long id = SecurityUtil.getAuthenticatedUserIdFromContext();
        List<IDogstagram> dogstagrams = dogstagramRepository.searchDogstagramBy(id, searchWord, take, skip);
        return dogstagrams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<Dogstagram> getOne(Long id) {
        return dogstagramRepository.findById(id);
    }

    @Transactional
    public ResponseEntity<String> postDogstagram(
            MultipartFile image,
            MultipartFile image2,
            MultipartFile image3,
            String description) {
        Optional<Member> member = memberRepository.findById(SecurityUtil.getAuthenticatedUserId());
        if (member.isEmpty()) {
            throw new NotFoundException(ErrorCode.NO_USER);
        }

        try{
                Dogstagram dogstagram = new Dogstagram();
                Attachment attachment = new Attachment();

                dogstagram.setMember(member.get());
                dogstagram.setDescription(description);

                String url = awsS3UploadService.saveFile(image, "dogstagram");
                attachment.setUrl(url);

                if (image2 != null) {
                    String url2 = awsS3UploadService.saveFile(image2, "dogstagram");
                    attachment.setUrl2(url2);
                }

                if (image3 != null) {
                    String url3 = awsS3UploadService.saveFile(image3, "dogstagram");
                    attachment.setUrl3(url3);
                }
                dogstagram.setAttachment(attachment);

                dogstagramRepository.save(dogstagram);

                return ResponseEntity.ok("fin");
        } catch(IOException e){
            return ResponseEntity.internalServerError().body("다시 시도해보세요");
        }
    }

    @Transactional
    public ResponseEntity<String> patchDogstagram(
            Long id,
            String description) {
        Dogstagram dogstagram = this.validateUserOwnership(id);
        dogstagram.setDescription(description);

        return ResponseEntity.ok("fin");
    }

    @Transactional
    public ResponseEntity<String> deleteDogstagram(Long id) {
        Dogstagram dogstagram = this.validateUserOwnership(id);

        dogstagramRepository.delete(dogstagram);

        return ResponseEntity.ok("fin");
    }

    @Transactional
    public ResponseEntity<String> like(Long dogstaramId){
        Long id = SecurityUtil.getAuthenticatedUserId();

        Optional<Dogstagram> dogstagram = dogstagramRepository.findById(dogstaramId);
        if(dogstagram.isEmpty()){
            return ResponseEntity.badRequest().body("dogstagram 확인해주세요.");
        }

        Optional<DogstagramLike> dogstagramLike = dogstagramLikeRepository.findOneByDogstagramIdAndUserId(dogstagram.get().getId(), id);

        if(dogstagramLike.isEmpty()) {
            DogstagramLike newDogstagramLike = new DogstagramLike();
            newDogstagramLike.setDogstagram(dogstagram.get());
            newDogstagramLike.setUserId(id);

            dogstagramLikeRepository.save(newDogstagramLike);
        }else{
            dogstagramLikeRepository.delete(dogstagramLike.get());
        }

        return ResponseEntity.ok("good");
    }

    private Dogstagram validateUserOwnership(Long id) {
        Optional<Dogstagram> dogstagram = dogstagramRepository.findById(id);

        if(dogstagram.isEmpty()) {
            throw new NotFoundException(ErrorCode.NOT_FOUND);
        }

        Dogstagram dogstagramEntity = dogstagram.get();
        Long dogstagramMemberId = dogstagramEntity.getMember().getId();

        if(!Objects.equals(SecurityUtil.getAuthenticatedUserId(), dogstagramMemberId)){
            throw new NotFoundException(ErrorCode.NOT_AUTHOR);
        }

        return dogstagramEntity;
    }

    private DogstagramResponseDto convertToDto(IDogstagram dogstagram) {
        DogstagramResponseDto dto = modelMapper.map(dogstagram, DogstagramResponseDto.class);

        dto.setUser_id(dogstagram.getUser_Id());
        String[] imageUrls = {dogstagram.getUrl(), dogstagram.getUrl2(), dogstagram.getUrl3()};
        dto.setImageUrls(imageUrls);

        return dto;
    }
}