package bibliotecaProjetoPOO;

public abstract class Usuario {

    protected String id;
    protected String nome;
    protected String tipo; 
    protected String username;
    protected String password; 

    public Usuario(String id, String nome, String username, String password, String tipo) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }
    
   
	public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUsername() {
        return username;
    }
    
    public boolean verificarSenha(String senhaDigitada) {
        return this.password.equals(senhaDigitada);
    }
}