package com.puppypaws.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.TimeZoneColumn;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "Attachment")
public class Attachment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_id_generator")
    @SequenceGenerator(name = "attachment_id_generator", sequenceName = "attachment_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "url2")
    private String url2;

    @Column(name = "url3")
    private String url3;

    @Column(name = "created_at", updatable = false)
    @TimeZoneColumn
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @TimeZoneColumn
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "attachment", fetch = FetchType.LAZY)
    private Dogstagram dogstagram;
}
