package com.puppypaws.project.repository;

import com.puppypaws.project.entity.Community;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.puppypaws.project.entity.QCommunity.community;
import static com.puppypaws.project.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class CommunityRepositoryImpl implements CommunityRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Community> findCommunitiesByConditions(String pickupLocation, String status, String dogType, Pageable pageable) {
        JPAQuery<Community> query = queryFactory
                .selectFrom(community)
                .innerJoin(community.member, member).fetchJoin()
                .where(
                        eqPickupLocation(pickupLocation),
                        eqStatus(status),
                        likeDogType(dogType)
                );

        long totalCount = query.fetchCount();

        List<Community> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    private BooleanExpression eqPickupLocation(String pickupLocation) {
        return StringUtils.hasText(pickupLocation)
                ? community.pickupLocation.like("%" + pickupLocation + "%")
                : null;
    }

    private BooleanExpression eqStatus(String status) {
        return StringUtils.hasText(status)
                ? community.status.eq(status)
                : null;
    }

    private BooleanExpression likeDogType(String dogType) {
        return StringUtils.hasText(dogType)
                ? member.dogType.like("%" + dogType + "%")
                : null;
    }
}