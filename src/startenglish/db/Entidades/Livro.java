package startenglish.db.Entidades;


public class Livro 
{
    private int LivroID;
    private String Nome;
    private double valor;
    private String editora;
    private String volume;

    public Livro(int LivroID, String Nome, double valor, String editora, String volume) {
        this.LivroID = LivroID;
        this.Nome = Nome;
        this.valor = valor;
        this.editora = editora;
        this.volume = volume;
    }

    public Livro(String Nome, double valor, String editora, String volume) {
        this.Nome = Nome;
        this.valor = valor;
        this.editora = editora;
        this.volume = volume;
    }

    public Livro() {
    }
    
    @Override
    public String toString()
    {
        return this.Nome;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
       
    public int getLivroID() {
        return LivroID;
    }

    public void setLivroID(int LivroID) {
        this.LivroID = LivroID;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
    
    
}
