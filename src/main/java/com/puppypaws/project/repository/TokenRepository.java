package com.puppypaws.project.repository;


import com.puppypaws.project.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token  findByAccessToken(String accessToken);

    @Modifying
    @Transactional
    @Query("update Token t set t.accessToken = ?1 where t.refreshToken = ?2")
    void updateToken(String newAccessToken, String refreshToken);

    default void saveToken(String accessToken, String refreshToken) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        save(token);
    }
}
