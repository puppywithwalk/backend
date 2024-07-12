package com.puppypaws.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Attachment")
public class Attachment extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment_id_generator")
    @SequenceGenerator(name="attachment_id_generator", sequenceName="attachment_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "dogstagram_id", nullable = false)
    private Long dogstagramId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "url2")
    private String url2;

    @Column(name = "url3")
    private String url3;
}

