package com.puppypaws.project.repository;

import com.puppypaws.project.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.email = :email AND m.provider = :provider")
    Optional<Member> findByEmailAndProvider(@Param("email") String email, @Param("provider") String provider);
}
