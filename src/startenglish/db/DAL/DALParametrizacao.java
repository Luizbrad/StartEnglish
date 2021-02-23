package startenglish.db.DAL;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import startenglish.db.Entidades.Parametrizacao;
import startenglish.db.util.Banco;


public class DALParametrizacao {
    
    public boolean gravar(Parametrizacao p){
        
        String sql = "insert into parametrizacao(nome,telefone,razaosocial,email,enderecoid,cnpj) values('#1','#2','#3','#4',#5,'#6')";
        sql = sql.replaceAll("#1",p.getNome());
        sql = sql.replaceAll("#2",p.getTelefone());
        sql = sql.replaceAll("#3",p.getRazaoSocial());
        sql = sql.replaceAll("#4",p.getEmail());
        sql = sql.replaceAll("#5",""+p.getEndereco().getEnderecoID());
        sql = sql.replaceAll("#6",p.getCNPJ());    
        
        return Banco.getCon().manipular(sql);
        
    }
    
    public boolean alterar(Parametrizacao p,String nome){
        
        String sql = "update parametrizacao set nome = '#1', telefone = '#2', razaosocial='#3', email= '#4', enderecoid=#5, cnpj='#6' where nome = '"+nome+"'";
        sql = sql.replaceAll("#1",p.getNome());
        sql = sql.replaceAll("#2",p.getTelefone());
        sql = sql.replaceAll("#3",p.getRazaoSocial());
        sql = sql.replaceAll("#4",p.getEmail());
        sql = sql.replaceAll("#5",""+p.getEndereco().getEnderecoID());
        sql = sql.replaceAll("#6",p.getCNPJ());
        
        return Banco.getCon().manipular(sql);
    }
    
    public boolean apagar(Parametrizacao p){
        
        return Banco.getCon().manipular("delete from parametrizacao where nome = '"+p.getNome()+"'");
    }
    
    public Parametrizacao get(){
        
        Parametrizacao paramet = null;
        ResultSet rs = Banco.getCon().consultar("select nome,telefone,razaosocial,email,enderecoid,cnpj from parametrizacao");
        
        DALEndereco dal = new DALEndereco();
        
        try{
            
            if(rs.next())
            {
                paramet = new Parametrizacao(rs.getString("nome"),rs.getString("telefone"),rs.getString("email"),rs.getString("razaosocial"),
                        dal.get(rs.getInt("EnderecoID")),rs.getString("cnpj"));
                 
             }
        }
        catch(SQLException e ){System.out.println(e.getMessage());}
        
        return paramet;
    }
       
    
    public boolean gravarFoto(String nome,File arq) throws FileNotFoundException{
        
         FileInputStream imagem = null;
         
        if(arq != null){
             imagem = new FileInputStream(arq);
            try
            {
                PreparedStatement ps = Banco.getCon().getConnect().
                prepareStatement("UPDATE parametrizacao set logotipo = ? where nome= ?");
                ps.setBinaryStream(1, imagem);
                ps.setString(2, nome);
                ps.executeUpdate();
                ps.close();
                imagem.close();
            }
            catch(Exception e)
            {
                return false;
            }
        }
        else{
             try
            {
                PreparedStatement ps = Banco.getCon().getConnect().
                prepareStatement("UPDATE parametrizacao set logotipo = null where nome = ?");
                ps.setString(1, nome);
                ps.executeUpdate();
                ps.close();
                imagem.close();
            }
            catch(Exception e)
            {
                return false;
            }
            
        }
        
        return true;
    }
    
    public InputStream getFoto(String nome){
         
        InputStream is=null;
        
        try
        {
            PreparedStatement ps = Banco.getCon().getConnect().
            prepareStatement("SELECT logotipo from parametrizacao where nome=?");
            ps.setString(1,nome);
            
            ResultSet rs=ps.executeQuery();
            
            if(rs.next())
            {
                byte[] bytes=rs.getBytes("logotipo");
                is=new ByteArrayInputStream(bytes);
            }
            ps.close();
        }
        catch(Exception e)
        {
            return null;
        }
        
        return is;
     }
}
