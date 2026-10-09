package br.ueg.trindade.nome_projeto_fullstack.security;

import java.util.Locale;
import br.ueg.trindade.nome_projeto_fullstack.model.Usuario;
import br.ueg.trindade.nome_projeto_fullstack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrap {
    @Bean CommandLineRunner criarAdmin(UsuarioRepository repo, PasswordEncoder encoder,
            @Value("${app.admin.email:}") String email, @Value("${app.admin.password:}") String senha) {
        return args -> {
            if (!email.isBlank() && senha.length() >= 8) {
                String normalized = email.trim().toLowerCase(Locale.ROOT);
                if (!repo.existsByEmail(normalized)) repo.save(new Usuario("Administrador", normalized, normalized, encoder.encode(senha), Usuario.Papel.ADMIN));
            }
        };
    }
}
