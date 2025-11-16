package bibliotecaProjetoPOO;

public class Usuario {
    private String id;
    private String nome;
    private String username;
    private String password;
    private String tipo; // "Admin" ou "Leitor"

    public Usuario(String id, String nome, String username, String password, String tipo) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}