package bibliotecaProjetoPOO;

import java.util.List;

public class Leitor extends Usuario {

    public Leitor(String id, String nome, String username, String password) {
        super(id, nome, username, password, "Leitor");
    }


    public List<Livro> listarLivrosPorGenero(String genero) {
        System.out.println("-> (LEITOR) Buscando livros do gênero: " + genero);
        return null;
    }

    public List<Livro> listarTodosLivros() {
        System.out.println("-> (LEITOR) Buscando todos os livros...");
        return null;
    }

    public void pedirEmprestimo(String livroId) {
        System.out.println("-> (LEITOR) " + this.nome + " está pedindo o livro ID: " + livroId);
    }

    public void devolverLivro(String livroId) {
        System.out.println("-> (LEITOR) " + this.nome + " está devolvendo o livro ID: " + livroId);
    }
}