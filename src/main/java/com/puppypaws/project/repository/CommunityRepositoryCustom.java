package com.puppypaws.project.repository;

import com.puppypaws.project.dto.Community.CommunityResponseDto;
import com.puppypaws.project.dto.Community.CommunitySearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CommunityRepositoryCustom {
    Slice<CommunityResponseDto> findCommunitiesByConditions(CommunitySearchCondition communitySearchCondition, Pageable pageable);
}
