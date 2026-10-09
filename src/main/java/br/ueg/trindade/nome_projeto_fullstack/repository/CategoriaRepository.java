package br.ueg.trindade.nome_projeto_fullstack.repository;

import br.ueg.trindade.nome_projeto_fullstack.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findFirstByNomeIgnoreCase(String nome);
}
