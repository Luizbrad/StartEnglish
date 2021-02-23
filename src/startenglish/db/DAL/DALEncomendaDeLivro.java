package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import startenglish.db.Entidades.EncomendaDeLivros;
import startenglish.db.Entidades.ItemEncomenda;
import startenglish.db.Entidades.Livro;
import startenglish.db.util.Banco;

public class DALEncomendaDeLivro {
    
    public List<ItemEncomenda> BuscaItens(int cod)
    {
        String sql="select * from itensencomenda where encomendaid ="+cod;
        
        List <ItemEncomenda> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        try 
        {
            while(rs.next())
            {
                                
                DALLivro dall = new DALLivro();
                Livro livaux = dall.get(rs.getInt("livroid"));
                String auxiliarconta;
                double valor = 0;
                DecimalFormat df = new DecimalFormat("#.##");
                auxiliarconta = df.format(rs.getInt("quantidade")*livaux.getValor());
                auxiliarconta = auxiliarconta.replace(',', '.');
                try {
                   valor = Double.parseDouble(auxiliarconta); 
                } catch (Exception e) {
                }
                
                aux.add(new ItemEncomenda(dall.get(rs.getInt("livroid")), rs.getInt("quantidade"), valor));
            }
        } 
        catch (SQLException ex) 
        {
            
        }
        return aux;
    }
    
    
    public List<EncomendaDeLivros> BuscaEncomendas(String filtro){
        
        String sql="select * from encomendalivros";
        if(!filtro.isEmpty())
            sql+=" where "+filtro;
        List <EncomendaDeLivros> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        try 
        {
            while(rs.next())
            {
                DALFuncionario auxFunc = new DALFuncionario();
                
                aux.add(new EncomendaDeLivros(rs.getInt("encomendaid"), auxFunc.get(rs.getInt("funcid")), 
                        rs.getDate("dataencomenda").toLocalDate(), rs.getString("fornecedor"), rs.getDouble("valortotal"),
                        rs.getDate("previsaoentrega").toLocalDate()));
            }
        } 
        catch (SQLException ex) 
        {
            
        }
        return aux;
    }
    
    public boolean GravarEncomenda(EncomendaDeLivros e)
    {   
        String sql = "insert into encomendalivros(funcid, dataencomenda, fornecedor, valortotal, previsaoentrega) "
                + "values(#2,'#3','#4',#5,'#6')";
        
        

        sql = sql.replace("#2", ""+e.getFunc().getID());
        //System.out.println(c.getNum());

        sql = sql.replace("#3", ""+e.getDataEncomenda());
        //System.out.println(c.getNome());

        sql = sql.replace("#4", e.getFornecedor());
        //System.out.println(c.getData());

        sql = sql.replace("#5",""+e.getValor());
        //System.out.println(c.getDesc());

        sql = sql.replace("#6", ""+e.getPrevisaoEntrega());
       // System.out.println(c.getValor());

        
        return Banco.getCon().manipular(sql);
    }
    
    public boolean GravarItens(EncomendaDeLivros lista) throws SQLException
    {
         boolean ok = false;
         
          try
        {
            Banco.getCon().getConnect().setAutoCommit(false);//commit manual
                
                    for (int i = 0; i < lista.getItens().size(); i++) 
                    {
                        String sql = "insert into itensencomenda(encomendaid, livroid, quantidade) values (#1,#2,#3)";
                        sql = sql.replaceAll("#1", ""+lista.getCodigoEnc());
                        sql = sql.replaceAll("#2", ""+lista.getItens().get(i).getLivro().getLivroID());
                        sql = sql.replaceAll("#3", ""+lista.getItens().get(i).getQuantidade());

                        ok = Banco.getCon().manipular(sql);
                    }
                
                
        }
        catch(Exception e)
        {   ok=false; }
        if(ok)
          Banco.getCon().getConnect().commit();
        else
         Banco.getCon().getConnect().rollback();
         
        Banco.getCon().getConnect().setAutoCommit(true);
        
        return ok;
        
    }
    
    public boolean alterarEncomenda(EncomendaDeLivros e)
    {   
        String sql = "update encomendalivros set funcid = #1, dataencomenda = '#2', fornecedor = '#3',"
                + "                         valortotal = #4, previsaoentrega = '#5' where encomendaid = '#6'";
        
//        System.out.println(c.getCod());

        sql = sql.replace("#1", ""+e.getFunc().getID());
//        System.out.println(c.getNum());

        sql = sql.replace("#2",""+e.getDataEncomenda());
//        System.out.println(c.getNome());

        sql = sql.replace("#3", e.getFornecedor());
//        System.out.println(c.getData());

        sql = sql.replace("#4",""+e.getValor());
//        System.out.println(c.getDesc());

        sql = sql.replace("#5", ""+e.getPrevisaoEntrega());
//        System.out.println(c.getValor());
        
        sql = sql.replace("#6", ""+e.getCodigoEnc());
//        System.out.println(c.getCod());
        
        return Banco.getCon().manipular(sql);
    }
    
    public boolean AtualizarValor(int cod, double valor)
    {
         String sql = "update encomendalivros set valortotal = #1 where encomendaid =" +cod;
        
         sql = sql.replaceAll("#1", ""+valor);
         
        return Banco.getCon().manipular(sql);
    }
    
    public boolean AlterarItens(EncomendaDeLivros lista) throws SQLException
    {
        boolean ok = false;
         
          try
        {
            Banco.getCon().getConnect().setAutoCommit(false);//commit manual
                
                    String sql = "delete from itensencomenda where encomendaid = "+lista.getCodigoEnc();
                    ok = Banco.getCon().manipular(sql);
                    
                    if(ok)
                    {
                        for (int i = 0; i < lista.getItens().size(); i++) 
                        {
                            sql = "insert into itensencomenda (encomendaid, livroid, quantidade) values (#1,#2,#3)";
                            sql = sql.replaceAll("#1", ""+lista.getCodigoEnc());
                            sql = sql.replaceAll("#2", ""+lista.getItens().get(i).getLivro().getLivroID());
                            sql = sql.replaceAll("#3", ""+lista.getItens().get(i).getQuantidade());

                            ok = Banco.getCon().manipular(sql);
                        }
                    }       
        }
        catch(Exception e)
        {   ok=false; }
        if(ok)
          Banco.getCon().getConnect().commit();
        else
         Banco.getCon().getConnect().rollback();
         
        Banco.getCon().getConnect().setAutoCommit(true);
        
        return ok;
    }
    
    public int getMaxPK()
    {
        return Banco.getCon().getMaxPK("encomendalivros", "encomendaid");
    }
}
