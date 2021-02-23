package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import startenglish.db.Entidades.Aluno;
import startenglish.db.util.Banco;


public class DALAluno {
    
     public boolean gravar(Aluno a){
        
        String sql = "insert into aluno(enderecoid,rg,cpf,email,nome,fone) values(#1,'#2','#3','#4','#5','#6')";
        sql = sql.replaceAll("#1", ""+a.getEndereco().getEnderecoID());
        sql = sql.replaceAll("#2", ""+a.getRg());
        sql = sql.replaceAll("#3", ""+a.getCpf());
        sql = sql.replaceAll("#4", ""+a.getEmail());
        sql = sql.replaceAll("#5", ""+a.getNome());
        sql = sql.replaceAll("#6", ""+a.getFone());
              
        return Banco.getCon().manipular(sql);
        
    }
    
    public boolean alterar(Aluno a){
        
       String sql = "update aluno set enderecoid = #1, rg = '#2', cpf='#3', email= '#4', nome='#5', fone='#6' where alunoid = "+a.getID();
        sql = sql.replaceAll("#1", ""+a.getEndereco().getEnderecoID());
        sql = sql.replaceAll("#2", ""+a.getRg());
        sql = sql.replaceAll("#3", ""+a.getCpf());
        sql = sql.replaceAll("#4", ""+a.getEmail());
        sql = sql.replaceAll("#5", ""+a.getNome());
        sql = sql.replaceAll("#6", ""+a.getFone());
        
        return Banco.getCon().manipular(sql);
    }
    
    public boolean apagar(Aluno a){
        
        return Banco.getCon().manipular("delete from aluno where alunoid="+a.getID());
    }
    
    public Aluno get(int cod){
     
        Aluno aluno = null;
        ResultSet rs = Banco.getCon().consultar("select * from aluno where alunoid="+cod);
        
        try{
            
            if(rs.next())
            {
                                       
                DALEndereco dale = new DALEndereco();
                aluno = new Aluno(rs.getInt("alunoid"), dale.get(rs.getInt("enderecoid")), rs.getString("rg"), rs.getString("cpf"), rs.getString("email"), rs.getString("nome"), rs.getString("fone"));
                 
             }
        }
        catch(SQLException e ){System.out.println(e.getMessage());}
        
        return aluno;
    }
    
    public List<Aluno> get(String filtro){
        
       String sql="select * from aluno";
       
        if(!filtro.isEmpty())
            sql+=" where "+filtro;
        
        List <Aluno> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        try 
        {
            while(rs.next())
            {
                DALEndereco dale = new DALEndereco();
                aux.add(new Aluno(rs.getInt("alunoid"), dale.get(rs.getInt("enderecoid")), rs.getString("rg"), rs.getString("cpf"), rs.getString("email"), rs.getString("nome"), rs.getString("fone")));
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return aux;
    }
}
