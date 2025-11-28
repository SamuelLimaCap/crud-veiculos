package com.support.compracarros.repositories;

import com.support.compracarros.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findById(UUID uuid);
    Optional<RefreshToken> findByUser_Id(Long userId);

}
