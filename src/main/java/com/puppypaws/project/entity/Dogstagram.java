package com.puppypaws.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@EqualsAndHashCode(callSuper = false)
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

    @OneToMany(mappedBy = "dogstagram" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DogstagramLike> dogstagramLikes = new ArrayList<>();

    @OneToOne(fetch =  FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;
}