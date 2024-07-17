package com.puppypaws.project.repository;

import com.puppypaws.project.entity.DogstagramLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DogstagramLikeRepository extends JpaRepository<DogstagramLike, Long> {
    Optional<DogstagramLike> findOneByDogstagramIdAndUserId(Long dogstagram_id, Long user_id);
}
