package startenglish.db.Entidades;

public class Aluno {
    
    private int ID;
    private Endereco endereco;
    private String rg;
    private String cpf;
    private String email;
    private String nome;
    private String fone;

    public Aluno(int ID, Endereco endereco, String rg, String cpf, String email, String nome, String fone) {
        this.ID = ID;
        this.endereco = endereco;
        this.rg = rg;
        this.cpf = cpf;
        this.email = email;
        this.nome = nome;
        this.fone = fone;
    }

    public Aluno(Endereco endereco, String rg, String cpf, String email, String nome, String fone) {
        this.endereco = endereco;
        this.rg = rg;
        this.cpf = cpf;
        this.email = email;
        this.nome = nome;
        this.fone = fone;
    }

    public Aluno() {
        this.endereco = new Endereco();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }
  
    
    @Override
    public String toString(){
        
        return this.nome;
    }
    
}
