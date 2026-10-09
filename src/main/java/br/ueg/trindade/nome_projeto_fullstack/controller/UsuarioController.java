package br.ueg.trindade.nome_projeto_fullstack.controller;
import java.util.List;
import br.ueg.trindade.nome_projeto_fullstack.api.Dto;
import br.ueg.trindade.nome_projeto_fullstack.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RestController @RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) { this.service = service; }
    @GetMapping public List<Dto.UsuarioResposta> listar() { return service.listar(); }
    @GetMapping("/{id}") public Dto.UsuarioResposta obter(@PathVariable Long id) { return service.obter(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Dto.UsuarioResposta criar(@Valid @RequestBody Dto.NovoUsuario dados) { return service.criar(dados); }
    @PutMapping("/{id}") public Dto.UsuarioResposta editar(@PathVariable Long id, @Valid @RequestBody Dto.EdicaoUsuario dados) { return service.editar(id, dados); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) { service.excluir(id); }
}
