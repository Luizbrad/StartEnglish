package startenglish.db.Entidades;


public class Endereco {
    
    private int EnderecoID;
    private String Rua;
    private String CEP;
    private String bairro;
    private int numero;
    private String Cidade;

    public Endereco(int EnderecoID, String Rua, String CEP, String bairro, int numero, String Cidade) {
        this.EnderecoID = EnderecoID;
        this.Rua = Rua;
        this.CEP = CEP;
        this.bairro = bairro;
        this.numero = numero;
        this.Cidade = Cidade;
    }

    public Endereco(String Rua, String CEP, String bairro, int numero, String Cidade) {
        this.Rua = Rua;
        this.CEP = CEP;
        this.bairro = bairro;
        this.numero = numero;
        this.Cidade = Cidade;
    }

    public Endereco() {
        
        this.Rua = "";
        this.CEP = "";
        this.bairro = "";
        this.numero = 0;
        this.Cidade = "";
    }

    
    public int getEnderecoID() {
        return EnderecoID;
    }

    public void setEnderecoID(int EnderecoID) {
        this.EnderecoID = EnderecoID;
    }

    public String getRua() {
        return Rua;
    }

    public void setRua(String Rua) {
        this.Rua = Rua;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String Cidade) {
        this.Cidade = Cidade;
    }
    
    
}
