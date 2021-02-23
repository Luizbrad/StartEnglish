package startenglish.db.Entidades;


public class Matricula 
{
    
    private int nummat;
    private Livro livro;
    private Turma turmaID;
    private Aluno aluno;
    private char ativo;
    private double valor;
    private String instuiEnsino;
    private String infoCancelamento;
    private int desconto;
    private String nomeResponsável;
    private String nivel;
    

    public Matricula() 
    {
        
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Matricula(int nummat, Livro livro, Turma turmaID, Aluno aluno, char ativo, double valor,String nivel) {
        this.nummat = nummat;
        this.livro = livro;
        this.turmaID = turmaID;
        this.aluno = aluno;
        this.ativo = ativo;
        this.valor = valor;
        this.nivel=nivel;
    }

    public Matricula(int nummat, Livro livro, Turma turmaID, Aluno aluno, char ativo, double valor, String instuiEnsino, String infoCancelamento, int desconto, String nomeResponsável,String nivel) {
        this.nummat = nummat;
        this.livro = livro;
        this.turmaID = turmaID;
        this.aluno = aluno;
        this.ativo = ativo;
        this.valor = valor;
        this.instuiEnsino = instuiEnsino;
        this.infoCancelamento = infoCancelamento;
        this.desconto = desconto;
        this.nomeResponsável = nomeResponsável;
        this.nivel=nivel;
    }

    
    
      
    public int getNummat() {
        return nummat;
    }

    public void setNummat(int nummat) {
        this.nummat = nummat;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Turma getTurmaID() {
        return turmaID;
    }

    public void setTurmaID(Turma turmaID) {
        this.turmaID = turmaID;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public char getAtivo() {
        return ativo;
    }

    public void setAtivo(char ativo) {
        this.ativo = ativo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getInstuiEnsino() {
        return instuiEnsino;
    }

    public void setInstuiEnsino(String instuiEnsino) {
        this.instuiEnsino = instuiEnsino;
    }

    public String getInfoCancelamento() {
        return infoCancelamento;
    }

    public void setInfoCancelamento(String infoCancelamento) {
        this.infoCancelamento = infoCancelamento;
    }

    public int getDesconto() 
    {
        return desconto;
    }

    public void setDesconto(int desconto) 
    {
        this.desconto = desconto;
    }
    

    public String getNomeResponsável() {
        return nomeResponsável;
    }

    public void setNomeResponsável(String nomeResponsável) {
        this.nomeResponsável = nomeResponsável;
    }
    
    
    
    
}
