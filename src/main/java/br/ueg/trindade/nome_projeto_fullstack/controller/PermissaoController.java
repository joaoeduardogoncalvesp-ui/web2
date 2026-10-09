package br.ueg.trindade.nome_projeto_fullstack.controller;

import br.ueg.trindade.nome_projeto_fullstack.api.Dto;

import java.util.List;
import br.ueg.trindade.nome_projeto_fullstack.model.Permissao;
import br.ueg.trindade.nome_projeto_fullstack.service.PermissaoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RestController
@RequestMapping({"/permissoes", "/api/permissoes"})
public class PermissaoController {
    public record Dados(@NotBlank String nome, String descricao) {}
    private final PermissaoService service;
    public PermissaoController(PermissaoService service) { this.service = service; }

    @GetMapping public List<Permissao> listar() { return service.listar(); }
    @GetMapping("/{id}") public Permissao obter(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Permissao criar(@Valid @RequestBody Dados dados) { return service.criar(dados.nome(), dados.descricao()); }
    @PutMapping("/{id}")
    public Permissao editar(@PathVariable Long id, @Valid @RequestBody Dados dados) {
        return service.editar(id, dados.nome(), dados.descricao());
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
