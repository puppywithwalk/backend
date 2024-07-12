package com.puppypaws.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.TimeZoneColumn;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Dogstagram_Like")
public class DogstagramLike {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dogstagram_like_id_generator")
    @SequenceGenerator(name="dogstagram_like_id_generator", sequenceName="dogstagram_like_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dogstagram_id")
    private Dogstagram dogstagram;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", updatable = false)
    @TimeZoneColumn
    @CreatedDate
    private LocalDateTime createdAt;
}