package com.puppypaws.project.service;

import com.puppypaws.project.dto.Dogstagram.DogstagramRequestDto;
import com.puppypaws.project.dto.Dogstagram.DogstagramResponseDto;
import com.puppypaws.project.entity.Attachment;
import com.puppypaws.project.entity.Dogstagram;
import com.puppypaws.project.entity.Member;
import com.puppypaws.project.model.IDogstagram;
import com.puppypaws.project.repository.AttachmentRepository;
import com.puppypaws.project.repository.DogstagramRepository;
import com.puppypaws.project.repository.MemberRepository;
import com.puppypaws.project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DogstagramService {
    private final MemberRepository memberRepository;
    private final DogstagramRepository dogstagramRepository;
    private final AttachmentRepository attachmentRepository;
    private final AwsS3UploadService awsS3UploadService;
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

    public List<DogstagramResponseDto> searchDogstagrams(String searchWord, int take, int skip) {
        List<IDogstagram> dogstagrams = dogstagramRepository.searchDogstagramBy(SecurityUtil.getAuthenticatedUserId(), searchWord, take, skip);
        return dogstagrams.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<Dogstagram> getOne(Long id) {
        return dogstagramRepository.findById(id);
    }

    public ResponseEntity<String> postDogstagram(
            MultipartFile image,
            MultipartFile image2,
            MultipartFile image3,
            String description) throws IOException {
        if (SecurityUtil.getAuthenticatedUserId() == null) {
            return ResponseEntity.status(400).body("UserId is null");
        }

        Optional<Member> member = memberRepository.findById(SecurityUtil.getAuthenticatedUserId());
        if (member.isEmpty()) {
            return ResponseEntity.status(400).body("no user");
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
                attachment.setDogstagram(dogstagram);
                dogstagram.setAttachment(attachment);

                dogstagramRepository.save(dogstagram);

                return ResponseEntity.ok("fin");
        } catch(IOException e){
            return ResponseEntity.internalServerError().body("다시 시도해보세요");
        }
    }

    public ResponseEntity<String> patchDogstagram(
            Long id,
            String description) throws IOException {
        try{
            Dogstagram dogstagram = this.validateUserOwnership(id);

            dogstagram.setDescription(description);

            dogstagramRepository.save(dogstagram);
            return ResponseEntity.ok("fin");
        } catch(IOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<String> deleteDogstagram(Long id) throws BadRequestException {
        try {
            Dogstagram dogstagram = this.validateUserOwnership(id);

            dogstagramRepository.delete(dogstagram);

            return ResponseEntity.ok("fin");
        } catch (BadRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Dogstagram validateUserOwnership(Long id) throws BadRequestException {
        if (SecurityUtil.getAuthenticatedUserId() == null) {
            throw new BadRequestException("UserId is null");
        }

        Optional<Dogstagram> dogstagram = dogstagramRepository.findById(id);

        if(dogstagram.isEmpty()) {
            throw new BadRequestException("dogstagram Id를 확인해주세요");
        }

        Dogstagram dogstagramEntity = dogstagram.get();
        Long dogstagramMemberId = dogstagramEntity.getMember().getId();

        if(!Objects.equals(SecurityUtil.getAuthenticatedUserId(), dogstagramMemberId)){
            throw new BadRequestException("작성자가 아닙니다. " + SecurityUtil.getAuthenticatedUserId().toString() + " " + dogstagramMemberId.toString());
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