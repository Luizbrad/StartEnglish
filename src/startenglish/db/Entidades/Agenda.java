package startenglish.db.Entidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Agenda {
    
    private int ID;
    private Professor professor;
    private Aluno aluno;
    private LocalDate dataProva;
    private String horaini;
    private String horafim;
    private String local;
    private char situacao_prova;
    private double nota;
    private String dataProvaFORM;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
   

    public String getDataProvaFORM() {
        return dataProvaFORM;
    }

    public void setDataProvaFORM(String dataProvaFORM) {
        this.dataProvaFORM = dataProvaFORM;
    }

    
    public Agenda(int ID, Professor professor, Aluno aluno, LocalDate dataProva, String horaini, String horafim, String local, char situacao_prova, double nota) {
        this.ID = ID;
        this.professor = professor;
        this.aluno = aluno;
        this.dataProva = dataProva;
        this.horaini = horaini;
        this.horafim = horafim;
        this.local = local;
        this.situacao_prova = situacao_prova;
        this.nota = nota;
        
        switch (situacao_prova) {
            case 'A':
                this.status = "Aguardando";
                break;
            case 'R':
                this.status = "Realizou";
                break;
            default:
                this.status = "Faltou";
                break;
        }
        
        setaData();
    }
   
    public void setaData(){
        if(dataProva != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            this.dataProvaFORM  = this.dataProva.format(formatter);
        }
    }
    public Agenda() {
        
        this(0,new Professor(),new Aluno(),null,"","","",'F',0.0);
        this.status = "";
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public LocalDate getDataProva() {
        return dataProva;
    }

    public void setDataProva(LocalDate dataProva) {
        this.dataProva = dataProva;
    }

  
    public String getLocal() {
        return local;
    }

    public String getHoraini() {
        return horaini;
    }

    public void setHoraini(String horaini) {
        this.horaini = horaini;
    }

    public String getHorafim() {
        return horafim;
    }

    public void setHorafim(String horafim) {
        this.horafim = horafim;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public char getSituacao_prova() {
        return situacao_prova;
    }

    public void setSituacao_prova(char situacao_prova) {
        this.situacao_prova = situacao_prova;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
    
    

   
}
