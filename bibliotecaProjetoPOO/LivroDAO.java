package bibliotecaProjetoPOO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    /**
     * Cadastra um novo Livro no banco de dados.
     */
    public void cadastrarLivro(Livro livro) {
        String sql = "INSERT INTO Livro(id, titulo, autor, genero, disponivel) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, livro.getId());
            pstmt.setString(2, livro.getTitulo());
            pstmt.setString(3, livro.getAutor());
            pstmt.setString(4, livro.getGenero());
            pstmt.setBoolean(5, livro.isDisponivel());
            
            pstmt.executeUpdate();
            System.out.println("Livro '" + livro.getTitulo() + "' cadastrado com sucesso.");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar livro: " + e.getMessage());
        }
    }

    /**
     * Lista todos os livros cadastrados no banco de dados.
     */
    public List<Livro> listarTodosLivros() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM Livro";

        try (Connection conn = ConexaoSQLite.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Recria o objeto Livro com dados do banco
                Livro livro = new Livro(
                    rs.getString("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("genero")
                );
                // Ajusta a disponibilidade (o construtor padrão define como true)
                if (!rs.getBoolean("disponivel")) {
                    livro.emprestar(); // Método "emprestar" coloca disponibilidade como false
                }
                livros.add(livro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros: " + e.getMessage());
        }
        return livros;
    }
    
    /**
     * Busca um livro específico pelo seu ID.
     * Retorna o objeto Livro ou null se não for encontrado.
     */
    public Livro buscarPorId(String id) {
        String sql = "SELECT * FROM Livro WHERE id = ?";
        
        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Livro livro = new Livro(
                    rs.getString("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("genero")
                );
                // Ajusta a disponibilidade do objeto criado
                if (!rs.getBoolean("disponivel")) {
                    livro.emprestar();
                }
                return livro;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por ID: " + e.getMessage());
        }
        return null; // Não encontrado
    }

    /**
     * Atualiza o status de disponibilidade de um livro no banco de dados.
     * (true = disponível, false = emprestado)
     */
    public void atualizarDisponibilidade(String livroId, boolean disponivel) {
        String sql = "UPDATE Livro SET disponivel = ? WHERE id = ?";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, disponivel);
            pstmt.setString(2, livroId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar disponibilidade do livro: " + e.getMessage());
        }
    }
    
    /**
     * Remove um livro do banco de dados pelo ID.
     */
    public void removerLivro(String id) {
        String sql = "DELETE FROM Livro WHERE id = ?";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Livro com ID " + id + " foi removido com sucesso.");
            } else {
                System.out.println("Nenhum livro encontrado com o ID " + id + ".");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao remover livro. Verifique se ele está em um empréstimo ativo.");
            System.err.println("Detalhe: " + e.getMessage());
        }
    }

    /**
     * Edita os dados de um livro (Título, Autor, Gênero) baseado no ID.
     */
    public void editarLivro(String id, String novoTitulo, String novoAutor, String novoGenero) {
        String sql = "UPDATE Livro SET titulo = ?, autor = ?, genero = ? WHERE id = ?";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, novoTitulo);
            pstmt.setString(2, novoAutor);
            pstmt.setString(3, novoGenero);
            pstmt.setString(4, id); // ID é o último parâmetro (do WHERE)
            
            int linhasAfetadas = pstmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("Livro ID " + id + " atualizado com sucesso.");
            } else {
                System.out.println("Nenhum livro encontrado com o ID " + id + ".");
            }

        } catch (SQLException e) {
            System.err.println("Erro ao editar livro: " + e.getMessage());
        }
    }

    /**
     * Lista todos os livros que correspondem a um gênero.
     * A busca ignora maiúsculas/minúsculas.
     */
    public List<Livro> listarPorGenero(String genero) {
        List<Livro> livros = new ArrayList<>();
        // Usamos UPPER() para fazer a busca sem diferenciar maiúsculas/minúsculas
        String sql = "SELECT * FROM Livro WHERE UPPER(genero) = UPPER(?)";

        try (Connection conn = ConexaoSQLite.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, genero);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getString("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("genero")
                );
                if (!rs.getBoolean("disponivel")) {
                    livro.emprestar();
                }
                livros.add(livro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros por gênero: " + e.getMessage());
        }
        return livros;
    }
}