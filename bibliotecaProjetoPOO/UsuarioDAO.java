package bibliotecaProjetoPOO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    /**
     * Realiza o login de um usuário
     */
    public Usuario login(String username, String password) {
        String sql = "SELECT * FROM Usuario WHERE username = ? AND password = ?";
        
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String tipo = rs.getString("tipo");
                if (tipo.equals("Leitor")) {
                    return new Leitor(
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                } else {
                    // Usa a classe Admin em vez de Usuario
                    return new Admin(
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro no login: " + e.getMessage());
        }
        
        return null; // Login falhou
    }

    /**
     * Cadastra um novo usuário (leitor)
     */
    public void cadastrarUsuario(String id, String nome, String username, String password) {
        String sql = "INSERT INTO Usuario(id, nome, username, password, tipo) VALUES(?, ?, ?, ?, 'Leitor')";
        
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, nome);
            pstmt.setString(3, username);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            
            System.out.println("Leitor '" + nome + "' cadastrado com sucesso!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    /**
     * Lista todos os leitores (usuários do tipo 'Leitor')
     */
    public List<Usuario> listarTodosLeitores() {
        List<Usuario> leitores = new ArrayList<>();
        String sql = "SELECT * FROM Usuario WHERE tipo = 'Leitor'";

        try (Connection conn = ConexaoSQLite.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario leitor = new Leitor(
                    rs.getString("id"),
                    rs.getString("nome"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                leitores.add(leitor);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar leitores: " + e.getMessage());
        }
        return leitores;
    }

    /**
     * Remove um usuário pelo ID (apenas leitores, não remove admin)
     */
    public void removerUsuario(String id) {
        // Primeiro verifica se é um leitor
        String sqlVerificar = "SELECT tipo FROM Usuario WHERE id = ?";
        String sqlRemover = "DELETE FROM Usuario WHERE id = ? AND tipo = 'Leitor'";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmtVerificar = conn.prepareStatement(sqlVerificar);
             PreparedStatement pstmtRemover = conn.prepareStatement(sqlRemover)) {
            
            pstmtVerificar.setString(1, id);
            ResultSet rs = pstmtVerificar.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                if (tipo.equals("Leitor")) {
                    pstmtRemover.setString(1, id);
                    int linhasAfetadas = pstmtRemover.executeUpdate();
                    
                    if (linhasAfetadas > 0) {
                        System.out.println("Leitor removido com sucesso.");
                    } else {
                        System.out.println("Erro ao remover leitor.");
                    }
                } else {
                    System.out.println("Não é permitido remover administradores.");
                }
            } else {
                System.out.println("Usuário não encontrado.");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao remover usuário: " + e.getMessage());
        }
    }

    /**
     * Busca um usuário pelo ID
     */
    public Usuario buscarPorId(String id) {
        String sql = "SELECT * FROM Usuario WHERE id = ?";
        
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String tipo = rs.getString("tipo");
                if (tipo.equals("Leitor")) {
                    return new Leitor(
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                } else {
                    return new Admin(
                        rs.getString("id"),
                        rs.getString("nome"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário por ID: " + e.getMessage());
        }
        return null;
    }
}