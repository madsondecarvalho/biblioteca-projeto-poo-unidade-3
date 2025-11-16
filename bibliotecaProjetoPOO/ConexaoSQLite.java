package bibliotecaProjetoPOO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ConexaoSQLite {

    private static final String NOME_BANCO = "biblioteca.db";
    private static final float VALOR_MULTA_POR_DIA = 0.35f; // 35 centavos por dia

    /**
     * Conecta (ou cria) ao banco de dados SQLite.
     * Este método agora cria e retorna uma *nova* conexão a cada chamada.
     */
    public static Connection getConexao() throws SQLException {
        // Define a string de conexão JDBC para SQLite
        String url = "jdbc:sqlite:" + NOME_BANCO;
        // Estabelece e retorna a conexão diretamente
        return DriverManager.getConnection(url);
    }

    /**
     * Cria as tabelas do banco de dados se elas não existirem.
     * Deve ser chamado na inicialização do sistema.
     */
    public static void criarTabelas() {
        // SQL para criar a tabela de Usuários
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS Usuario (" +
                            "  id TEXT PRIMARY KEY," +
                            "  nome TEXT NOT NULL," +
                            "  username TEXT NOT NULL UNIQUE," +
                            "  password TEXT NOT NULL," +
                            "  tipo TEXT NOT NULL" + // 'Admin' ou 'Leitor'
                            ");";

        // SQL para criar a tabela de Livros
        String sqlLivro = "CREATE TABLE IF NOT EXISTS Livro (" +
                          "  id TEXT PRIMARY KEY," + // Pode ser o ISBN
                          "  titulo TEXT NOT NULL," +
                          "  autor TEXT," +
                          "  genero TEXT," +
                          "  disponivel BOOLEAN NOT NULL DEFAULT 1" +
                          ");";
        
        // SQL para criar a tabela de Empréstimos
        String sqlEmprestimo = "CREATE TABLE IF NOT EXISTS Emprestimo (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  dataEmprestimo TEXT NOT NULL," +
                "  dataDevolucao TEXT," +
                "  dataVencimento TEXT NOT NULL," + 
                "  multa REAL DEFAULT 0," + 
                "  livroId TEXT NOT NULL," +
                "  usuarioId TEXT NOT NULL," +
                "  FOREIGN KEY (livroId) REFERENCES Livro(id)," +
                "  FOREIGN KEY (usuarioId) REFERENCES Usuario(id)" +
                ");";

        // Este 'try-with-resources' pega uma conexão NOVA e a fecha automaticamente.
        try (Connection conn = getConexao();
             Statement stmt = conn.createStatement()) {
            
            // Executa as criações de tabela
            stmt.execute(sqlUsuario);
            stmt.execute(sqlLivro);
            stmt.execute(sqlEmprestimo);
            
            System.out.println("Tabelas verificadas/criadas com sucesso.");
            
            // Adiciona os dados iniciais (admin, 5 leitores e 15 livros)
            popularDadosIniciais(conn);

        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas ou ao obter conexão: " + e.getMessage());
        }
    }
    
    /**
     * Popula o banco com dados iniciais (admin, leitores e livros).
     * USA 'INSERT OR IGNORE' para ser seguro e rodar sempre, 
     * sem duplicar dados e sem falhar se os dados já existirem.
     */
    private static void popularDadosIniciais(Connection conn) throws SQLException {
        
        // Usaremos um único Statement para todas as operações de batch
        try (Statement stmt = conn.createStatement()) {
            System.out.println("Verificando e inserindo dados iniciais (seeds)...");

            // --- 1. Popular Usuários ---
            // 'INSERT OR IGNORE' tentará inserir. Se o 'id' ou 'username' já existir,
            // ele simplesmente ignora o comando e continua.
            stmt.addBatch("INSERT OR IGNORE INTO Usuario(id, nome, username, password, tipo) " +
                          "VALUES('admin-001', 'Administrador Padrão', 'admin', 'admin', 'Admin')");
            stmt.addBatch("INSERT OR IGNORE INTO Usuario(id, nome, username, password, tipo) " +
                          "VALUES('L001', 'Ana Silva', 'ana', '123', 'Leitor')");
            stmt.addBatch("INSERT OR IGNORE INTO Usuario(id, nome, username, password, tipo) " +
                          "VALUES('L002', 'Bruno Costa', 'bruno', '123', 'Leitor')");
            stmt.addBatch("INSERT OR IGNORE INTO Usuario(id, nome, username, password, tipo) " +
                          "VALUES('L003', 'Carla Dias', 'carla', '123', 'Leitor')");
            stmt.addBatch("INSERT OR IGNORE INTO Usuario(id, nome, username, password, tipo) " +
                          "VALUES('L004', 'Diego Faria', 'diego', '123', 'Leitor')");
            stmt.addBatch("INSERT OR IGNORE INTO Usuario(id, nome, username, password, tipo) " +
                          "VALUES('L005', 'Elisa Mendes', 'elisa', '123', 'Leitor')");

            // --- 2. Popular Livros ---
            // Também usa 'INSERT OR IGNORE'
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('B001', 'Dom Casmurro', 'Machado de Assis', 'Romance')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('B002', 'Memórias Póstumas de Brás Cubas', 'Machado de Assis', 'Romance')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('B003', 'Grande Sertão: Veredas', 'João Guimarães Rosa', 'Ficção')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('B004', 'Capitães da Areia', 'Jorge Amado', 'Romance')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('B005', 'Vidas Secas', 'Graciliano Ramos', 'Romance')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('B006', 'A Hora da Estrela', 'Clarice Lispector', 'Ficção')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('B007', 'O Auto da Compadecida', 'Ariano Suassuna', 'Teatro')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('M001', '1984', 'George Orwell', 'Distopia')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('M002', 'O Pequeno Príncipe', 'Antoine de Saint-Exupéry', 'Fantasia')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('M003', 'Dom Quixote', 'Miguel de Cervantes', 'Clássico')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('M004', 'Guerra e Paz', 'Liev Tolstói', 'Histórico')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('M005', 'O Conde de Monte Cristo', 'Alexandre Dumas', 'Aventura')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('M006', 'Cem Anos de Solidão', 'Gabriel García Márquez', 'Realismo Mágico')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('M007', 'O Grande Gatsby', 'F. Scott Fitzgerald', 'Ficção')");
            stmt.addBatch("INSERT OR IGNORE INTO Livro(id, titulo, autor, genero) VALUES ('M008', 'O Apanhador no Campo de Centeio', 'J.D. Salinger', 'Ficção')");
            
            // --- 3. Inserir empréstimos iniciais (Ana e Carla) com atraso ---
            LocalDate hoje = LocalDate.now();
            LocalDate dataEmprestimoAna = hoje.minusDays(10);
            LocalDate dataVencimentoAna = dataEmprestimoAna.plusDays(7);
            LocalDate dataEmprestimoCarla = hoje.minusDays(8);
            LocalDate dataVencimentoCarla = dataEmprestimoCarla.plusDays(7);

            stmt.addBatch("INSERT OR IGNORE INTO Emprestimo (dataEmprestimo, dataVencimento, livroId, usuarioId) " +
                          "SELECT '" + dataEmprestimoAna + "', '" + dataVencimentoAna + "', 'B001', 'L001' " +
                          "WHERE NOT EXISTS (SELECT 1 FROM Emprestimo WHERE usuarioId = 'L001' AND livroId = 'B001' AND dataEmprestimo = '" + dataEmprestimoAna + "')");

            stmt.addBatch("INSERT OR IGNORE INTO Emprestimo (dataEmprestimo, dataVencimento, livroId, usuarioId) " +
                          "SELECT '" + dataEmprestimoCarla + "', '" + dataVencimentoCarla + "', 'B002', 'L003' " +
                          "WHERE NOT EXISTS (SELECT 1 FROM Emprestimo WHERE usuarioId = 'L003' AND livroId = 'B002' AND dataEmprestimo = '" + dataEmprestimoCarla + "')");

            // --- 4. Executar TUDO ---
            int[] resultados = stmt.executeBatch();
            System.out.println(resultados.length + " comandos de 'INSERT OR IGNORE' executados.");
            
        } // O 'try-with-resources' fecha o Statement
    }

    /**
     * Calcula a multa por atraso na devolução (35 centavos por dia de atraso)
     * @param dataVencimento Data de vencimento do empréstimo
     * @param dataDevolucao Data real da devolução
     * @return Valor da multa calculada
     */
    public static float calcularMulta(String dataVencimento, String dataDevolucao) {
        if (dataVencimento == null || dataDevolucao == null) {
            return 0.0f;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate vencimento = LocalDate.parse(dataVencimento, formatter);
            LocalDate devolucao = LocalDate.parse(dataDevolucao, formatter);
            
            // Se a devolução foi feita antes ou no vencimento, não há multa
            if (devolucao.isBefore(vencimento) || devolucao.isEqual(vencimento)) {
                return 0.0f;
            }
            
            // Calcula dias de atraso
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(vencimento, devolucao);
            
            if (diasAtraso > 0) {
                return diasAtraso * VALOR_MULTA_POR_DIA;
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao calcular multa: " + e.getMessage());
        }
        
        return 0.0f;
    }

    /**
     * Método para processar a devolução de um empréstimo calculando a multa
     * @param emprestimoId ID do empréstimo
     * @param dataDevolucao Data da devolução
     * @return Valor da multa aplicada
     */
    public static float processarDevolucao(int emprestimoId, String dataDevolucao) {
        String sqlBuscarVencimento = "SELECT dataVencimento FROM Emprestimo WHERE id = " + emprestimoId;
        String sqlAtualizarDevolucao = "UPDATE Emprestimo SET dataDevolucao = ?, multa = ? WHERE id = ?";
        
        try (Connection conn = getConexao();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sqlBuscarVencimento)) {
            
            if (rs.next()) {
                String dataVencimento = rs.getString("dataVencimento");
                float multa = calcularMulta(dataVencimento, dataDevolucao);
                
                // Atualiza o empréstimo com a data de devolução e multa
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sqlAtualizarDevolucao)) {
                    pstmt.setString(1, dataDevolucao);
                    pstmt.setFloat(2, multa);
                    pstmt.setInt(3, emprestimoId);
                    pstmt.executeUpdate();
                }
                
                System.out.println("Devolução processada. Multa calculada: R$ " + String.format("%.2f", multa));
                return multa;
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao processar devolução: " + e.getMessage());
        }
        
        return 0.0f;
    }
}