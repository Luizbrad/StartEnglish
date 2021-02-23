package startenglish.db.Entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Recebimentos {
    
    private int recebimentoid;
    private Caixa caixa;
    private Matricula mat;
    private LocalDate dtvencimento;
    private LocalDate dtreceb;
    private LocalDate dtemissoa;
    private String dtVencimentoFORM;
    private String dtRecebFORM;
    private String dtEmissaoFORM;
    private double valor;
    private double valorpago;
    private String pago;
    private int amarracao;

    public Recebimentos(int recebimentoid, Caixa caixa, Matricula mat, LocalDate dtvencimento, LocalDate dtreceb, LocalDate dtemissoa, double valor, double valorpago,int amarracao) {
        this.recebimentoid = recebimentoid;
        this.caixa = caixa;
        this.mat = mat;
        this.dtvencimento = dtvencimento;
        this.dtreceb = dtreceb;
        this.dtemissoa = dtemissoa;
        this.valor = valor;
        this.valorpago = valorpago;
        if(valorpago > 0 )
            this.pago = "Pago";
        else
            this.pago = "Não Pago";
     
        setaDatas();
        this.amarracao = amarracao;

    }

    public int getAmarracao() {
        return amarracao;
    }

    public void setAmarracao(int amarracao) {
        this.amarracao = amarracao;
    }
    
    public void setaDatas(){
        
        if (dtemissoa != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            this.dtVencimentoFORM = this.dtvencimento.format(formatter);
            if(dtreceb !=null)
             this.dtRecebFORM = this.dtreceb.format(formatter);
            
            this.dtEmissaoFORM = this.dtemissoa.format(formatter);
        }
    }
    
    public String getDtVencimentoFORM() {
        return dtVencimentoFORM;
    }

    public String getDtRecebFORM() {
        return dtRecebFORM;
    }

    public String getDtEmissaoFORM() {
        return dtEmissaoFORM;
    }

    
    public void setDtVencimentoFORM(String dtVencimentoFORM) {
        this.dtVencimentoFORM = dtVencimentoFORM;
    }

    public void setDtRecebFORM(String dtRecebFORM) {
        this.dtRecebFORM = dtRecebFORM;
    }

    public void setDtEmissaoFORM(String dtEmissaoFORM) {
        this.dtEmissaoFORM = dtEmissaoFORM;
    }

    
    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    
    public Recebimentos() {
        
        this(0,new Caixa(),new Matricula(),null,null,null,0.0,0.0,0);
        this.pago = "Não Pago";
    }

    public int getRecebimentoid() {
        return recebimentoid;
    }

    public void setRecebimentoid(int recebimentoid) {
        this.recebimentoid = recebimentoid;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public Matricula getMat() {
        return mat;
    }

    public void setMat(Matricula mat) {
        this.mat = mat;
    }

    public LocalDate getDtvencimento() {
        return dtvencimento;
    }

    public void setDtvencimento(LocalDate dtvencimento) {
        this.dtvencimento = dtvencimento;
    }

    public LocalDate getDtreceb() {
        return dtreceb;
    }

    public void setDtreceb(LocalDate dtreceb) {
        this.dtreceb = dtreceb;
    }

    public LocalDate getDtemissoa() {
        return dtemissoa;
    }

    public void setDtemissoa(LocalDate dtemissoa) {
        this.dtemissoa = dtemissoa;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValorpago() {
        return valorpago;
    }

    public void setValorpago(double valorpago) {
        this.valorpago = valorpago;
    }
    
    

   
    
}
