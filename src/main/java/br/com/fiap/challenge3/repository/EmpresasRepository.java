package br.com.fiap.challenge3.repository;

import br.com.fiap.challenge3.model.Empresas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresasRepository extends JpaRepository<Empresas,Long> {
}
