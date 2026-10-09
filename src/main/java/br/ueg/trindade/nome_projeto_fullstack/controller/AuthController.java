package br.ueg.trindade.nome_projeto_fullstack.controller;
import br.ueg.trindade.nome_projeto_fullstack.api.Dto;
import br.ueg.trindade.nome_projeto_fullstack.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final AuthService service;
 public AuthController(AuthService service) { this.service=service; }
 @PostMapping("/registro") @ResponseStatus(HttpStatus.CREATED)
 public Dto.TokenResposta registrar(@Valid @RequestBody Dto.NovoUsuario dados) { return service.registrar(dados); }
 @PostMapping("/login") public Dto.TokenResposta login(@Valid @RequestBody Dto.Credenciais dados) { return service.login(dados); }
}
