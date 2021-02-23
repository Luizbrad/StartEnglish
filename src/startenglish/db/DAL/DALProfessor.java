package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import startenglish.db.Entidades.Professor;
import startenglish.db.util.Banco;

public class DALProfessor {
     public boolean gravar(Professor p){
        
        String sql = "insert into professor(funcid) values(#1)";
        sql = sql.replaceAll("#1", ""+p.getFunc().getID());
                
        return Banco.getCon().manipular(sql);
        
    }
       
    public boolean apagar(int p){
        
        return Banco.getCon().manipular("delete from professor where funcid="+p);
    }
    
    public Professor get(int cod){
     
        Professor prof = null;
        ResultSet rs = Banco.getCon().consultar("select * from professor where funcid="+cod);
        
        try{
            
            if(rs.next())
            {
                
                        
                DALFuncionario dale = new DALFuncionario();
                prof = new Professor(dale.get(rs.getInt("funcid")));
                 
             }
        }
        catch(SQLException e ){System.out.println(e.getMessage());}
        
        return prof;
    }
    
    public List<Professor> get(String filtro){
        
       String sql="select *from professor";
       
        if(!filtro.isEmpty())
            sql+=" where "+filtro;
        
        List <Professor> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        try 
        {
            while(rs.next())
            {
                DALFuncionario dale = new DALFuncionario();
                aux.add(new Professor(dale.get(rs.getInt("funcid"))));
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return aux;
    }
}
