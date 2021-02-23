package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import startenglish.db.DAL.DALCurso;
import startenglish.db.DAL.DALProfessor;
import startenglish.db.DAL.DALTurma;
import startenglish.db.Entidades.Cursos;
import startenglish.db.Entidades.Professor;
import startenglish.db.Entidades.Turma;
import startenglish.db.util.Banco;
import startenglish.util.MaskFieldUtil;

public class FXMLTurmasController implements Initializable 
{

    @FXML
    private AnchorPane pndados;
    @FXML
    private JFXTextField txID;
    @FXML
    private JFXButton btPesquisar;
    @FXML
    private JFXTextField txPesquisa;
    @FXML
    private JFXComboBox<String> cbFiltro;
    @FXML
    private TableView<Turma> tabela;
    @FXML
    private JFXComboBox<Professor> cbProfessor;
    @FXML
    private JFXComboBox<Cursos> cbCurso;
    @FXML
    private TableColumn<Turma,String> tabelaTurma;
    @FXML
    private TableColumn<Turma,String> tabelaProfessor;
    @FXML
    private TableColumn<Turma,String> tabelaCurso;
    @FXML
    private TableColumn<Turma,String> tabelaIni;
    @FXML
    private TableColumn<Turma,String> tabelaFim;
    @FXML
    private TableColumn<Turma,String> tabelaAtivo;
    @FXML
    private JFXTextField txHorIni;
    @FXML
    private JFXTextField txHorFim;
    @FXML
    private JFXDatePicker dtIni;
    @FXML
    private JFXCheckBox checkAtivo;
    @FXML
    private JFXTextField txTurma;
    @FXML
    private JFXCheckBox checkSegunda;
    @FXML
    private JFXTextField txVagas;
    @FXML
    private JFXCheckBox checkDomingo;
    @FXML
    private JFXCheckBox checkSabado;
    @FXML
    private JFXCheckBox checkSexta;
    @FXML
    private JFXCheckBox checkQuinta;
    @FXML
    private JFXCheckBox checkQuarta;
    @FXML
    private JFXCheckBox checkTerca;
    @FXML
    private JFXCheckBox checkAtivaSearch;
    @FXML
    private JFXButton btInserir;
    @FXML
    private JFXButton btAlterar;
    @FXML
    private JFXButton btExcluir;
    @FXML
    private JFXButton btConfirmar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private JFXComboBox<Professor> cbFiltroProfessor;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       tabelaTurma.setCellValueFactory(new PropertyValueFactory("nome"));
       tabelaProfessor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProf().getFunc().getNome()));
       tabelaCurso.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCur().getNomeCurso()));
       tabelaIni.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHorario().substring(0, 5)));
       tabelaFim.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getHorario().substring(8)));
       tabelaAtivo.setCellValueFactory(new PropertyValueFactory("ativo"));
       dtIni.setValue(LocalDate.now());
       MaskFieldUtil.maxField(txTurma,29);
       MaskFieldUtil.maxField(txVagas,5);
       MaskFieldUtil.numericField(txVagas);
       MaskFieldUtil.maxField(txHorIni,5);
       MaskFieldUtil.maxField(txHorFim,5);
       
       List<String> combo = new ArrayList();
        combo.add("Turma");
        combo.add("Professor");
        
        ObservableList<String> modelo = FXCollections.observableArrayList(combo);
        cbFiltro.setItems(modelo);
        cbFiltro.setValue("Turma");
        
        List<Professor> comboProf = new ArrayList();
        DALProfessor prof = new DALProfessor();
        comboProf=prof.get("");
        ObservableList<Professor> modeloProf = FXCollections.observableArrayList(comboProf);
        cbProfessor.setItems(modeloProf);
        cbFiltroProfessor.setItems(modeloProf);
        cbProfessor.getSelectionModel().selectFirst();
        List<Cursos> comboCursos= new ArrayList();
        DALCurso cur = new DALCurso();
        comboCursos=cur.get("");
        ObservableList<Cursos> modeloCursos = FXCollections.observableArrayList(comboCursos);
        cbCurso.setItems(modeloCursos);
        cbCurso.getSelectionModel().selectFirst();
        EstadoOriginal();
    }    
    private void EstadoOriginal()
    {    
        dtIni.setValue(LocalDate.now());
        txTurma.setDisable(true);
        cbCurso.setDisable(true);
        dtIni.setDisable(true);
        cbProfessor.setDisable(true);
        txHorIni.setDisable(true);
        txHorFim.setDisable(true);
        txVagas.setDisable(true);
        checkAtivo.setDisable(true);
        checkSegunda.setDisable(true);
        checkTerca.setDisable(true);
        checkQuarta.setDisable(true);
        checkQuinta.setDisable(true);
        checkSexta.setDisable(true);
        checkSabado.setDisable(true);
        checkDomingo.setDisable(true);       
        checkAtivo.setSelected(false);
        checkSegunda.setSelected(false);
        checkTerca.setSelected(false);
        checkQuarta.setSelected(false);
        checkQuinta.setSelected(false);
        checkSexta.setSelected(false);
        checkSabado.setSelected(false);
        checkDomingo.setSelected(false);
        btInserir.setDisable(false);
        btAlterar.setDisable(true);
        btExcluir.setDisable(true);
        btCancelar.setDisable(false);
        btConfirmar.setDisable(true);
        cbFiltroProfessor.setDisable(true);
        txPesquisa.clear();
        
        ObservableList <Node> componentes=pndados.getChildren(); //”limpa” os componentes
        for(Node n : componentes)
        {
            if (n instanceof TextInputControl)  // textfield, textarea e htmleditor
                ((TextInputControl)n).setText("");
        }
      
        CarregaTabela("");
    }
    private void EstadoEdicao()
    {
        txTurma.setDisable(false);
        cbCurso.setDisable(false);
        dtIni.setDisable(false);
        cbProfessor.setDisable(false);
        txHorIni.setDisable(false);
        txHorFim.setDisable(false);
        txVagas.setDisable(false);
        checkAtivo.setDisable(false);
        checkSegunda.setDisable(false);
        checkTerca.setDisable(false);
        checkQuarta.setDisable(false);
        checkQuinta.setDisable(false);
        checkSexta.setDisable(false);
        checkSabado.setDisable(false);
        checkDomingo.setDisable(false);
        btInserir.setDisable(true);
        btAlterar.setDisable(true);
        btExcluir.setDisable(true);
        btCancelar.setDisable(false);
        btConfirmar.setDisable(false);
        txTurma.requestFocus();
        
    }
    
    private void CarregaTabela(String filtro)
    {
        DALTurma dalt = new DALTurma();       
        List <Turma> listaTurma = dalt.get(filtro);
        listaTurma = dalt.get(filtro);           
        ObservableList <Turma> modelo;
        modelo = FXCollections.observableArrayList(listaTurma);       
        tabela.setItems(modelo);
    }

    @FXML
    private void evtPesquisar(ActionEvent event)
    {
        String auxiliar="";
        Professor prof = new Professor();
        if(checkAtivaSearch.isSelected())
            auxiliar="turma.ativo='A' and ";
        DALProfessor dal=new DALProfessor();
        if(cbFiltro.getSelectionModel().getSelectedItem().equals("Turma"))
           CarregaTabela(auxiliar+"turma.nome like '%"+txPesquisa.getText()+"%'");
        else
        {
            prof = cbFiltroProfessor.getSelectionModel().getSelectedItem();
            CarregaTabela(auxiliar+"turma.professorid= "+prof.getFunc().getID()); 
        }   
    }

    @FXML
    private void evtClickTabela(MouseEvent event)
    {
        if(tabela.getSelectionModel().getSelectedIndex()>=0)
        {
           btAlterar.setDisable(false);
           btExcluir.setDisable(false);
        }
    }

    @FXML
    private void evtInserir(ActionEvent event) 
    {
        EstadoEdicao();
        checkAtivo.setSelected(true);
    }

    @FXML
    private void evtAlterar(ActionEvent event)
    {
        if(tabela.getSelectionModel().getSelectedItem() != null)
        {     
            EstadoEdicao();
            Turma t = tabela.getSelectionModel().getSelectedItem();
            Double auxiliar;
            txID.setText(""+t.getTurmaID());
            txTurma.setText(t.getNome());
            txHorIni.setText(t.getHorario().substring(0,5));
            txHorFim.setText(t.getHorario().substring(8));
            txVagas.setText(""+t.getQtdvagas());
            cbProfessor.getSelectionModel().select(t.getProf());
            cbCurso.getSelectionModel().select(t.getCur());
            dtIni.setValue(t.getAno());
            if(t.getAtivo()=='A')
                checkAtivo.setSelected(true);
            if(t.getSemana().charAt(0)=='1')
                checkSegunda.setSelected(true);
            if(t.getSemana().charAt(1)=='1')
                checkTerca.setSelected(true);
            if(t.getSemana().charAt(2)=='1')
                checkQuarta.setSelected(true);
            if(t.getSemana().charAt(3)=='1')
                checkQuinta.setSelected(true);
            if(t.getSemana().charAt(4)=='1')
                checkSexta.setSelected(true);
            if(t.getSemana().charAt(5)=='1')
                checkSabado.setSelected(true);
            if(t.getSemana().charAt(6)=='1')
                checkDomingo.setSelected(true);
            
            
        }
    }

    @FXML
    private void evtExcluir(ActionEvent event) 
    {

            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES,ButtonType.NO);
            boolean ok = true;

            if(a.showAndWait().get() == ButtonType.YES)
            {           
                Turma t = new Turma();
                DALTurma dal = new DALTurma();

                t = tabela.getSelectionModel().getSelectedItem();

                try{

                    Banco.getCon().getConnect().setAutoCommit(false);

                    ok = dal.apagar(t.getTurmaID());

                    if(ok)
                    {                   
                         Banco.getCon().getConnect().commit();
                        a =  new Alert(Alert.AlertType.CONFIRMATION," Exclusão ocorrida com sucesso!!", ButtonType.OK);
                    }
                    else
                    {                  
                        Banco.getCon().getConnect().rollback();
                        a =  new Alert(Alert.AlertType.ERROR, "Erro ao deletar turma! Verificar se ainda existem matrículas!", ButtonType.OK);
                    }

                    a.showAndWait();

                }catch(SQLException ex){System.out.println(ex.getMessage());}

                EstadoOriginal();    
                CarregaTabela("");
            
            }
        
    }
    private boolean validaHoraIni()
    {
        String a =txHorIni.getText();
        if(a.charAt(0)>47 && a.charAt(0)<51 && a.charAt(1)>47 && a.charAt(1)<58 &&
                a.charAt(3)>47 && a.charAt(3)<54 && a.charAt(4)>47 && a.charAt(4)<58)
        {
            
            if(a.charAt(2)==':')
                {
                    if(a.charAt(0)=='2' && a.charAt(1)>51)
                    {
                        return false;
                    }
                    else
                        return true;
                }
            
        }
        return false;
    }
    private boolean validaHoraFim()
    {
        String b =txHorFim.getText();
            if(b.charAt(0)>47 && b.charAt(0)<51 && b.charAt(1)>47 && b.charAt(1)<58 &&
                b.charAt(3)>47 && b.charAt(3)<54 && b.charAt(4)>47 && b.charAt(4)<58)
            {
                if(b.charAt(2)==':')
                {
                    if(b.charAt(0)=='2' && b.charAt(1)>51)
                    {
                        return false;
                    }
                    else
                        return true;
                }
                    
            }
        
        return false;
    }
    private boolean validaHoraFimini()
    {
        String a =txHorIni.getText();
        String b =txHorFim.getText();
        if(validaHoraFim() && validaHoraIni())
        {
            if(b.compareTo(a)>0)
                return true;
            else
                return false;
        }
        return false;
    }
    private boolean validatxt(String nome,String horin,String horfim,String vagas)
    {
        boolean ok=true;
        Alert a=null;
        if(nome.isEmpty())
        {        
            ok = false;
            txTurma.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txTurma.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Nome da turma obrigatório!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
            
        }
        else
        {
            txTurma.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
        }
        if(horin.isEmpty())
        {
            ok = false;
            txHorIni.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txHorIni.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Horário inicial obrigatório!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
        }
        else if(horin.length()<5)
        {
            ok = false;
            txHorIni.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txHorIni.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Horário inicial inválido!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
        }
        else if(!validaHoraIni())
        {
            ok = false;
            txHorIni.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txHorIni.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Horário inicial inválido!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
        }
        else
        {
            txHorIni.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
        }
        if(horfim.isEmpty())
        {
            ok = false;
            txHorFim.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txHorFim.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Horário final obrigatório!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
        }
        else if(horfim.length()<5)
        {
            ok = false;
            txHorFim.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txHorFim.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Horário final inválido!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
        }
        else if(!validaHoraFim())
        {
            ok = false;
            txHorFim.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txHorFim.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Horário final inválido!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
        }
        else
        {
            txHorFim.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
        }
        if(vagas.isEmpty())
        {
            ok = false;
            txVagas.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txVagas.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Vagas obrigatórias!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
        }
        else
        {
            try
            {
                Integer.parseInt(txVagas.getText());
                if(Integer.parseInt(txVagas.getText())<0)
                    txVagas.setText("A");
                Integer.parseInt(txVagas.getText());
                txVagas.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            }
            catch(NumberFormatException ex)
            {
               if(a==null)
                {
                    txVagas.requestFocus();
                    a = new Alert(Alert.AlertType.WARNING, "Vagas inválidas!!", ButtonType.CLOSE);            
                    a.showAndWait();
                } 
            }
        }
        if(!validaHoraFimini())
        {
            ok=false;
            if(a==null)
                {
                    txVagas.requestFocus();
                    a = new Alert(Alert.AlertType.WARNING, "Horário final menor que o inicial!!", ButtonType.CLOSE);            
                    a.showAndWait();
                } 
        }
        return ok;
    }
    private boolean validadata()
    {
        if(dtIni.getValue().isBefore(LocalDate.now()))
        {
            dtIni.requestFocus();
                    Alert b = new Alert(Alert.AlertType.WARNING, "Data inválida!!", ButtonType.CLOSE);            
                    b.showAndWait();
            return false;
        }
            
        return true;
    }
    private boolean validaprof()
    {
        DALTurma t = new DALTurma();
        List<Turma> lista = new ArrayList();
        lista=t.get("");
        String semana="";
        if(checkSegunda.isSelected())
            semana+='1';
        else
            semana+='0';
        if(checkTerca.isSelected())
            semana+='1';
        else
            semana+='0';
        if(checkQuarta.isSelected())
            semana+='1';
        else
            semana+='0';
        if(checkQuinta.isSelected())
            semana+='1';
        else
            semana+='0';
        if(checkSexta.isSelected())
            semana+='1';
        else
            semana+='0';
        if(checkSabado.isSelected())
            semana+='1';
        else
            semana+='0';
        if(checkDomingo.isSelected())
            semana+='1';
        else
            semana+='0';
        boolean retira=false,conflito;
        for (int i = 0; i < lista.size() && !retira; i++)
        {
            retira=false;
            conflito=false;
            List<Turma> comboTurmas = new ArrayList();
            DALTurma Tur = new DALTurma();
            comboTurmas=Tur.get("");
            
            LocalTime ini =java.time.LocalTime.parse(comboTurmas.get(i).getHorario().substring(0, 5));
           LocalTime fim =java.time.LocalTime.parse(comboTurmas.get(i).getHorario().substring(8));
            
            LocalTime inin=java.time.LocalTime.parse(txHorIni.getText());
            LocalTime fimn=java.time.LocalTime.parse(txHorFim.getText());
            
            if(inin.isBefore(ini))
            {
                if(!fimn.isBefore(ini))
                {
                    conflito=true;
                }
            }
            else
            {
                if(!inin.isAfter(fim));
                {
                    conflito=true;
                }
            }
            if(conflito && comboTurmas.get(i).getProf().getFunc().getNome().equals(cbProfessor.getSelectionModel().getSelectedItem().getFunc().getNome()))
            {
                Turma aux= comboTurmas.get(i);
                if(aux.getSemana().charAt(0)=='1' && semana.charAt(0)=='1')
                    retira=true;
                else if(aux.getSemana().charAt(1)=='1' && semana.charAt(1)=='1')
                    retira=true;
                else if(aux.getSemana().charAt(2)=='1' && semana.charAt(2)=='1')
                    retira=true;
                else if(aux.getSemana().charAt(3)=='1' && semana.charAt(3)=='1')
                    retira=true;
                else if(aux.getSemana().charAt(4)=='1' && semana.charAt(4)=='1')
                    retira=true;
                else if(aux.getSemana().charAt(5)=='1' && semana.charAt(5)=='1')
                    retira=true;
                else if(aux.getSemana().charAt(6)=='1' && semana.charAt(6)=='1')
                    retira=true;
                if(retira && aux.getTurmaID()==Integer.parseInt(txID.getText()))
                {
                    retira=false;
                }
            }
                     
        }
        if(retira)
        {
            Alert a = new Alert(Alert.AlertType.ERROR, "Professor leciona no horário informado!!", ButtonType.OK);
            a.showAndWait();
            return false;
        }
        return true;
    }
    @FXML
    private void evtConfirmar(ActionEvent event)
    {
        if(validatxt(txTurma.getText(), txHorIni.getText(), txHorFim.getText(), txVagas.getText()) && validadata() && validaprof())
        {
            int cod;
            String semana="";
            String horario="";
            try
            {
                cod = Integer.parseInt(txID.getText());
            }
            catch (NumberFormatException ex) 
            {
                cod = 0;                   
            }
            DALTurma dalt = new DALTurma();
            Turma t = new Turma();
            t.setNome(txTurma.getText());
            t.setAno(dtIni.getValue());
            t.setCur(cbCurso.getSelectionModel().getSelectedItem());
            t.setProf(cbProfessor.getSelectionModel().getSelectedItem());
            t.setQtdvagas(Integer.parseInt(txVagas.getText()));
            horario+=txHorIni.getText();
            horario+=" a ";
            horario+=txHorFim.getText();
            t.setHorario(horario);
            if(checkSegunda.isSelected())
                semana+='1';
            else
                semana+='0';
            if(checkTerca.isSelected())
                semana+='1';
            else
                semana+='0';
            if(checkQuarta.isSelected())
                semana+='1';
            else
                semana+='0';
            if(checkQuinta.isSelected())
                semana+='1';
            else
                semana+='0';
            if(checkSexta.isSelected())
                semana+='1';
            else
                semana+='0';
            if(checkSabado.isSelected())
                semana+='1';
            else
                semana+='0';
            if(checkDomingo.isSelected())
                semana+='1';
            else
                semana+='0';
            t.setSemana(semana);
            if(checkAtivo.isSelected())
                t.setAtivo('A');
            else
                t.setAtivo('F');
            t.setTurmaID(cod);
            Alert a = null;
            boolean ok;
            try
                {
                    Banco.getCon().getConnect().setAutoCommit(false);

                    if (cod == 0)
                    {
                        ok = dalt.gravar(t);

                        if (ok)
                        {
                            a = new Alert(Alert.AlertType.CONFIRMATION, "Turma inserida!!", ButtonType.OK);
                        } 
                        else 
                        {
                            a = new Alert(Alert.AlertType.ERROR, "Problemas ao inserir a turma!!", ButtonType.OK);
                        }

                    } 
                    else
                    { 
                        ok = dalt.alterar(t);

                        if(ok)
                        {
                            a = new Alert(Alert.AlertType.CONFIRMATION, "Turma atualizada!!", ButtonType.OK);
                        } 
                        else
                        {
                            a = new Alert(Alert.AlertType.ERROR, "Problemas ao atualizar a turma!!", ButtonType.OK);
                        }

                    }

                    a.showAndWait();
                    if(ok)
                    {
                        Banco.getCon().getConnect().commit();
                        EstadoOriginal();
                        CarregaTabela("");
                    } 
                    else
                    {
                        Banco.getCon().getConnect().rollback();
                    }

                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                }
        }
    }

    @FXML
    private void evtCancelar(ActionEvent event)
    {
        if (!txTurma.isDisabled())
        {
            EstadoOriginal();
            
        } 
        else
        {
            
            FXMLPrincipalController.snprincipal.setCenter(null);
            FXMLPrincipalController.nome.setText("");          
        }
    }

    @FXML
    private void evtFiltroTxt(ActionEvent event) 
    {
        if(cbFiltro.getSelectionModel().getSelectedItem().contains("Turma"))
        {
            txPesquisa.setDisable(false);
            cbFiltroProfessor.setDisable(true);
            txPesquisa.setText("");
        }
        else if(cbFiltro.getSelectionModel().getSelectedItem().contains("Professor"))
        {
            txPesquisa.setDisable(true);
            cbFiltroProfessor.setDisable(false);
            txPesquisa.setText("");
        }
    }
       
}
