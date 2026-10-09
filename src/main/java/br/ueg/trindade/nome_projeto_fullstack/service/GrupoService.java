package br.ueg.trindade.nome_projeto_fullstack.service;

import br.ueg.trindade.nome_projeto_fullstack.api.Dto;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.ueg.trindade.nome_projeto_fullstack.model.Grupo;
import br.ueg.trindade.nome_projeto_fullstack.repository.GrupoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class GrupoService {
    private final GrupoRepository grupos;
    public GrupoService(GrupoRepository grupos) { this.grupos = grupos; }
    public List<Grupo> listar() { return grupos.findAll(); }
    public Grupo obter(Long id) { return buscar(id); }
    
    public Grupo criar(Dto.GrupoDados d) { return grupos.save(new Grupo(d.nome().trim(), d.descricao())); }
     public Grupo editar(Long id, Dto.GrupoDados d) {
        Grupo g = buscar(id); g.nome = d.nome().trim(); g.descricao = d.descricao(); return grupos.save(g);
    }
    
    public void excluir(Long id) { grupos.delete(buscar(id)); }
    private Grupo buscar(Long id) { return grupos.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo não encontrado")); }
}
