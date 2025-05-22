package com.doacoes.api.repository;

import com.doacoes.api.model.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
    List<Doacao> findByCampanhaId(Long campanhaId);
    List<Doacao> findByDoadorId(Long doadorId);
    List<Doacao> findByStatus(Doacao.StatusDoacao status);
}
