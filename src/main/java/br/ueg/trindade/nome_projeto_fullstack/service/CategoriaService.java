package br.ueg.trindade.nome_projeto_fullstack.service;

import java.util.List;
import br.ueg.trindade.nome_projeto_fullstack.model.Categoria;
import br.ueg.trindade.nome_projeto_fullstack.repository.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CategoriaService {
    private final CategoriaRepository repositorio;
    public CategoriaService(CategoriaRepository repositorio) { this.repositorio = repositorio; }

    @Transactional(readOnly = true)
    public List<Categoria> listar() { return repositorio.findAll(); }

    @Transactional(readOnly = true)
    public Categoria buscar(Long id) {
        return repositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    public Categoria criar(String nome, String descricao) {
        String nomeLimpo = nome.trim();
        verificarNomeDisponivel(nomeLimpo, null);
        return repositorio.save(new Categoria(nomeLimpo, descricao));
    }

    public Categoria editar(Long id, String nome, String descricao) {
        Categoria categoria = buscar(id);
        String nomeLimpo = nome.trim();
        verificarNomeDisponivel(nomeLimpo, id);
        categoria.nome = nomeLimpo;
        categoria.descricao = descricao;
        return repositorio.save(categoria);
    }

    public void excluir(Long id) { repositorio.delete(buscar(id)); }

    private void verificarNomeDisponivel(String nome, Long idAtual) {
        repositorio.findFirstByNomeIgnoreCase(nome).ifPresent(existente -> {
            if (!existente.id.equals(idAtual))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma categoria com esse nome");
        });
    }
}
