package br.com.fiap.challenge3.repository;

import br.com.fiap.challenge3.model.TendenciasGastos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TendenciasGastosRepository extends JpaRepository<TendenciasGastos, Long> {
    List<TendenciasGastos> findByEmpresaId(Long idEmpresa);
}


