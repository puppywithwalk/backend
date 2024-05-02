package com.puppypaws.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import lombok.Data;
import org.apache.ibatis.annotations.Update;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TimeZoneColumn;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_id_generator")
    @SequenceGenerator(name="member_id_generator", sequenceName="member_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "dog_type")
    private String dogType;

    @Column(name = "dog_name")
    private String dogName;

    @Column(name = "dog_character")
    private String dogCharacter;

    @Column(name = "dog_profile_url")
    private String dogProfileUrl;

    @Column(name = "created_at", updatable = false)
    @TimeZoneColumn
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @TimeZoneColumn
    @LastModifiedDate
    private LocalDateTime updatedAt;

}