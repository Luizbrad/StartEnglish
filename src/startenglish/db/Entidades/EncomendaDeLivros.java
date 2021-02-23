package startenglish.db.Entidades;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class EncomendaDeLivros {
    
    
    private int codigoEnc;
    private Funcionario func;
    private LocalDate dataEncomenda;
    private String fornecedor;
    private double valor;
    private LocalDate previsaoEntrega;
    private List<ItemEncomenda> itens;

    public EncomendaDeLivros() {
    }

    public EncomendaDeLivros(int codigoEnc, Funcionario func, LocalDate dataEncomenda, String fornecedor, double valor, LocalDate previsaoEntrega, List<ItemEncomenda> itens) {
        this.codigoEnc = codigoEnc;
        this.func = func;
        this.dataEncomenda = dataEncomenda;
        this.fornecedor = fornecedor;
        this.valor = valor;
        this.previsaoEntrega = previsaoEntrega;
        this.itens = itens;
    }

    public EncomendaDeLivros(int codigoEnc, Funcionario func, LocalDate dataEncomenda, String fornecedor, double valor, LocalDate previsaoEntrega) {
        this.codigoEnc = codigoEnc;
        this.func = func;
        this.dataEncomenda = dataEncomenda;
        this.fornecedor = fornecedor;
        this.valor = valor;
        this.previsaoEntrega = previsaoEntrega;
    }
    
    public int getCodigoEnc() {
        return codigoEnc;
    }

    public void setCodigoEnc(int codigoEnc) {
        this.codigoEnc = codigoEnc;
    }

    public Funcionario getFunc() {
        return func;
    }

    public void setFunc(Funcionario func) {
        this.func = func;
    }

    public LocalDate getDataEncomenda() {
        return dataEncomenda;
    }

    public void setDataEncomenda(LocalDate dataEncomenda) {
        this.dataEncomenda = dataEncomenda;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(LocalDate previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

    public List<ItemEncomenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemEncomenda> itens) {
        this.itens = itens;
    }
    
    
}
