package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import startenglish.db.Entidades.Funcionario;
import startenglish.db.util.Banco;

public class DALFuncionario {
    public boolean gravar(Funcionario f){
        
        String sql = "insert into funcionario(nome,cpf,rg,email,fone,enderecoid) values('#1','#2','#3','#4','#5',#6)";
        sql = sql.replaceAll("#1", ""+f.getNome());
        sql = sql.replaceAll("#2", ""+f.getCpf());
        sql = sql.replaceAll("#3", ""+f.getRg());
        sql = sql.replaceAll("#4", ""+f.getEmail());
        sql = sql.replaceAll("#5", ""+f.getFone());
        sql = sql.replaceAll("#6",""+f.getEndereco().getEnderecoID());
        
        return Banco.getCon().manipular(sql);
        
    }
    
    public boolean alterar(Funcionario f){
        
       String sql = "update funcionario set nome = '#1', cpf = '#2', rg='#3', email= '#4', fone='#5', enderecoid=#6 where funcid = "+f.getID();
        sql = sql.replaceAll("#1", ""+f.getNome());
        sql = sql.replaceAll("#2", ""+f.getCpf());
        sql = sql.replaceAll("#3", ""+f.getRg());
        sql = sql.replaceAll("#4", ""+f.getEmail());
        sql = sql.replaceAll("#5", ""+f.getFone());
        sql = sql.replaceAll("#6",""+f.getEndereco().getEnderecoID());
        
        return Banco.getCon().manipular(sql);
    }
    
    public boolean apagar(Funcionario f){
        
        return Banco.getCon().manipular("delete from funcionario where funcid="+f.getID());
    }
    
    public Funcionario get(int cod){
     
        Funcionario func = null;
        ResultSet rs = Banco.getCon().consultar("select * from funcionario where funcid="+cod);
        
        try{
            
            if(rs.next())
            {
                
                                       
                DALEndereco dale = new DALEndereco();
                func = new Funcionario(rs.getInt("funcid"), rs.getString("nome"), rs.getString("cpf"), rs.getString("rg"), rs.getString("email"), rs.getString("fone"), dale.get(rs.getInt("enderecoid")));
                 
             }
        }
        catch(SQLException e ){System.out.println(e.getMessage());}
        
        return func;
    }
    
    public List<Funcionario> get(String filtro){
        
       String sql="select * from funcionario";
       
        if(!filtro.isEmpty())
            sql+=" where "+filtro;
        
        List <Funcionario> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        try 
        {
            while(rs.next())
            {
                DALEndereco dale = new DALEndereco();
                aux.add(new Funcionario(rs.getInt("funcid"), rs.getString("nome"), rs.getString("cpf"), rs.getString("rg"), rs.getString("email"), rs.getString("fone"), dale.get(rs.getInt("enderecoid"))));
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return aux;
    }
}
