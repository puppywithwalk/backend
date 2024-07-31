package com.puppypaws.project.repository;

import com.puppypaws.project.dto.Community.CommunityResponseDto;
import com.puppypaws.project.dto.Community.CommunitySearchCondition;
import com.puppypaws.project.entity.Community;
import com.puppypaws.project.entity.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
class CommunityRepositoryTest {
    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    EntityManager em;
    @Autowired
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void setMemberAndCommunityData(){
        queryFactory = new JPAQueryFactory(em);

        for(int i=0; i < 100; i++){
            String name = "member" + i;
            String email = "member" + i + "@naver.com";
            String provider = "K";
            Member member = new Member(name, email,provider);
            em.persist(member);

            String description = "TEST" + i;
            String pickLocation = "당산동 4가 " + i;
            String status = i % 2 == 0 ? "Y" : "N";
            em.persist(new Community(member, description, new Date(), pickLocation, status));
        }
    }
    @Test
    public void searchCommunitiesByConditionsTest(){
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("created_at").descending());
        CommunitySearchCondition communitySearchCondition = new CommunitySearchCondition();
        communitySearchCondition.setPickupLocation("4가 9");
        communitySearchCondition.setStatus("Y");
        Slice<CommunityResponseDto> communitiesByConditions = communityRepository.findCommunitiesByConditions(communitySearchCondition, pageRequest);
        List<CommunityResponseDto> result = communitiesByConditions.getContent();

        assertThat(result).extracting("pickupLocation").containsExactly("당산동 4가 98","당산동 4가 96","당산동 4가 94","당산동 4가 92","당산동 4가 90");
    }

    @Test
    public void searchCommunityResponseDtoByConditionsWithNullValuesTest() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CommunitySearchCondition communitySearchCondition = new CommunitySearchCondition();
        Slice<CommunityResponseDto> communitiesByConditions = communityRepository.findCommunitiesByConditions(communitySearchCondition, pageRequest);
        List<CommunityResponseDto> result = communitiesByConditions.getContent();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(10);
    }

    @Test
    public void paginationTest() {
        PageRequest firstPageRequest = PageRequest.of(0, 10);
        CommunitySearchCondition communitySearchCondition = new CommunitySearchCondition();
        Slice<CommunityResponseDto> firstPage = communityRepository.findCommunitiesByConditions(communitySearchCondition, firstPageRequest);
        List<CommunityResponseDto> firstPageResults = firstPage.getContent();

        assertThat(firstPageResults).hasSize(10);

        PageRequest secondPageRequest = PageRequest.of(1, 10);
        Slice<CommunityResponseDto> secondPage = communityRepository.findCommunitiesByConditions(communitySearchCondition, secondPageRequest);
        List<CommunityResponseDto> secondPageResults = secondPage.getContent();

        assertThat(secondPageResults).hasSize(10);
        assertThat(firstPageResults).doesNotContainAnyElementsOf(secondPageResults);
    }
}