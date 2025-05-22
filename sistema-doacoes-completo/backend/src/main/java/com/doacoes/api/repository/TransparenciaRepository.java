package com.doacoes.api.repository;

import com.doacoes.api.model.Transparencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransparenciaRepository extends JpaRepository<Transparencia, Long> {
    List<Transparencia> findByCampanhaId(Long campanhaId);
    List<Transparencia> findByResponsavelId(Long responsavelId);
}
