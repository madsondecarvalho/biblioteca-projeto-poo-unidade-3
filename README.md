# Sistema de Biblioteca em Java

Sistema de gerenciamento de biblioteca desenvolvido em Java com SQLite.

## Funcionalidades

-  Login de usuários (Admin/Leitor)
-  Cadastro de livros e usuários
-  Empréstimo e devolução de livros
-  Cálculo automático de multas (R$ 0,35/dia de atraso)
-  Interface por linha de comando

## Tecnologias

- Java
- SQLite
- JDBC

## Dependências

O projeto utiliza o driver JDBC para SQLite. O arquivo `sqlite-jdbc-3.51.0.0.jar` está disponível na pasta `lib/` e deve estar no classpath para executar o projeto.

## Como executar

### Opção 1: Executar o JAR (Recomendado)

O arquivo executável compilado está disponível em `runnable_jar/biblioteca.jar`. Para executar:

```bash
java -jar runnable_jar/biblioteca.jar
```

**Nota importante**: Na primeira execução, o banco de dados SQLite será criado automaticamente como `biblioteca.db`. Este arquivo será gerado no mesmo diretório onde o comando for executado.

### Opção 2: Importar e executar no Eclipse

1. Abra o Eclipse IDE
2. Vá em `File` → `Import...`
3. Selecione `General` → `Projects from Folder or Archive`
4. Clique em `Directory...` e navegue até a pasta do projeto: `c:\Users\Madson Gustavo\eclipse-workspace\biblioteca-projeto-poo-unidade-3`
5. Clique em `Finish`
6. O projeto será importado para seu workspace
7. Clique com botão direito no projeto e selecione `Build Path` → `Configure Build Path`
8. Na aba `Libraries`, clique em `Add External Archives...` e adicione `lib/sqlite-jdbc-3.51.0.0.jar`
9. Clique em `Apply and Close`
10. Localize a classe `SistemaBiblioteca.java` em `bibliotecaProjetoPOO/`
11. Clique com botão direito e selecione `Run As` → `Java Application`

## Banco de Dados

### Criação e Migrations

O sistema cria automaticamente as tabelas do banco de dados na primeira execução:

- **Tabela Usuario**: Armazena administradores e leitores
- **Tabela Livro**: Catálogo de livros disponíveis
- **Tabela Emprestimo**: Registros de empréstimos e devoluções

### Seeds (Dados Iniciais)

O sistema popula automaticamente o banco com dados iniciais:

- **1 Administrador**: 
  - Username: `admin`
  - Senha: `admin`
  
- **5 Leitores**:
  - Ana Silva (ana / 123)
  - Bruno Costa (bruno / 123)
  - Carla Dias (carla / 123)
  - Diego Faria (diego / 123)
  - Elisa Mendes (elisa / 123)

- **15 Livros**: Incluindo clássicos da literatura brasileira e mundial

## Sistema de Multas por Atraso

### Regra de Cálculo

O sistema aplica uma multa de **R$ 0,35 (trinta e cinco centavos) por dia de atraso** na devolução de livros.

- Empréstimos têm prazo de **7 dias** para devolução
- Devoluções no prazo ou antecipadas: **sem multa**
- Após a data de vencimento: multa é calculada automaticamente

### Como testar empréstimos atrasados

Para testar o cálculo de multas, o sistema já inclui dois empréstimos com atraso nos dados iniciais:

1. **Ana Silva** (ana/123) emprestou "Dom Casmurro" com 3 dias de atraso
2. **Carla Dias** (carla/123) emprestou "Memórias Póstumas de Brás Cubas" com 1 dia de atraso

Ao fazer login e consultar empréstimos, você verá a multa calculada automaticamente com base na fórmula acima.

## Credentials de Teste

```
Admin
Username: admin
Senha: admin

Leitores
Username: ana    | Senha: 123
Username: bruno  | Senha: 123
Username: carla  | Senha: 123
Username: diego  | Senha: 123
Username: elisa  | Senha: 123
```

## Desenvolvedor

Madson Gustavo Fagundes Pinto de Carvalho
