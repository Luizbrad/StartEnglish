package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import startenglish.db.Entidades.Caixa;
import startenglish.db.Entidades.Matricula;
import startenglish.db.Entidades.Recebimentos;
import startenglish.db.util.Banco;


public class DALRecebimento {
    
    public int maxAmarracao() {

        String sql = "select max(amarracao) from recebimentos";
        ResultSet rs = Banco.getCon().consultar(sql);
        int amarracaoMax = 0;
        
        try 
        {
            if(rs != null){
                amarracaoMax = rs.getInt("amarracao");
            }
            
        }catch(SQLException ex){System.out.println(ex.getMessage());};

        return amarracaoMax;
    }
    
    public boolean inserir(Recebimentos r,boolean tabelaMat){
        
        String sql;
        if(r.getDtreceb() != null)
         sql = "insert into recebimentos(matriculaid,caixaid,dtvencimento,dtreceb,dtemissao,valor,valorreceb,amarracao)"
                + " values(#1,#2,'#3','#4','#5',#6,#7,#8)";
        else
            sql = "insert into recebimentos(matriculaid,caixaid,dtvencimento,dtreceb,dtemissao,valor,valorreceb,amarracao)"
                + " values(#1,#2,'#3',#4,'#5',#6,#7,#8)";
        
        if(tabelaMat)
         r.setAmarracao(maxAmarracao()+1);
        
        sql = sql.replaceAll("#1",""+r.getMat().getNummat());
        sql = sql.replaceAll("#2",""+r.getCaixa().getCaixaid());
        sql = sql.replaceAll("#3",""+r.getDtvencimento());
        sql = sql.replaceAll("#4",""+r.getDtreceb());
        sql = sql.replaceAll("#5",""+r.getDtemissoa());
        sql = sql.replaceAll("#6",""+r.getValor());
        sql = sql.replaceAll("#7",""+r.getValorpago());
        sql = sql.replaceAll("#8", ""+r.getAmarracao());
        return Banco.getCon().manipular(sql);
    }
    
     public ArrayList<Recebimentos> get(String filtro) {

       String sql="select * from recebimentos";
             
        if(!filtro.isEmpty())
            sql+=" "+filtro;
        
        ArrayList <Recebimentos> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        DALMatricula dalm = new DALMatricula();
        Matricula mat;
        
        try 
        {
            if(rs != null){
                 while(rs.next())
                {
                    mat = dalm.get(rs.getInt("matriculaid"));
                    
                    if(rs.getDate("dtreceb") == null)
                        aux.add(
                        new Recebimentos(rs.getInt("recebimentoid"), new Caixa(rs.getInt("caixaid")),mat , 
                                rs.getDate("dtvencimento").toLocalDate(), null , rs.getDate("dtemissao").toLocalDate(),
                                rs.getDouble("valor"), rs.getDouble("valorreceb"),rs.getInt("amarracao")));
                    else
                        aux.add(
                        new Recebimentos(rs.getInt("recebimentoid"), new Caixa(rs.getInt("caixaid")),mat , 
                                rs.getDate("dtvencimento").toLocalDate(), rs.getDate("dtreceb").toLocalDate() , rs.getDate("dtemissao").toLocalDate(),
                                rs.getDouble("valor"), rs.getDouble("valorreceb"),rs.getInt("amarracao")));
                }
            }
           
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return aux;
    }
     
       public boolean InserirTudo(List<Recebimentos> Recebimentos) {

        boolean ok = true;

        try {

            Banco.getCon().getConnect().setAutoCommit(false);
          
            if(!get("").isEmpty())
                 ok = Banco.getCon().manipular("delete from recebimentos");

            if (ok) {

                for (int i = 0; i < Recebimentos.size() && ok; i++) {

                    ok = inserir(Recebimentos.get(i),false);
                }
            }

            if (ok) {
                Banco.getCon().getConnect().commit();
            } else {
                Banco.getCon().getConnect().rollback();
        
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            ok = false;
        }

        return ok;
    }
}
