
package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import startenglish.db.Entidades.Cursos;
import startenglish.db.Entidades.Professor;
import startenglish.db.Entidades.Turma;
import startenglish.db.util.Banco;

public class DALTurma
{
    public boolean gravar(Turma tu)
    {    
        String sql = "insert into turma(nome,professorid,cursoid,horario,ano,ativo,semana,qtd_vagas) values('#1',#2,#3,'#4','#5','#6','#7',#8)";
        sql = sql.replaceAll("#1",tu.getNome());
        sql = sql.replaceAll("#2",""+tu.getProf().getFunc().getID());
        sql = sql.replaceAll("#3",""+tu.getCur().getCursoID());
        sql = sql.replaceAll("#4",tu.getHorario());
        sql = sql.replaceAll("#5",""+tu.getAno());
        sql = sql.replaceAll("#6",""+tu.getAtivo());
        sql = sql.replaceAll("#7",tu.getSemana());
        sql = sql.replaceAll("#8",""+tu.getQtdvagas());
        
        return Banco.getCon().manipular(sql);
    }
    
    public boolean subqtd(Turma tu)
    {
        String sql="update turma set qtd_vagas='"+(tu.getQtdvagas()-1)+"' where turmaid= "+tu.getTurmaID();
        return Banco.getCon().manipular(sql);
    }
    
    public boolean addqtd(int id,int qtdvag)
    {
        String sql="update turma set qtd_vagas='"+(qtdvag+1)+"' where turmaid="+id;
        return Banco.getCon().manipular(sql);
    }
    
     public boolean alterar(Turma tu)
     {
        
       String sql = "update turma set nome = '#1', professorid = #2, cursoid = #3, horario = '#4', ano ='#5', ativo = '#6',"
               + "semana = '#7',qtd_vagas = '#8' where turmaid = "+tu.getTurmaID();
        sql = sql.replaceAll("#1",tu.getNome());
        sql = sql.replaceAll("#2",""+tu.getProf().getFunc().getID());
        sql = sql.replaceAll("#3",""+tu.getCur().getCursoID());
        sql = sql.replaceAll("#4",tu.getHorario());
        sql = sql.replaceAll("#5",""+tu.getAno());
        sql = sql.replaceAll("#6",""+tu.getAtivo());
        sql = sql.replaceAll("#7",tu.getSemana());
        sql = sql.replaceAll("#8",""+tu.getQtdvagas());
       
        return Banco.getCon().manipular(sql);
    }
     
    public boolean apagar(int cod)
    {
        
        return Banco.getCon().manipular("delete from turma where turmaid="+cod);
    }
    
     public Turma get(int cod)
     {
     
        Turma turma = null;
        ResultSet rs = Banco.getCon().consultar("select * from turma where turmaid="+cod);
        DALProfessor prof= new DALProfessor();
        DALCurso cur = new DALCurso();
        Professor pro;
        Cursos curs;
        try{
            
            if(rs.next())
            {            
                pro = prof.get(rs.getInt("professorid"));
                curs = cur.get(rs.getInt("cursoid"));
                turma = new Turma(rs.getInt("turmaid"),rs.getString("nome"),pro,curs,
                rs.getString("horario"),rs.getDate("ano").toLocalDate(),rs.getString("ativo").charAt(0),rs.getString("semana"),rs.getInt("qtd_vagas"));
             }
        }
        catch(SQLException e ){System.out.println(e.getMessage());}
        
        return turma;
    }
    
    public List<Turma> get(String filtro)
    {
        
       String sql="select * from turma";
       
        if(!filtro.isEmpty())
            sql+=" where "+filtro;
        DALProfessor prof= new DALProfessor();
        DALCurso cur = new DALCurso();
        List <Turma> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        try 
        {
            if(rs!=null)
                while(rs.next())
                {
                    Cursos curs = new Cursos();
                    curs=cur.get(rs.getInt("cursoid"));
                    aux.add(new Turma(rs.getInt("turmaid"),rs.getString("nome"),prof.get(rs.getInt("professorid")),curs,
                    rs.getString("horario"),rs.getDate("ano").toLocalDate(),rs.getString("ativo").charAt(0),rs.getString("semana"),rs.getInt("qtd_vagas")));
                }
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return aux;
    }
}
