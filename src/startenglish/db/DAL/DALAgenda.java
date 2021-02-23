package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import startenglish.db.Entidades.Agenda;
import startenglish.db.util.Banco;


public class DALAgenda {
    
    public boolean gravar(Agenda a){
        
        String sql = "insert into agendaprova(professorid,alunoid,dataprova,horainicio,horafim,local,situacaoprova,nota) values(#1,#2,'#3','#4','#5','#6','#7',#8)";
        sql = sql.replaceAll("#1", ""+a.getProfessor().getFunc().getID());
        sql = sql.replaceAll("#2", ""+a.getAluno().getID());
        sql = sql.replaceAll("#3", "" +a.getDataProva());
        sql = sql.replaceAll("#4",  a.getHoraini());
        sql = sql.replaceAll("#5", a.getHorafim());
        sql = sql.replaceAll("#6",  a.getLocal());
        sql = sql.replaceAll("#7", "" + a.getSituacao_prova());
        sql = sql.replaceAll("#8", "" + a.getNota());
        
        return Banco.getCon().manipular(sql);
    }
  
    public ArrayList<Agenda> get(String filtro) {

       String sql="select * from agendaprova";
             
        if(!filtro.isEmpty())
            sql+=" "+filtro;
        
        ArrayList <Agenda> aux = new ArrayList();
        ResultSet rs = Banco.getCon().consultar(sql);
        DALProfessor dalp = new DALProfessor();
        DALAluno dala = new DALAluno();
        
        try 
        {
            if(rs != null){
                 while(rs.next())
                {
                    aux.add( new Agenda(rs.getInt("agendaid"), dalp.get(rs.getInt("professorid")) , dala.get(rs.getInt("alunoid")), 
                            rs.getDate("dataprova").toLocalDate(), rs.getString("horainicio"), rs.getString("horafim"),rs.getString("local"), rs.getString("situacaoprova").charAt(0), rs.getDouble("nota")));
                }
            }
           
        } 
        catch (SQLException ex) 
        {
            System.out.println(ex.getMessage());
        }
        
        return aux;
    }
    
     public boolean InserirTudo(List<Agenda> Agendas) {

        boolean ok = true;

        try {

            Banco.getCon().getConnect().setAutoCommit(false);
          
            if(!get("").isEmpty())
                 ok = Banco.getCon().manipular("delete from agendaprova");

            if (ok) {

                for (int i = 0; i < Agendas.size() && ok; i++) {

                    ok = gravar(Agendas.get(i));
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
