package com.doacoes.api.repository;

import com.doacoes.api.model.MensagemContato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemContatoRepository extends JpaRepository<MensagemContato, Long> {
    List<MensagemContato> findByEmail(String email);
}
