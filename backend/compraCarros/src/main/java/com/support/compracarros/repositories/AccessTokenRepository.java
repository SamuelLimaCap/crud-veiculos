package com.support.compracarros.repositories;

import com.support.compracarros.entities.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByToken(String token);

    List<AccessToken> findAllByUser_id(Long userId);
}
