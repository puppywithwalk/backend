package com.puppypaws.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "Dogstagram")
public class Dogstagram extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dogstagram_id_generator")
    @SequenceGenerator(name="dogstagram_id_generator", sequenceName="dogstagram_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToOne(fetch =  FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "attechmentId")
    private Attachment attachment;
}