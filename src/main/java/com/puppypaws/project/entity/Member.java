package com.puppypaws.project.entity;


import jakarta.persistence.*;

import lombok.*;

@Entity
@Data
@Table(name = "Member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity{

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

    public Member(String nickname, String email, String provider) {
        this.nickname = nickname;
        this.email = email;
        this.provider = provider;
    }
}