package com.doacoes.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doacoes.api.model.SessionTransferTokenEntity;

@Repository
public interface SessionTransferTokenRepository extends JpaRepository<SessionTransferTokenEntity, UUID> {
}

