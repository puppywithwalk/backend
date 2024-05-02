package com.puppypaws.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.TimeZoneColumn;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Dogstagram")
public class Dogstagram {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dogstagram_id_generator")
    @SequenceGenerator(name="dogstagram_id_generator", sequenceName="dogstagram_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", updatable = false)
    @TimeZoneColumn
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @TimeZoneColumn
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
    private Member member;
}