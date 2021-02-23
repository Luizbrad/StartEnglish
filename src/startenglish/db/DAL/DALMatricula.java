package startenglish.db.DAL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import startenglish.db.Entidades.Aluno;
import startenglish.db.Entidades.Livro;
import startenglish.db.Entidades.Matricula;
import startenglish.db.Entidades.Turma;
import startenglish.db.util.Banco;

public class DALMatricula {

    public boolean gravar(Matricula mat){
    
        String sql = "insert into matricula(livroid,turmaid,alunoid,ativo,desconto,informacoescancelamento,instituiensino,valor,nomeresponsavel,nivel)"
                + " values(#1,#2,#3,'#4',#5,'#6','#7',#8,'#9','#zz')";
        
        sql = sql.replaceAll("#1", ""+mat.getLivro().getLivroID());
        sql = sql.replaceAll("#2", ""+mat.getTurmaID().getTurmaID());
        sql = sql.replaceAll("#3", ""+mat.getAluno().getID());
        sql = sql.replaceAll("#4", ""+mat.getAtivo());
        sql = sql.replaceAll("#5", ""+mat.getDesconto());
        sql = sql.replaceAll("#6", mat.getInfoCancelamento());
        sql = sql.replaceAll("#7", mat.getInstuiEnsino());
        sql = sql.replaceAll("#8", ""+mat.getValor());
        sql = sql.replaceAll("#9", mat.getNomeResponsável());
        sql = sql.replaceAll("#zz",mat.getNivel());
        
        return Banco.getCon().manipular(sql);
    }
    
    public boolean apagar(int cod){
        
        String sql = "delete from matricula where numeromatricula = "+cod;
        return Banco.getCon().manipular(sql);
    }
    
    public boolean atualizar(Matricula mat){
        
        String sql = "update matricula set livroid = #1,turmaid = #2, alunoid = #3,ativo = '#4',desconto = #5, informacoescancelamento = '#6',"
                + "instituiensino = '#7',valor = #8,nomeresponsavel = '#9',nivel = '#zz' where numeromatricula = "+mat.getNummat();
        
        sql = sql.replaceAll("#1", ""+mat.getLivro().getLivroID());
        sql = sql.replaceAll("#2", ""+mat.getTurmaID().getTurmaID());
        sql = sql.replaceAll("#3", ""+mat.getAluno().getID());
        sql = sql.replaceAll("#4", ""+mat.getAtivo());
        sql = sql.replaceAll("#5", ""+mat.getDesconto());
        sql = sql.replaceAll("#6", mat.getInfoCancelamento());
        sql = sql.replaceAll("#7", mat.getInstuiEnsino());
        sql = sql.replaceAll("#8", ""+mat.getValor());
        sql = sql.replaceAll("#9", mat.getNomeResponsável());
        sql = sql.replaceAll("#zz",mat.getNivel());
        
        return Banco.getCon().manipular(sql);
    }
    
    public Matricula get(int cod) {
        String sql = "select * from matricula where numeromatricula = " + cod;

        Matricula aux = null;
        ResultSet rs = Banco.getCon().consultar(sql);
        Turma tu;
        Aluno alu;
        Livro livro;
        DALTurma dalt = new DALTurma();
        DALLivro dall = new DALLivro();
        DALAluno dala = new DALAluno();

        try {
            if (rs.next()) {

                alu = dala.get(rs.getInt("alunoid"));
                livro = dall.get(rs.getInt("livroid"));
                tu =  dalt.get(rs.getInt("turmaid"));
                aux = new Matricula(rs.getInt("numeromatricula"), livro,tu, alu, rs.getString("ativo").charAt(0), rs.getDouble("valor"),
                        rs.getString("instituiensino"),rs.getString("informacoescancelamento"),rs.getInt("desconto"),rs.getString("nomeresponsavel"),rs.getString("nivel"));
                
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return aux;
    }
    
    public ArrayList<Matricula> get(String filtro) {
        
        String sql;
        sql = "select * from matricula";

        if (!filtro.isEmpty()) 
            sql += " " + filtro;
        
        ArrayList<Matricula> mats = new ArrayList();
        
        Matricula aux = null;
        ResultSet rs = Banco.getCon().consultar(sql);
        Turma tu;
        Aluno alu;
        Livro livro;
        DALTurma dalt = new DALTurma();
        DALLivro dall = new DALLivro();
        DALAluno dala = new DALAluno();

        try {
            while (rs.next()) {

                alu = dala.get(rs.getInt("alunoid"));
                livro = dall.get(rs.getInt("livroid"));
                tu =  dalt.get(rs.getInt("turmaid"));
                aux = new Matricula(rs.getInt("numeromatricula"), livro,tu, alu, rs.getString("ativo").charAt(0), rs.getDouble("valor"),
                        rs.getString("instituiensino"),rs.getString("informacoescancelamento"),rs.getInt("desconto"),rs.getString("nomeresponsavel"),rs.getString("nivel"));
                
                mats.add(aux);
                
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return mats;
    }

}
