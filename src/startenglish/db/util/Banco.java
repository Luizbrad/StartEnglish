package startenglish.db.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class Banco {
   
     static private Conexao con = null;
    
    static public Conexao getCon()
    {
        return con;
    }

    private Banco() {} // construtor privado, ou seja, nao pode dar um new em banco, da uma segurança maior no projeto
    
    static public boolean conectar()
    {
        con = new Conexao(); 
        return con.conectar("jdbc:postgresql://localhost/","StartEnglish" ,"postgres", "postgres123");
            
    }
     /*
    public static boolean criarBD(String BD)
    {
        try {
            //Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/";
            Connection con = DriverManager.getConnection(url, "postgres", "postgres123");

            Statement statement = con.createStatement();
            statement.execute("CREATE DATABASE " + BD + " WITH OWNER = postgres ENCODING = 'UTF8'  "
                    + "TABLESPACE = pg_default LC_COLLATE = 'Portuguese_Brazil.1252'  "
                    + "LC_CTYPE = 'Portuguese_Brazil.1252'  CONNECTION LIMIT = -1;");
            statement.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
     
   
    public static boolean criarTabelas(String script, String BD) {
        try {
           // Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost/" + BD;
            Connection con = DriverManager.getConnection(url, "postgres", "postgres123");

            Statement statement = connect.createStatement();
            RandomAccessFile arq = new RandomAccessFile(script, "r");
            while (arq.getFilePointer() < arq.length()) {
                statement.addBatch(arq.readLine());
            }
            statement.executeBatch();

            statement.close();
            connect.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    public static void realizaBackupRestauracao(String arqlote) 
    {
        String reslinha ="";
        Runtime r = Runtime.getRuntime();
        try {
            Process p = r.exec(arqlote);
            if (p != null) {
                InputStreamReader str = new InputStreamReader(p.getErrorStream());
                BufferedReader reader = new BufferedReader(str);
                
                String linha;
                while ((linha = reader.readLine()) != null) {
                   reslinha+=linha+"\n";
                }
            }
            JOptionPane.showMessageDialog(null, "Backup/restore realizado com sucesso!"+reslinha);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Erro no backup!" + ex.getMessage());
        }
    }*/

      
}
