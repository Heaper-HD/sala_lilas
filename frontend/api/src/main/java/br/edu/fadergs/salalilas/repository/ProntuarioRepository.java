package br.edu.fadergs.salalilas.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.fadergs.salalilas.model.Prontuario;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, UUID> {

}
