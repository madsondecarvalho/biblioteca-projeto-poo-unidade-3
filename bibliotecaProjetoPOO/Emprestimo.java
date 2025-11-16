package bibliotecaProjetoPOO;

// Não precisamos mais importar java.util.Date

public class Emprestimo {

    // --- Atributos ---
    // Alteramos de Date para String para ser compatível com o SQLite (TEXT)
    private String dataEmprestimo;
    private String dataDevolucao; 
    
    private String livroId;
    private String usuarioId;
    
    private Livro livro;
    private Usuario usuario;

    // --- Construtor ---
    // (Este construtor é usado quando criamos um NOVO empréstimo)
    public Emprestimo(Usuario usuario, Livro livro, String dataEmprestimo) {
        this.usuario = usuario;
        this.livro = livro;
        this.setUsuarioId(usuario.getId());
        this.setLivroId(livro.getId());
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = null; // Inicia como nulo
    }
    
    // --- Getters ---
    public String getDataEmprestimo() {
        return dataEmprestimo;
    }

    public String getDataDevolucao() {
        return dataDevolucao;
    }
    
    public void setDataDevolucao(String data) {
        this.dataDevolucao = data;
    }

    public Livro getLivro() {
        return livro;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
    // O método de calcular multa pode ser implementado depois
    public double calcularMulta() {
        // TODO: Implementar lógica de cálculo de multa
        return 0.0;
    }

	public String getLivroId() {
		return livroId;
	}

	public void setLivroId(String livroId) {
		this.livroId = livroId;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}
}