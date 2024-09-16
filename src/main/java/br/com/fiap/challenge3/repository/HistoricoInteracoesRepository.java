package br.com.fiap.challenge3.repository;

import br.com.fiap.challenge3.model.HistoricoInteracoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoInteracoesRepository extends JpaRepository<HistoricoInteracoes, Long> {
    List<HistoricoInteracoes> findByEmpresaId(Long empresaId);
}
