package com.puppypaws.project.repository;

import com.puppypaws.project.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommunityRepositoryCustom {
    Page<Community> findCommunitiesByConditions(String pickupLocation, String status, String dogType, Pageable pageable);
}
