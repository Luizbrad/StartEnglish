package startenglish.db.Entidades;

import java.time.LocalDate;


public class Cursos {
    
    private int CursoID;
    private String etapa;
    private LocalDate data_lancamento;
    private LocalDate data_encerramento;
    private String nomeCurso;
    private String descricao;
    private double preco;

    public Cursos(int CursoID, String etapa, LocalDate data_lancamento, LocalDate data_encerramento, String nomeCurso, String descricao, double preco) {
        this.CursoID = CursoID;
        this.etapa = etapa;
        this.data_lancamento = data_lancamento;
        this.data_encerramento = data_encerramento;
        this.nomeCurso = nomeCurso;
        this.descricao = descricao;
        this.preco = preco;
    }

    public Cursos(String etapa, LocalDate data_lancamento, LocalDate data_encerramento, String nomeCurso, String descricao, double preco) {
        this.etapa = etapa;
        this.data_lancamento = data_lancamento;
        this.data_encerramento = data_encerramento;
        this.nomeCurso = nomeCurso;
        this.descricao = descricao;
        this.preco = preco;
    }

   
    public Cursos() {
        
        this.descricao = "";
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public LocalDate getData_lancamento() {
        return data_lancamento;
    }

    public void setData_lancamento(LocalDate data_lancamento) {
        this.data_lancamento = data_lancamento;
    }

    public LocalDate getData_encerramento() {
        return data_encerramento;
    }

    public void setData_encerramento(LocalDate data_encerramento) {
        this.data_encerramento = data_encerramento;
    }

    
    
    public int getCursoID() {
        return CursoID;
    }

    public void setCursoID(int CursoID) {
        this.CursoID = CursoID;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
    @Override
    public String toString(){
        
        return this.getNomeCurso();
    }
    
}
