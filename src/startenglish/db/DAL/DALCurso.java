package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import startenglish.db.Entidades.Cursos;
import startenglish.db.util.Banco;


public class DALCurso {
 
    public boolean gravar(Cursos c){
     
        String sql;
        
        if(c.getData_encerramento() == null)
         sql = "insert into curso(nomecurso,descricao,preco,etapa,datalancamento,dataencerramento) values('#1','#2',#3,'#4','#5',#6)";
        else
          sql = "insert into curso(nomecurso,descricao,preco,etapa,datalancamento,dataencerramento) values('#1','#2',#3,'#4','#5','#6')"; 
            
        sql = sql.replaceAll("#1",c.getNomeCurso());
        sql = sql.replaceAll("#2",c.getDescricao());
        sql = sql.replaceAll("#3",""+c.getPreco());
        sql = sql.replaceAll("#4",c.getEtapa());
        sql = sql.replaceAll("#5",""+c.getData_lancamento());
        sql = sql.replaceAll("#6",""+c.getData_encerramento());
        
        return Banco.getCon().manipular(sql);
    }
    
     public boolean alterar(Cursos c){
        
           String sql;
        if(c.getData_encerramento() == null)
              sql = "update curso set nomecurso = '#1', descricao='#2', preco = #3, etapa = '#4', datalancamento = '#5', dataencerramento = #6 where cursoid = "+c.getCursoID();
        else
            sql = "update curso set nomecurso = '#1', descricao='#2', preco = #3, etapa = '#4', datalancamento = '#5', dataencerramento = '#6' where cursoid = "+c.getCursoID();

            
       sql = sql.replaceAll("#1",c.getNomeCurso());
       sql = sql.replaceAll("#2",c.getDescricao());
       sql = sql.replaceAll("#3",""+c.getPreco());
       sql = sql.replaceAll("#4",c.getEtapa());
       sql = sql.replaceAll("#5",""+c.getData_lancamento());
       sql = sql.replaceAll("#6",""+c.getData_encerramento());
        
        return Banco.getCon().manipular(sql);
    }
     
    public boolean apagar(int cod){
        
        return Banco.getCon().manipular("delete from curso where cursoid="+cod);
    }
    
     public Cursos get(int cod){
     
        Cursos curso = null;
        ResultSet rs = Banco.getCon().consultar("select * from curso where cursoid="+cod);
        
        try{
            
            if(rs.next())
            {
                  if(rs.getDate("dataencerramento") == null)
                     curso = new Cursos(rs.getInt("cursoid"),rs.getString("etapa"), rs.getDate("datalancamento").toLocalDate(), null, 
                        rs.getString("nomecurso"), rs.getString("descricao"), rs.getDouble("preco"));
                else
                     curso = new Cursos(rs.getInt("cursoid"),rs.getString("etapa"), rs.getDate("datalancamento").toLocalDate(), rs.getDate("dataencerramento").toLocalDate(), 
                        rs.getString("nomecurso"), rs.getString("descricao"), rs.getDouble("preco"));
              
                 
             }
        }
        catch(SQLException e ){System.out.println(e.getMessage());}
        
        return curso;
    }
    
    public List<Cursos> get(String filtro){
        
       String sql="select *from curso";
             
        if(!filtro.isEmpty())
            sql+=" where "+filtro;
        
        List <Cursos> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        try 
        {
            while(rs.next())
            {
                           
                if(rs.getDate("dataencerramento") == null)
                    aux.add(new Cursos(rs.getInt("cursoid"),rs.getString("etapa"),rs.getDate("datalancamento").toLocalDate() ,null, 
                        rs.getString("nomecurso"), rs.getString("descricao"), rs.getDouble("preco")));
                else
                    aux.add(new Cursos(rs.getInt("cursoid"),rs.getString("etapa"),rs.getDate("datalancamento").toLocalDate() ,rs.getDate("dataencerramento").toLocalDate(), 
                        rs.getString("nomecurso"), rs.getString("descricao"), rs.getDouble("preco"))); 
            }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return aux;
    }
}
