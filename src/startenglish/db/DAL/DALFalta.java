package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import startenglish.db.Entidades.Falta;
import startenglish.db.Entidades.Funcionario;
import startenglish.db.util.Banco;


public class DALFalta {
     public boolean gravar(List<Falta> lista) throws SQLException{
         
         boolean ok = false;
         
          try
        {
            Banco.getCon().getConnect().setAutoCommit(false);//commit manual
                
                    for (int i = 0; i < lista.size(); i++) {
                        String sql = "insert into falta(matriculaid, turmaid, data, presenca) values(#1,#2,'#3','A')";
                        sql = sql.replaceAll("#1", "" + lista.get(i).getMatricula().getNummat());
                        sql = sql.replaceAll("#2", "" + lista.get(i).getMatricula().getTurmaID());
                        sql = sql.replaceAll("#3", "" + lista.get(i).getData());  
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
    
    public boolean alterar(List<Falta> lista) throws SQLException{
        
       boolean ok = false;
         
          try
        {
            Banco.getCon().getConnect().setAutoCommit(false);//commit manual
                
                    String sql = "delete from falta where turmaid = "+lista.get(0).getTurma().getTurmaID()+" and data = '"+lista.get(0).getData()+"'";
                    ok = Banco.getCon().manipular(sql);
                    
                    if(ok)
                    {
                        for (int i = 0; i < lista.size(); i++) 
                        {
                            sql = "insert into falta(matriculaid, turmaid, data, presenca) values(#1,#2,'#3','A')";
                            sql = sql.replaceAll("#1", "" + lista.get(i).getMatricula().getNummat());
                            sql = sql.replaceAll("#2", "" + lista.get(i).getMatricula().getTurmaID());
                            sql = sql.replaceAll("#3", "" + lista.get(i).getData());  
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
    
    public boolean apagar(Funcionario f){
        
        return Banco.getCon().manipular("delete from funcionario where funcid="+f.getID());
    }
    
//    public Funcionario get(int codTurma, Date data){
//     
//        Funcionario func = null;
//        ResultSet rs = Banco.getCon().consultar("select * from falta where turmaid = "+codTurma+" and data = '"+data+"'");
//        
//        try{
//            
//            if(rs.next())
//            {
//                
//                                       
//                DALEndereco dale = new DALEndereco();
//                func = new Funcionario(rs.getInt("funcid"), rs.getString("nome"), rs.getString("cpf"), rs.getString("rg"), rs.getString("email"), rs.getString("fone"), dale.get(rs.getInt("enderecoid")));
//                 
//             }
//        }
//        catch(SQLException e ){System.out.println(e.getMessage());}
//        
//        return func;
//    }
    
    public List<Falta> get(int codTurma, Date data){
        
       String sql="select * from falta where turmaid = "+codTurma+" and data = '"+data+"'";
       
        
        
        List <Falta> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        try 
        {
            while(rs.next())
            {
                DALTurma dalt = new DALTurma();
                
                //aux.add(new Falta(dalt.get(rs.getInt("turmaid")), , data, ));
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return aux;
    }
}
