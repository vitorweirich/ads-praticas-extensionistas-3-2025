package com.doacoes.api.repository;

import com.doacoes.api.model.Campanha;
import com.doacoes.api.model.Campanha.StatusCampanha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampanhaRepository extends JpaRepository<Campanha, Long> {
    List<Campanha> findByStatus(Campanha.StatusCampanha status);
    List<Campanha> findByCategoria(String categoria);
    List<Campanha> findByAdministradorId(Long administradorId);
	long countByStatus(StatusCampanha ativa);
}
