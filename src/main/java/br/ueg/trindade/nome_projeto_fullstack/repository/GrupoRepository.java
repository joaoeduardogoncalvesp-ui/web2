package br.ueg.trindade.nome_projeto_fullstack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ueg.trindade.nome_projeto_fullstack.model.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {}
