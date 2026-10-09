package br.ueg.trindade.nome_projeto_fullstack.security;

import java.io.IOException;
import java.util.List;
import br.ueg.trindade.nome_projeto_fullstack.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class SecurityConfig {
    @Bean CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean SecurityFilterChain chain(HttpSecurity http, JwtService jwt, UsuarioRepository usuarios) throws Exception {
        OncePerRequestFilter tokenFilter = new OncePerRequestFilter() {
            @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
                String header = req.getHeader("Authorization");
                if (header != null && header.startsWith("Bearer ")) {
                    String email = jwt.validar(header.substring(7));
                    if (email != null) usuarios.findByEmail(email).ifPresent(u -> {
                        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + u.papel.name()));
                        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u.email, null, authorities));
                    });
                }
                chain.doFilter(req, res);
            }
        };
        return http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(h -> h.frameOptions(f -> f.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/api/status", "/h2-console/**").permitAll()
                .requestMatchers("/api/usuarios", "/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/permissoes", "/permissoes/**", "/api/permissoes", "/api/permissoes/**").authenticated()
                .requestMatchers("/permissoes", "/permissoes/**", "/api/permissoes", "/api/permissoes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/categorias", "/api/categorias/**").authenticated()
                .requestMatchers("/api/categorias", "/api/categorias/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/grupos", "/api/grupos/**").authenticated()
                .requestMatchers("/api/grupos", "/api/grupos/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
