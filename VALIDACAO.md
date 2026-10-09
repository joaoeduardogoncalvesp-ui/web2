# Conferência do checklist — 09/10/2026

## Validado nesta revisão

- Java 21.0.12.1 e Spring Boot 3.5.6: compilação e empacotamento aprovados.
- Maven: 3 testes executados, 0 falhas, 0 erros (contexto Spring, token e integração API/H2).
- Vite + TypeScript: `npm run build` aprovado.
- Navegador Chromium, com React e Spring rodando juntos: cadastrar → listar → editar → excluir aprovado para Usuario, Permissao, Categoria e Grupo.
- Categoria duplicada: mensagem de conflito exibida no navegador.
- Email/username duplicados: HTTP 409 nos testes de integração.
- Senha ausente na resposta de cadastro: teste de integração aprovado.
- Registro/login de conta USER: aprovado; sem botões de gerenciamento e com HTTP 403 ao acessar usuários.
- Nenhum erro JavaScript capturado durante o fluxo do navegador.
- Testes de integração e navegador usaram H2 em memória para não deixar dados de teste no projeto. A configuração padrão continua usando H2 em arquivo.

## Estrutura conferida

- Maven configurado para Java 21; Web, Data JPA, H2, PostgreSQL, DevTools, Security e Validation presentes.
- Pacotes controller, service, repository e model; controllers delegam aos services.
- DTO e `@JsonIgnore` ocultam senha; BCrypt guarda seu hash.
- GET, POST, PUT e DELETE disponíveis em `/api`, com `@PathVariable` para IDs.
- Axios com baseURL e token; proxy Vite e `@CrossOrigin`/CORS do Security.
- Listas e itens via props, formulários controlados, páginas com lógica do CRUD e App apenas renderizando a página do sistema.
- Após salvar ou excluir, as páginas recarregam a lista.
- Categoria é a entidade própria, com CRUD e regra de nome único sem diferenciar maiúsculas/minúsculas.

## Pendências externas

- Criar/publicar o repositório na conta do aluno e compartilhar o link.
- Commits regulares dependem do histórico real de trabalho; não foram inventados commits anteriores.
- Conferir reprodução das Aulas 01–06 com os materiais do professor, não fornecidos nesta revisão.
- Perfil PostgreSQL configurado, mas não testado com servidor PostgreSQL (o checklist pede a integração com H2, que foi testada).

## Alterações principais

Java 17 → 21; inclusão de DevTools; extração de UsuarioService, GrupoService e AuthService; controllers no pacote controller; telas migradas para TypeScript; separação de páginas, listas e itens; correção de escape inválido no JwtService original; tratamento de erros; configuração dos testes sem segredo externo; README refeito com execução Windows/Linux e entrega GitHub.
