package bibliotecaProjetoPOO;

import java.util.List;

public class Admin extends Usuario {

    // --- Construtor ---
    public Admin(String id, String nome, String username, String password) {
        // "super" chama o construtor da classe "mãe" (Usuario)
        super(id, nome, username, password, "Admin");
    }

    // --- Métodos do Admin ---
    // (Métodos vazios, prontos para a lógica)
    
    // O diagrama usa "dto" (Data Transfer Object)
    // Para cadastrar um livro, o "dto" é o próprio livro
    public Livro cadastrarLivro(Livro livro) {
        System.out.println("-> (ADMIN) Cadastrando livro: " + livro.getTitulo());
        // TODO: Implementar a lógica (ex: salvar em uma lista)
        return livro;
    }
    
    // Para editar, passamos o ID e os novos dados
    public Livro editarLivro(String id, Livro dadosNovos) {
        System.out.println("-> (ADMIN) Editando livro ID: " + id);
        // TODO: Implementar a lógica
        return null;
    }

    public void removerLivro(String id) {
        System.out.println("-> (ADMIN) Removendo livro ID: " + id);
        // TODO: Implementar a lógica
    }

    public List<Livro> listarTodos() {
        System.out.println("-> (ADMIN) Listando todos os livros...");
        // TODO: Implementar a lógica
        return null;
    }
    
    // Para cadastrar um usuário, o "dto" é o novo usuário
    public Usuario cadastrarUsuario(Usuario novoUsuario) {
        System.out.println("-> (ADMIN) Cadastrando usuário: " + novoUsuario.getNome());
        // TODO: Implementar a lógica
        return novoUsuario;
    }
    
    // O Admin também pode listar por gênero, como o Leitor
    public List<Livro> listarPorGenero(String genero) {
        System.out.println("-> (ADMIN) Buscando livros do gênero: " + genero);
        // TODO: Implementar a lógica
        return null;
    }
}