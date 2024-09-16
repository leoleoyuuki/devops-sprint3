package br.com.fiap.challenge3.repository;

import br.com.fiap.challenge3.model.ComportamentoNegocios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComportamentoNegociosRepository extends JpaRepository<ComportamentoNegocios, Long> {
    List<ComportamentoNegocios> findByEmpresaId(Long empresaId);
}

