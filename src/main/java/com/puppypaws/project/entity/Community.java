package com.puppypaws.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Community")
public class Community extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "community_id_generator")
    @SequenceGenerator(name="community_id_generator", sequenceName="community_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "pickup_location", nullable = false, length = 30)
    private String pickupLocation;

    @Column(name = "pickup_date", nullable = false)
    private Date pickupDate;

    @Column(name = "status", length = 1)
    @ColumnDefault("'N'")
    private String status;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;
}
