package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import startenglish.db.Entidades.Livro;
import startenglish.db.util.Banco;


public class DALLivro {
    
      public boolean gravar(Livro lv){
     
        String sql = "insert into livro(nome,valor,editora,volume) values('#1',#2,'#3','#4')";
        sql = sql.replaceAll("#1",lv.getNome());
        sql = sql.replaceAll("#2",""+lv.getValor());
        sql = sql.replaceAll("#3",lv.getEditora());
        sql = sql.replaceAll("#4",lv.getVolume());
        
        return Banco.getCon().manipular(sql);
    }
    
     public boolean alterar(Livro lv){
        
       String sql = "update livro set nome = '#1', valor = #2, editora = '#3', volume = '#4' where livroid = "+lv.getLivroID();
       sql = sql.replaceAll("#1",lv.getNome());
       sql = sql.replaceAll("#2",""+lv.getValor());
       sql = sql.replaceAll("#3",lv.getEditora());
       sql = sql.replaceAll("#4",lv.getVolume()); 
       
        return Banco.getCon().manipular(sql);
    }
     
    public boolean apagar(int cod){
        
        return Banco.getCon().manipular("delete from livro where livroid="+cod);
    }
    
     public Livro get(int cod){
     
        Livro livro = null;
        ResultSet rs = Banco.getCon().consultar("select * from livro where livroid="+cod);
        
        try{
            
            if(rs.next())
            {
                livro = new Livro(rs.getInt("livroid"),rs.getString("nome"),rs.getDouble("valor"),rs.getString("editora"),rs.getString("volume"));
                 
             }
        }
        catch(SQLException e ){System.out.println(e.getMessage());}
        
        return livro;
    }
    
    public List<Livro> get(String filtro){
        
       String sql="select * from livro";
       
        if(!filtro.isEmpty())
            sql+=" where "+filtro;
        
        List <Livro> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        try 
        {
            while(rs.next())
            {
                aux.add(new Livro(rs.getInt("livroid"),rs.getString("nome"),rs.getDouble("valor"),rs.getString("editora"),rs.getString("volume")));
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return aux;
    }
}
