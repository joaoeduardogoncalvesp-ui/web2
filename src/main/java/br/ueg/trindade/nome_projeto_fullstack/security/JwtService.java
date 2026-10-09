package br.ueg.trindade.nome_projeto_fullstack.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {
    private final String secret;
    private final ObjectMapper json;
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    public JwtService(@Value("${app.jwt.secret:}") String secret, ObjectMapper json) {
        this.secret = secret; this.json = json;
    }
    @PostConstruct void verificarSegredo() {
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32)
            throw new IllegalStateException("Defina JWT_SECRET com no mínimo 32 bytes aleatórios");
    }
    private byte[] assinatura(String input) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
    }
    public String criar(String email) {
        try {
            String header = ENCODER.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
            String payload = ENCODER.encodeToString(json.writeValueAsBytes(java.util.Map.of("sub", email, "exp", Instant.now().plusSeconds(7200).getEpochSecond())));
            String input = header + "." + payload;
            return input + "." + ENCODER.encodeToString(assinatura(input));
        } catch (Exception ex) { throw new IllegalStateException("Falha ao criar token", ex); }
    }
    public String validar(String token) {
        try {
            String[] parts = token.split("\\.", -1);
            if (parts.length != 3 || !parts[0].equals(ENCODER.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8)))) return null;
            byte[] expected = assinatura(parts[0] + "." + parts[1]);
            if (!MessageDigest.isEqual(expected, Base64.getUrlDecoder().decode(parts[2]))) return null;
            JsonNode payload = json.readTree(Base64.getUrlDecoder().decode(parts[1]));
            if (!payload.hasNonNull("sub") || !payload.has("exp") || payload.get("exp").asLong() <= Instant.now().getEpochSecond()) return null;
            return payload.get("sub").asText();
        } catch (Exception ex) { return null; }
    }
}
