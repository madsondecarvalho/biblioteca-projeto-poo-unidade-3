package bibliotecaProjetoPOO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Tenta autenticar um usuário.
     * Retorna o objeto Usuario (Admin ou Leitor) se for bem-sucedido,
     * ou null se o login falhar.
     */
    public Usuario login(String username, String password) {
        String sql = "SELECT * FROM Usuario WHERE username = ? AND password = ?";
        
        // Este 'try-with-resources' agora pega uma conexão NOVA
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            // Se encontrou um usuário
            if (rs.next()) {
                String id = rs.getString("id");
                String nome = rs.getString("nome");
                String tipo = rs.getString("tipo");

                // Cria a classe correta (Admin ou Leitor)
                if ("Admin".equals(tipo)) {
                    return new Admin(id, nome, username, password);
                } else {
                    return new Leitor(id, nome, username, password);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao tentar fazer login: " + e.getMessage());
        }
        
        // Se não encontrou ou deu erro
        return null;
    }
    
    /**
     * Cadastra um novo usuário (Leitor) no banco de dados.
     */
    public Usuario cadastrarUsuario(String id, String nome, String username, String password) {
        String sql = "INSERT INTO Usuario(id, nome, username, password, tipo) VALUES(?, ?, ?, ?, 'Leitor')";
        
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.setString(2, nome);
            pstmt.setString(3, username);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
            
            System.out.println("Leitor " + nome + " cadastrado com sucesso.");
            return new Leitor(id, nome, username, password);
            
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
        return null;
    }
}