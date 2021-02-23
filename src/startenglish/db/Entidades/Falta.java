package startenglish.db.Entidades;

import java.util.Date;
import javafx.scene.control.CheckBox;

public class Falta {
    private Turma turma;
    private Matricula matricula;
    private Date data;
    private CheckBox presenca;

    public Falta() {
    }

    public Falta(Turma turma, Matricula matricula, Date data, CheckBox presenca) {
        this.turma = turma;
        this.matricula = matricula;
        this.data = data;
        this.presenca = presenca;
    }
   
    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public CheckBox getPresenca() {
        return presenca;
    }

    public void setPresenca(CheckBox presenca) {
        this.presenca = presenca;
    }
    
    
}
