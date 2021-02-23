package startenglish.db.Entidades;

import javafx.scene.control.CheckBox;

public class Funcionario {
    private int ID;
    private String Nome;
    private String cpf;
    private String rg;
    private String email;
    private String fone;
    private Endereco endereco;
    private Boolean professor;

    public Funcionario(int ID, String Nome, String cpf, String rg, String email, String fone, Endereco endereco) {
        this.ID = ID;
        this.Nome = Nome;
        this.cpf = cpf;
        this.rg = rg;
        this.email = email;
        this.fone = fone;
        this.endereco = endereco;
        professor = false;
    }

    public Funcionario(String Nome, String cpf, String rg, String email, String fone, Endereco endereco) {
        this.Nome = Nome;
        this.cpf = cpf;
        this.rg = rg;
        this.email = email;
        this.fone = fone;
        this.endereco = endereco;
    }
    
    @Override
    public String toString()
    {
        return this.Nome;
    }

    public Funcionario() {
    } 
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Boolean getProfessor() {
        return professor;
    }

    public void setProfessor(Boolean professor) {
        this.professor = professor;
    }
}
