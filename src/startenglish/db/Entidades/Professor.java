package startenglish.db.Entidades;

public class Professor {
    private Funcionario func;

    public Professor() {
    }

    public Professor(Funcionario func) {
        this.func = func;
    }
   
    public Funcionario getFunc() {
        return func;
    }

    public void setFunc(Funcionario func) {
        this.func = func;
    }
    
    @Override
    public String toString(){
        
        return this.func.getNome();
    }
}
