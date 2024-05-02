package com.puppypaws.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.TimeZoneColumn;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Community")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_id_generator")
    @SequenceGenerator(name="community_id_generator", sequenceName="community_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "pickup_location", nullable = false, length = 30)
    private String pickupLocation;

    @Column(name = "pickup_date", nullable = false)
    private Date pickupDate;

    @Column(name = "status", nullable = false, length = 1)
    @ColumnDefault("'N'")
    private String status;

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
