package com.puppypaws.project.repository;

import com.puppypaws.project.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>, CommunityRepositoryCustom {
    @EntityGraph(attributePaths = {"member"})
    Optional<Community> findById(long id);

    @EntityGraph(attributePaths = {"member"})
    Page<Community> findAll(Pageable pageable);

    List<Community> findByMember_Id(long user_id);
}
