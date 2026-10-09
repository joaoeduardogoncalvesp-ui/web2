# Desenvolvimento Web II — Encontro 07

Projeto Spring Boot + React/Vite/TypeScript integrado ao H2. A entidade própria é **Categoria** (nome e descrição), com regra que impede nomes duplicados sem diferenciar maiúsculas e minúsculas. O cadastro de usuário impede email e username duplicados. Grupos e autenticação do projeto enviado foram preservados.

## Requisitos

- JDK **21** (`java -version` deve indicar 21).
- Node.js 20.19+ ou 22.12+ e npm.
- Internet na primeira instalação para baixar dependências Maven/npm.

## Executar no Windows (PowerShell)

Extraia o ZIP e abra um terminal na pasta que contém `pom.xml`. Configure um segredo e as credenciais do administrador local (não salve valores reais no Git):

```powershell
$env:JWT_SECRET = [guid]::NewGuid().ToString() + [guid]::NewGuid().ToString()
$env:ADMIN_EMAIL = "admin@example.com"
$env:ADMIN_PASSWORD = "TroqueEstaSenha123!"
.\mvnw.cmd spring-boot:run
```

No segundo terminal:

```powershell
cd src/main/frontend
npm ci
npm run dev
```

Abra **http://localhost:5173** e entre com o administrador configurado. Apenas ADMIN pode cadastrar, editar e excluir nas telas de gerenciamento; contas criadas pelo formulário público recebem USER e podem consultar permissões, categorias e grupos. O CRUD de Permissao é um cadastro didático; a autorização usa o campo `papel` do Usuario.

## Executar no Linux/macOS

```bash
export JWT_SECRET="$(openssl rand -hex 32)"
export ADMIN_EMAIL="admin@example.com"
export ADMIN_PASSWORD="TroqueEstaSenha123!"
chmod +x mvnw
./mvnw spring-boot:run
```

Em outro terminal, execute os mesmos comandos npm acima. Mantenha os dois terminais abertos. Se a porta 5173 estiver ocupada, encerre a outra instância.

## Arquitetura

- Java: `br.ueg.trindade.nome_projeto_fullstack`.
- `controller`: endpoints REST `/api`, validação de entrada e chamada somente aos services.
- `service`: operações, transações e regras de negócio.
- `repository`: `JpaRepository` por entidade.
- `model`: Usuario, Permissao, Categoria e Grupo, com `@Entity`, `@Id` e `@GeneratedValue`.
- `api/Dto.java`: DTOs de entrada e saída; senha ausente nas respostas. A entidade também usa `@JsonIgnore`, e senhas são armazenadas com BCrypt.
- `security`: Security, tokens e inicialização do administrador.
- React: `main.tsx` inicializa; `App.tsx` apenas renderiza `SistemaPage`.
- `pages`: lógica de sessão e CRUD em `UsuariosPage`, `PermissoesPage`, `CategoriasPage` e `GruposPage`.
- `components`: listas e itens via props; formulários controlados com `useState` e `useEffect`.
- `services/api.ts`: Axios, `baseURL: '/api'` e token de sessão.
- Vite encaminha `/api` ao Spring em localhost:8080; controllers têm `@CrossOrigin`, em conjunto com CORS do Security.

## Endpoints

| Entidade | Coleção | Registro |
| --- | --- | --- |
| Usuario | `/api/usuarios` | `/api/usuarios/{id}` |
| Permissao | `/api/permissoes` | `/api/permissoes/{id}` |
| Categoria | `/api/categorias` | `/api/categorias/{id}` |
| Grupo | `/api/grupos` | `/api/grupos/{id}` |

GET lista/consulta; POST cria; PUT edita; DELETE exclui usando `@PathVariable`. Registro/login em `/api/auth/registro` e `/api/auth/login`. O alias `/permissoes` foi mantido para compatibilidade.

## Banco e testes

H2 persistente em `data/`, fora do Git. Console: **http://localhost:8080/h2-console**, JDBC `jdbc:h2:file:./data/projeto_fullstack;DB_CLOSE_ON_EXIT=FALSE`, usuário `sa`, senha vazia. O console está habilitado para desenvolvimento local. Há também driver e perfil PostgreSQL em `application-postgres.properties`.

```powershell
.\mvnw.cmd test
cd src/main/frontend
npm run build
```

Linux/macOS: use `./mvnw test`. Os testes usam H2 em memória e segredo de teste. Não alteram os dados locais.

No navegador, como ADMIN, abra cada tela e faça cadastrar → listar → editar → excluir. Teste também uma categoria repetida e um email/username repetido. Confirme as alterações no H2 com `SELECT * FROM USUARIOS`, `SELECT * FROM PERMISSOES` e `SELECT * FROM CATEGORIAS`.

## Entrega no GitHub

O ZIP não cria nem publica um repositório na sua conta. Crie um repositório vazio no GitHub e, na raiz do projeto, execute (substitua a URL):

```bash
git init
git add .
git commit -m "Adequa projeto ao checklist do Encontro 07"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git
git push -u origin main
```

Compartilhe o link com o professor. Preserve commits reais de cada próxima alteração com `git add .`, `git commit -m "Descrição da alteração"` e `git push`. Este arquivo não comprova commits regulares anteriores nem o compartilhamento do link.

A reprodução exata das Aulas 01–06 depende de conferir os materiais do professor, que não foram fornecidos. A adequação foi feita com base no ZIP e no checklist enviado.

Ao publicar, o workflow `.github/workflows/validacao.yml` executa os testes Java e o build do front-end a cada push/PR. Consulte `VALIDACAO.md` para os resultados desta revisão.
