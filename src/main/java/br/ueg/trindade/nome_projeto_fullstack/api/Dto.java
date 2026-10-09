package br.ueg.trindade.nome_projeto_fullstack.api;

import br.ueg.trindade.nome_projeto_fullstack.model.Usuario;
import jakarta.validation.constraints.*;

public class Dto {
    public record Credenciais(@NotBlank @Email String email, @NotBlank String senha) {}
    public record NovoUsuario(@NotBlank String nome, @NotBlank String username, @NotBlank @Email String email, @NotBlank @Size(min=8) String senha) {}
    public record EdicaoUsuario(@NotBlank String nome, @NotBlank String username, @NotBlank @Email String email, String senha, @NotNull Usuario.Papel papel) {}
    public record UsuarioResposta(Long id, String nome, String username, String email, Usuario.Papel papel) {
        public static UsuarioResposta de(Usuario u) { return new UsuarioResposta(u.id, u.nome, u.username, u.email, u.papel); }
    }
    public record GrupoDados(@NotBlank String nome, String descricao) {}
    public record TokenResposta(String token, UsuarioResposta usuario) {}
}
