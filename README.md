# Spring Boot + React: usuários, permissões e categorias

API REST com Spring Data JPA, H2, Spring Security e JWT. O frontend React está em `src/main/frontend`.

## Executar

Requisitos: Java 17, Node.js 20.19+ e acesso aos repositórios Maven e npm na primeira instalação. O H2 é incorporado: não é necessário instalar um servidor de banco.

Na raiz do projeto, configure `JWT_SECRET` com pelo menos 32 bytes aleatórios (por exemplo, gere com `openssl rand -base64 48`). Configure também `ADMIN_EMAIL` e `ADMIN_PASSWORD` (senha com pelo menos 8 caracteres) para criar o primeiro administrador. Em Linux/macOS:

```bash
export JWT_SECRET="$(openssl rand -base64 48)"
export ADMIN_EMAIL="admin@example.com"
export ADMIN_PASSWORD="troque-por-uma-senha-forte"
./mvnw spring-boot:run
```

O banco H2 persiste em `./data/projeto_fullstack.mv.db` a partir da pasta de execução. A aplicação usa a porta 8080. As tabelas são criadas/atualizadas pelo Hibernate. Não publique o arquivo do banco nem as credenciais.

Em outro terminal, execute `cd src/main/frontend`, `npm install` e `npm run dev`. Abra `http://localhost:5173`.

O PostgreSQL da atividade anterior continua disponível: crie o banco e inicie o backend com `SPRING_PROFILES_ACTIVE=postgres`, `DB_URL`, `DB_USER` e `DB_PASSWORD` configurados.

## Rotas

| Método | Rota | Permissão |
| --- | --- | --- |
| POST | `/api/auth/registro`, `/api/auth/login` | Pública |
| GET, POST | `/api/usuarios` | ADMIN |
| GET, PUT, DELETE | `/api/usuarios/{id}` | ADMIN |
| GET | `/permissoes`, `/permissoes/{id}` | Autenticado |
| POST | `/permissoes` | ADMIN |
| PUT, DELETE | `/permissoes/{id}` | ADMIN |
| GET | `/api/categorias`, `/api/categorias/{id}` | Autenticado |
| POST | `/api/categorias` | ADMIN |
| PUT, DELETE | `/api/categorias/{id}` | ADMIN |
| GET, POST | `/api/grupos` | GET autenticado; POST ADMIN |
| GET, PUT, DELETE | `/api/grupos/{id}` | GET autenticado; demais ADMIN |

`/api/permissoes` é um endereço alternativo para `/permissoes`. A classe `Permissao` contém `id`, `nome` e `descricao`. A entidade extra escolhida foi `Categoria`, com `id`, `nome` e `descricao`. O `UsuarioRepository`, `PermissaoRepository` e `CategoriaRepository` herdam de `JpaRepository`; os controllers usam esses repositórios e não listas em memória.

## Testar com curl

Depois de iniciar o servidor com o administrador configurado:

```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@example.com","senha":"troque-por-uma-senha-forte"}'
```

Copie o campo `token` da resposta e configure `TOKEN` no terminal. Então teste:

```bash
curl -i http://localhost:8080/api/usuarios -H "Authorization: Bearer $TOKEN"
curl -i -X POST http://localhost:8080/api/usuarios -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"nome":"Ana","username":"ana","email":"ana@example.com","senha":"senha-segura-123"}'
curl -i -X POST http://localhost:8080/permissoes -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' -d '{"nome":"LER_RELATORIOS","descricao":"Consultar relatórios"}'
curl -i http://localhost:8080/permissoes/1 -H "Authorization: Bearer $TOKEN"
curl -i -X POST http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' -d '{"nome":"Ferramentas","descricao":"Produtos de ferramentas"}'
curl -i http://localhost:8080/api/categorias -H "Authorization: Bearer $TOKEN"
```

Para verificar a persistência, reinicie a aplicação e repita os GETs. O JWT expira em 2 horas; faça login novamente se necessário. Rode os testes automatizados com `./mvnw test`.

## Listagens React (Aula 04)

O frontend usa Axios (`src/services/api.ts`) com `baseURL: '/api'`. O Vite encaminha `/api` para `http://localhost:8080`. `UsuarioList.tsx` consulta `/usuarios`, `PermissaoList.tsx` consulta `/permissoes` e `CategoriaList.tsx` consulta `/categorias`. Há tipos TypeScript em `src/types/`, indicação de carregamento e mensagem quando uma requisição falha. A API também aceita requisições feitas diretamente do navegador em `http://localhost:5173` por meio da configuração global de CORS.

Para instalar as novas dependências do frontend, execute `npm install` em `src/main/frontend`. A lista de usuários só aparece para ADMIN, conforme as regras de autorização do backend.

## CRUD no navegador e H2 Console (Aula 05)

Entre como ADMIN e, na aba Usuários, cadastre alguém preenchendo nome, username, email e senha (mínimo de 8 caracteres). Use Editar e Excluir na linha cadastrada. Repita o fluxo nas abas Permissões e Categorias. As listas são consultadas novamente após salvar ou excluir; o ID de PUT/DELETE é enviado na URL.

Para conferir os dados diretamente no H2, abra `http://localhost:8080/h2-console` **no computador onde o backend está rodando**. Use JDBC URL `jdbc:h2:file:./data/projeto_fullstack;DB_CLOSE_ON_EXIT=FALSE`, usuário `sa` e senha vazia. Consulte `SELECT * FROM USUARIOS;`, `SELECT * FROM PERMISSOES;` e `SELECT * FROM CATEGORIAS;`. O console aceita acesso local e está habilitado para fins didáticos.

O campo `username` foi acrescentado nesta aula. Ele é obrigatório em novos cadastros e único; registros criados por versões anteriores podem permanecer com `username` vazio até serem editados.

## Serviços e regra de categoria (Aula 06)

`PermissaoController → PermissaoService → PermissaoRepository` e `CategoriaController → CategoriaService → CategoriaRepository`. A aba Permissões exibe `src/pages/PermissoesPage.tsx`, que usa a lista e o formulário existentes.

`CategoriaService` impede criar ou renomear uma categoria para um nome já usado, ignorando diferenças entre maiúsculas e minúsculas e espaços nas extremidades. Nesse caso, a API responde **409 Conflict** e a aba Categorias mostra uma mensagem específica. Para conferir no navegador: entre como ADMIN, crie `Ferragens` e tente criar `ferragens`; a segunda operação deve mostrar o conflito e a lista deve continuar com apenas um registro. Editar o próprio registro para `FERRAGENS` continua permitido. Há um teste automatizado que cobre esses casos (`./mvnw test`).
