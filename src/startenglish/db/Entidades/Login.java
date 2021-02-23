package startenglish.db.Entidades;

public class Login {
    private String user;
    private String senha;
    private char status;
    private int nivel;
    private Funcionario func;

    public Login()
    {
        
    }
    
    public Login(String user, String senha, char status, int nivel, Funcionario func) {
        this.user = user;
        this.senha = senha;
        this.status = status;
        this.nivel = nivel;
        this.func = func;
    }  
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Funcionario getFunc() {
        return func;
    }

    public void setFunc(Funcionario func) {
        this.func = func;
    }
    
}
