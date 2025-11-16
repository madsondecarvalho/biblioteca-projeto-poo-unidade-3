package bibliotecaProjetoPOO;

public class Livro {

    private String id;
    private String titulo;
    private String autor;
    private String genero;
    private boolean disponivel;

    public Livro(String id, String titulo, String autor, String genero) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.disponivel = true; 
    }

    public void emprestar() {
        if (this.disponivel) {
            this.disponivel = false;
        } else {
            System.out.println("Erro: O livro '" + this.titulo + "' já está emprestado.");
        }
    }

    public void devolver() {
        if (!this.disponivel) {
            this.disponivel = true;
        } else {
            System.out.println("Erro: O livro '" + this.titulo + "' já estava disponível.");
        }
    }

    
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public boolean isDisponivel() {
        return disponivel;
    }
    
    @Override
    public String toString() {
        return "Livro [id=" + id + ", titulo=" + titulo + ", autor=" + autor + ", disponivel=" + disponivel + "]";
    }
}