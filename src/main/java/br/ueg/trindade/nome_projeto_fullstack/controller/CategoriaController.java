package br.ueg.trindade.nome_projeto_fullstack.controller;

import br.ueg.trindade.nome_projeto_fullstack.api.Dto;

import java.util.List;
import br.ueg.trindade.nome_projeto_fullstack.model.Categoria;
import br.ueg.trindade.nome_projeto_fullstack.service.CategoriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    public record Dados(@NotBlank String nome, String descricao) {}
    private final CategoriaService service;
    public CategoriaController(CategoriaService service) { this.service = service; }

    @GetMapping public List<Categoria> listar() { return service.listar(); }
    @GetMapping("/{id}") public Categoria obter(@PathVariable Long id) { return service.buscar(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Categoria criar(@Valid @RequestBody Dados dados) { return service.criar(dados.nome(), dados.descricao()); }
    @PutMapping("/{id}")
    public Categoria editar(@PathVariable Long id, @Valid @RequestBody Dados dados) {
        return service.editar(id, dados.nome(), dados.descricao());
    }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
