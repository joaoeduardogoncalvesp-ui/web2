package br.ueg.trindade.nome_projeto_fullstack.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:persistenciatest;DB_CLOSE_DELAY=-1",
    "app.jwt.secret=0123456789abcdef0123456789abcdef",
    "app.admin.email=admin@example.com",
    "app.admin.password=senha-admin-segura"
})
class PersistenciaApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    @Test void salvaEConsultaUsuarioPermissaoECategoriaComH2() throws Exception {
        String login = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@example.com\",\"senha\":\"senha-admin-segura\"}"))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String auth = "Bearer " + json.readTree(login).get("token").asText();

        String usuario = mvc.perform(post("/api/usuarios").header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Ana\",\"username\":\"ana\",\"email\":\"ana@example.com\",\"senha\":\"senha-segura-123\"}"))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.email").value("ana@example.com"))
            .andExpect(jsonPath("$.senha").doesNotExist()).andReturn().getResponse().getContentAsString();
        long usuarioId = json.readTree(usuario).get("id").asLong();
        mvc.perform(post("/api/usuarios").header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Outra\",\"username\":\"outra\",\"email\":\"ANA@example.com\",\"senha\":\"senha-segura-123\"}"))
            .andExpect(status().isConflict());
        mvc.perform(post("/api/usuarios").header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Outra\",\"username\":\"ANA\",\"email\":\"outra@example.com\",\"senha\":\"senha-segura-123\"}"))
            .andExpect(status().isConflict());

        mvc.perform(get("/api/usuarios").header("Authorization", auth))
            .andExpect(status().isOk()).andExpect(jsonPath("$[?(@.email == 'ana@example.com')]").exists());

        mvc.perform(put("/api/usuarios/{id}", usuarioId).header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Ana Maria\",\"username\":\"anamaria\",\"email\":\"ana@example.com\",\"papel\":\"USER\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.username").value("anamaria"));

        String permissao = mvc.perform(post("/permissoes").header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"LER_RELATORIOS\",\"descricao\":\"Consultar relatórios\"}"))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long permissaoId = json.readTree(permissao).get("id").asLong();
        mvc.perform(get("/permissoes/{id}", permissaoId).header("Authorization", auth))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("LER_RELATORIOS"));

        mvc.perform(put("/permissoes/{id}", permissaoId).header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"EDITAR_RELATORIOS\",\"descricao\":\"Editar relatórios\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("EDITAR_RELATORIOS"));

        String categoria = mvc.perform(post("/api/categorias").header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Ferramentas\",\"descricao\":\"Ferramentas diversas\"}"))
            .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long categoriaId = json.readTree(categoria).get("id").asLong();
        mvc.perform(get("/api/categorias/{id}", categoriaId).header("Authorization", auth))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Ferramentas"));
        mvc.perform(put("/api/categorias/{id}", categoriaId).header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Ferragens\",\"descricao\":\"Ferragens diversas\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Ferragens"));
        mvc.perform(post("/api/categorias").header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"ferragens\",\"descricao\":\"Duplicada\"}"))
            .andExpect(status().isConflict());
        mvc.perform(put("/api/categorias/{id}", categoriaId).header("Authorization", auth).contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"FERRAGENS\",\"descricao\":\"Mesmo registro\"}"))
            .andExpect(status().isOk());
        mvc.perform(delete("/api/usuarios/{id}", usuarioId).header("Authorization", auth)).andExpect(status().isNoContent());
        mvc.perform(delete("/permissoes/{id}", permissaoId).header("Authorization", auth)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/categorias/{id}", categoriaId).header("Authorization", auth)).andExpect(status().isNoContent());
        mvc.perform(get("/api/usuarios/{id}", usuarioId).header("Authorization", auth)).andExpect(status().isNotFound());
        mvc.perform(get("/permissoes/{id}", permissaoId).header("Authorization", auth)).andExpect(status().isNotFound());
        mvc.perform(get("/api/categorias/{id}", categoriaId).header("Authorization", auth)).andExpect(status().isNotFound());
    }
}
