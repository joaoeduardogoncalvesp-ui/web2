package br.ueg.trindade.nome_projeto_fullstack.service;

import br.ueg.trindade.nome_projeto_fullstack.api.Dto;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;
import br.ueg.trindade.nome_projeto_fullstack.model.Usuario;
import br.ueg.trindade.nome_projeto_fullstack.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    public UsuarioService(UsuarioRepository usuarios, PasswordEncoder encoder) { this.usuarios = usuarios; this.encoder = encoder; }
    public List<Dto.UsuarioResposta> listar() { return usuarios.findAll().stream().map(Dto.UsuarioResposta::de).toList(); }
    public Dto.UsuarioResposta obter(Long id) { return Dto.UsuarioResposta.de(buscar(id)); }
    
    public Dto.UsuarioResposta criar(Dto.NovoUsuario d) {
        String email = d.email().trim().toLowerCase(Locale.ROOT);
        if (usuarios.existsByEmail(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        String username = d.username().trim().toLowerCase(Locale.ROOT);
        if (usuarios.existsByUsername(username)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já cadastrado");
        return Dto.UsuarioResposta.de(usuarios.save(new Usuario(d.nome().trim(), username, email, encoder.encode(d.senha()), Usuario.Papel.USER)));
    }
    
    public Dto.UsuarioResposta editar(Long id, Dto.EdicaoUsuario d) {
        Usuario u = buscar(id);
        String email = d.email().trim().toLowerCase(Locale.ROOT);
        if (!email.equals(u.email) && usuarios.existsByEmail(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado");
        String username = d.username().trim().toLowerCase(Locale.ROOT);
        if (!username.equals(u.username) && usuarios.existsByUsername(username)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username já cadastrado");
        u.nome = d.nome().trim(); u.username = username; u.email = email; u.papel = d.papel();
        if (d.senha() != null && !d.senha().isBlank()) {
            if (d.senha().length() < 8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha deve ter pelo menos 8 caracteres");
            u.senha = encoder.encode(d.senha());
        }
        return Dto.UsuarioResposta.de(usuarios.save(u));
    }
    
    public void excluir(Long id) { usuarios.delete(buscar(id)); }
    private Usuario buscar(Long id) { return usuarios.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")); }
}
