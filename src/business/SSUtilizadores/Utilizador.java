package business.SSUtilizadores;

public class Utilizador {
    private String id;
    private String nome;
    private String curso;
    private String password;

    public Utilizador(String id, String nome, String curso, String password) {
        this.id = id;
        this.nome = nome;
        this.curso = curso;
        this.password = password;
    }

    public String getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getCurso() {
        return this.curso;
    }

    public String getPassword() {
        return this.password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}