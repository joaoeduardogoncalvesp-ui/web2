package br.ueg.trindade.nome_projeto_fullstack.controller;
import java.util.List;
import br.ueg.trindade.nome_projeto_fullstack.model.Grupo;
import br.ueg.trindade.nome_projeto_fullstack.api.Dto;
import br.ueg.trindade.nome_projeto_fullstack.service.GrupoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RestController @RequestMapping("/api/grupos")
public class GrupoController {
    private final GrupoService service;
    public GrupoController(GrupoService service) { this.service = service; }
    @GetMapping public List<Grupo> listar() { return service.listar(); }
    @GetMapping("/{id}") public Grupo obter(@PathVariable Long id) { return service.obter(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Grupo criar(@Valid @RequestBody Dto.GrupoDados dados) { return service.criar(dados); }
    @PutMapping("/{id}") public Grupo editar(@PathVariable Long id, @Valid @RequestBody Dto.GrupoDados dados) { return service.editar(id, dados); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
