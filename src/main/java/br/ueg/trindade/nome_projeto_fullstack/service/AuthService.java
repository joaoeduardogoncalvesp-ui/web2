package br.ueg.trindade.nome_projeto_fullstack.service;
import br.ueg.trindade.nome_projeto_fullstack.api.Dto;
import br.ueg.trindade.nome_projeto_fullstack.repository.UsuarioRepository;
import br.ueg.trindade.nome_projeto_fullstack.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Locale;
@Service
public class AuthService {
 private final UsuarioService service; private final UsuarioRepository usuarios; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthService(UsuarioService service, UsuarioRepository usuarios, PasswordEncoder encoder, JwtService jwt) { this.service=service; this.usuarios=usuarios; this.encoder=encoder; this.jwt=jwt; }
 public Dto.TokenResposta registrar(Dto.NovoUsuario dados) { var usuario=service.criar(dados); return new Dto.TokenResposta(jwt.criar(usuario.email()), usuario); }
 public Dto.TokenResposta login(Dto.Credenciais dados) {
  var usuario=usuarios.findByEmail(dados.email().trim().toLowerCase(Locale.ROOT)).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciais inválidas"));
  if (!encoder.matches(dados.senha(),usuario.senha)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciais inválidas");
  return new Dto.TokenResposta(jwt.criar(usuario.email),Dto.UsuarioResposta.de(usuario));
 }
}
