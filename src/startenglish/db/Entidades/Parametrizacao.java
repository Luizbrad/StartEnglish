package startenglish.db.Entidades;


public class Parametrizacao {
    
    private String nome;
    private String telefone;
    private String email;
    private String RazaoSocial;
    private Endereco endereco;
    private String CNPJ;

    public Parametrizacao(String nome, String telefone, String email, String RazaoSocial, Endereco endereco, String CNPJ) {
        
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.RazaoSocial = RazaoSocial;
        this.endereco = endereco;
        this.CNPJ = CNPJ;
    }

    public Parametrizacao(){
        
        this.nome = "";
        this.telefone = "";
        this.email = "";
        this.RazaoSocial = "";
        this.endereco = new Endereco();
        this.CNPJ = "";
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRazaoSocial() {
        return RazaoSocial;
    }

    public void setRazaoSocial(String RazaoSocial) {
        this.RazaoSocial = RazaoSocial;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }
    
    
    @Override
    public String toString(){
        return this.nome;
    }
    
}
