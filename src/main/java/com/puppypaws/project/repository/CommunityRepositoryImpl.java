package com.puppypaws.project.repository;

import com.puppypaws.project.dto.Community.CommunityResponseDto;
import com.puppypaws.project.dto.Community.CommunitySearchCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.puppypaws.project.entity.QCommunity.community;
import static com.puppypaws.project.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class CommunityRepositoryImpl implements CommunityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public Slice<CommunityResponseDto> findCommunitiesByConditions(CommunitySearchCondition condition, Pageable pageable) {
        List<CommunityResponseDto> contents = queryFactory
                .select(Projections.constructor(CommunityResponseDto.class,
                        community.id,
                        community.pickupLocation,
                        community.pickupDate,
                        community.status,
                        member.dogName,
                        member.dogProfileUrl,
                        member.dogType,
                        member.dogCharacter,
                        community.description,
                        member.nickname,
                        member.id.as("userId"),
                        member.profileUrl,
                        community.createdAt)
                )
                .from(community)
                .innerJoin(community.member, member)
                .where(
                        eqPickupLocation(condition.getPickupLocation()),
                        eqStatus(condition.getStatus()),
                        likeDogType(condition.getDogType())
                )
                .orderBy(community.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();


        return new SliceImpl<>(contents, pageable, hasNextPage(contents, pageable.getPageSize()));
    }

    private boolean hasNextPage(List<CommunityResponseDto> contents, int pageSize) {
        if (contents.size() > pageSize) {
            contents.remove(pageSize);
            return true;
        }
        return false;
    }

    private BooleanExpression eqPickupLocation(String pickupLocation) {
        return StringUtils.hasText(pickupLocation)
                ? community.pickupLocation.contains(pickupLocation)
                : null;
    }

    private BooleanExpression eqStatus(String status) {
        return StringUtils.hasText(status)
                ? community.status.eq(status)
                : null;
    }

    private BooleanExpression likeDogType(String dogType) {
        return StringUtils.hasText(dogType)
                ? member.dogType.contains(dogType)
                : null;
    }
}