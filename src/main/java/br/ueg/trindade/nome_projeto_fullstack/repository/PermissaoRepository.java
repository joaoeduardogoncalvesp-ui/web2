package br.ueg.trindade.nome_projeto_fullstack.repository;

import br.ueg.trindade.nome_projeto_fullstack.model.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {}
