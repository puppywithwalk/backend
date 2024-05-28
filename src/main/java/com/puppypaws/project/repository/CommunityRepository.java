package com.puppypaws.project.repository;

import com.puppypaws.project.entity.Community;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findById(long id);

    @Query("SELECT c FROM Community c " +
            "JOIN c.member m " +
            "WHERE (:pickupLocation IS NULL OR c.pickupLocation ILIKE CONCAT('%', :pickupLocation, '%')) " +
            "AND (:status IS NULL OR c.status ILIKE CONCAT('%', :status, '%')) " +
            "AND (m.dogType IS NULL OR m.dogType ILIKE CONCAT('%', :dogType, '%'))")
    Page<Community> findCommunitiesByConditions(@Param("pickupLocation") String pickupLocation,
                                                @Param("status") String status,
                                                @Param("dogType") String dogType,
                                                Pageable pageable);
}
