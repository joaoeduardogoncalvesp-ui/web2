package br.ueg.trindade.nome_projeto_fullstack.service;

import java.util.List;
import br.ueg.trindade.nome_projeto_fullstack.model.Permissao;
import br.ueg.trindade.nome_projeto_fullstack.repository.PermissaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PermissaoService {
    private final PermissaoRepository repositorio;
    public PermissaoService(PermissaoRepository repositorio) { this.repositorio = repositorio; }

    @Transactional(readOnly = true)
    public List<Permissao> listar() { return repositorio.findAll(); }

    @Transactional(readOnly = true)
    public Permissao buscar(Long id) {
        return repositorio.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permissão não encontrada"));
    }

    public Permissao criar(String nome, String descricao) {
        return repositorio.save(new Permissao(nome.trim(), descricao));
    }

    public Permissao editar(Long id, String nome, String descricao) {
        Permissao permissao = buscar(id);
        permissao.nome = nome.trim();
        permissao.descricao = descricao;
        return repositorio.save(permissao);
    }

    public void excluir(Long id) { repositorio.delete(buscar(id)); }
}
