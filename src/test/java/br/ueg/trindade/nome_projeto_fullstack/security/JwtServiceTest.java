package br.ueg.trindade.nome_projeto_fullstack.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    @Test void aceitaTokenAssinadoERejeitaAlteracao() {
        JwtService jwt = new JwtService("0123456789abcdef0123456789abcdef", new ObjectMapper());
        String token = jwt.criar("pessoa@example.com");
        assertEquals("pessoa@example.com", jwt.validar(token));
        String[] partes = token.split("\\.");
        String adulterado = partes[0] + "." + partes[1] + "." + (partes[2].charAt(0) == 'A' ? 'B' : 'A') + partes[2].substring(1);
        assertNull(jwt.validar(adulterado));
    }
}
