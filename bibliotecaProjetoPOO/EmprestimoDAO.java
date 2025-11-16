package bibliotecaProjetoPOO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {

    /**
     * Registra um novo empréstimo no banco de dados.
     * Define a data de empréstimo para "agora" e data de vencimento para 14 dias depois.
     */
    public boolean realizarEmprestimo(String usuarioId, String livroId) {
        String sql = "INSERT INTO Emprestimo(dataEmprestimo, dataVencimento, usuarioId, livroId) VALUES (?, ?, ?, ?)";
        
        // Pega a data atual e calcula vencimento (14 dias)
        LocalDate dataEmprestimo = LocalDate.now();
        LocalDate dataVencimento = dataEmprestimo.plusDays(14);

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dataEmprestimo.toString());
            pstmt.setString(2, dataVencimento.toString());
            pstmt.setString(3, usuarioId);
            pstmt.setString(4, livroId);
            pstmt.executeUpdate();
            
            return true; // Sucesso

        } catch (SQLException e) {
            System.err.println("Erro ao realizar empréstimo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra uma devolução no banco de dados calculando multa se houver atraso.
     * CORRIGIDO: Usa uma única conexão para evitar "database locked"
     */
    public boolean registrarDevolucao(String usuarioId, String livroId) {
        Connection conn = null;
        PreparedStatement pstmtBuscar = null;
        PreparedStatement pstmtAtualizar = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoSQLite.getConexao();
            conn.setAutoCommit(false); // Inicia transação
            
            // Primeiro busca o empréstimo ativo
            String sqlBuscar = "SELECT id, dataVencimento FROM Emprestimo WHERE usuarioId = ? AND livroId = ? AND dataDevolucao IS NULL";
            pstmtBuscar = conn.prepareStatement(sqlBuscar);
            pstmtBuscar.setString(1, usuarioId);
            pstmtBuscar.setString(2, livroId);
            rs = pstmtBuscar.executeQuery();

            if (rs.next()) {
                int emprestimoId = rs.getInt("id");
                String dataVencimento = rs.getString("dataVencimento");
                String dataHoje = LocalDate.now().toString();
                
                // Calcula a multa
                float multa = ConexaoSQLite.calcularMulta(dataVencimento, dataHoje);
                
                // Atualiza o empréstimo com data de devolução e multa
                String sqlAtualizar = "UPDATE Emprestimo SET dataDevolucao = ?, multa = ? WHERE id = ?";
                pstmtAtualizar = conn.prepareStatement(sqlAtualizar);
                pstmtAtualizar.setString(1, dataHoje);
                pstmtAtualizar.setFloat(2, multa);
                pstmtAtualizar.setInt(3, emprestimoId);
                pstmtAtualizar.executeUpdate();
                
                conn.commit(); // Confirma a transação
                
                if (multa > 0) {
                    long diasAtraso = calcularDiasAtraso(dataVencimento, dataHoje);
                    System.out.println("Multa aplicada: R$ " + String.format("%.2f", multa) + " (" + diasAtraso + " dias de atraso)");
                }
                
                return true;
            }
            
            conn.commit();
            return false; // Não encontrou empréstimo ativo

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Reverte em caso de erro
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao registrar devolução: " + e.getMessage());
            return false;
        } finally {
            // Fecha todos os recursos na ordem inversa de abertura
            try {
                if (rs != null) rs.close();
                if (pstmtBuscar != null) pstmtBuscar.close();
                if (pstmtAtualizar != null) pstmtAtualizar.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    /**
     * Calcula dias de atraso para mensagem informativa
     */
    private long calcularDiasAtraso(String dataVencimento, String dataDevolucao) {
        try {
            LocalDate vencimento = LocalDate.parse(dataVencimento);
            LocalDate devolucao = LocalDate.parse(dataDevolucao);
            return java.time.temporal.ChronoUnit.DAYS.between(vencimento, devolucao);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Lista os empréstimos ativos (não devolvidos) de um usuário específico.
     * Agora inclui data de vencimento e alerta de atraso.
     */
    public List<String> listarEmprestimosAtivosPorUsuario(String usuarioId) {
        List<String> emprestimosAtivos = new ArrayList<>();
        
        String sql = "SELECT l.titulo, e.dataEmprestimo, e.dataVencimento, l.id as livroId " +
                     "FROM Emprestimo e " +
                     "JOIN Livro l ON e.livroId = l.id " +
                     "WHERE e.usuarioId = ? AND e.dataDevolucao IS NULL";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String dataVencimento = rs.getString("dataVencimento");
                LocalDate vencimento = LocalDate.parse(dataVencimento);
                LocalDate hoje = LocalDate.now();
                
                String status = "";
                if (hoje.isAfter(vencimento)) {
                    long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(vencimento, hoje);
                    float multa = diasAtraso * 0.35f;
                    status = " [ATRASADO! " + diasAtraso + " dias - Multa: R$ " + String.format("%.2f", multa) + "]";
                }
                
                String linha = String.format(
                    "ID: %s | Título: %s | Emprestado em: %s | Vencimento: %s%s",
                    rs.getString("livroId"),
                    rs.getString("titulo"),
                    rs.getString("dataEmprestimo"),
                    dataVencimento,
                    status
                );
                emprestimosAtivos.add(linha);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar empréstimos: " + e.getMessage());
        }
        
        return emprestimosAtivos;
    }

    /**
     * Lista o histórico completo de empréstimos de um usuário com multas
     */
    public List<String> listarHistoricoEmprestimos(String usuarioId) {
        List<String> historico = new ArrayList<>();
        
        String sql = "SELECT l.titulo, e.dataEmprestimo, e.dataVencimento, e.dataDevolucao, e.multa, l.id as livroId " +
                     "FROM Emprestimo e " +
                     "JOIN Livro l ON e.livroId = l.id " +
                     "WHERE e.usuarioId = ? " +
                     "ORDER BY e.dataEmprestimo DESC";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuarioId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String dataDevolucao = rs.getString("dataDevolucao");
                float multa = rs.getFloat("multa");
                String status = (dataDevolucao == null) ? "EM ABERTO" : "DEVOLVIDO";
                
                String linha = String.format(
                    "ID: %s | %s | Empréstimo: %s | Devolução: %s | Multa: R$ %.2f | %s",
                    rs.getString("livroId"),
                    rs.getString("titulo"),
                    rs.getString("dataEmprestimo"),
                    (dataDevolucao == null ? "---" : dataDevolucao),
                    multa,
                    status
                );
                historico.add(linha);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar histórico: " + e.getMessage());
        }
        
        return historico;
    }
    
    /**
     * Lista todos os empréstimos do sistema (para admin)
     */
    public List<String> listarTodosEmprestimos() {
        List<String> todosEmprestimos = new ArrayList<>();
        
        String sql = "SELECT e.*, l.titulo, u.nome as usuarioNome " +
                     "FROM Emprestimo e " +
                     "JOIN Livro l ON e.livroId = l.id " +
                     "JOIN Usuario u ON e.usuarioId = u.id " +
                     "ORDER BY e.dataEmprestimo DESC";

        try (Connection conn = ConexaoSQLite.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String dataDevolucao = rs.getString("dataDevolucao");
                String status = (dataDevolucao == null) ? "EM ABERTO" : "DEVOLVIDO";
                float multa = rs.getFloat("multa");
                
                String linha = String.format(
                    "ID: %d | Livro: %s | Usuário: %s | Empréstimo: %s | Vencimento: %s | Devolução: %s | Multa: R$ %.2f | %s",
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("usuarioNome"),
                    rs.getString("dataEmprestimo"),
                    rs.getString("dataVencimento"),
                    (dataDevolucao == null ? "---" : dataDevolucao),
                    multa,
                    status
                );
                todosEmprestimos.add(linha);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os empréstimos: " + e.getMessage());
        }
        
        return todosEmprestimos;
    }    
    
}