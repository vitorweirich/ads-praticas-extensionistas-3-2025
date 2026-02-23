package com.doacoes.api.repository;

import com.doacoes.api.model.Doacao;
import com.doacoes.api.model.Doacao.StatusDoacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
    List<Doacao> findByCampanhaId(Long campanhaId);
    List<Doacao> findByDoadorId(Long doadorId);
    List<Doacao> findByStatus(Doacao.StatusDoacao status);
    @Query("SELECT SUM(d.valor) FROM Doacao d WHERE d.status = :status")
    BigDecimal sumValorByStatus(StatusDoacao status);
	void deleteByCampanhaId(Long id);

}
