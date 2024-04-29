package com.puppypaws.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Data
@Table(name = "Token")
public class Token {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_id_generator")
    @SequenceGenerator(name="token_id_generator", sequenceName="token_id", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "access_token", length = 2048)
    private String accessToken;

    @Column(name = "refresh_token", length = 2048)
    private String refreshToken;
}
