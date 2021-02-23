
package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import startenglish.db.DAL.DALAgenda;
import startenglish.db.DAL.DALAluno;
import startenglish.db.DAL.DALProfessor;
import startenglish.db.Entidades.Agenda;
import startenglish.db.Entidades.Aluno;
import startenglish.db.Entidades.Professor;
import startenglish.util.MaskFieldUtil;


public class FXMLAgendarProvaController implements Initializable {

    @FXML
    private JFXComboBox<Professor> cbProfessor;
    @FXML
    private JFXComboBox<String> cbLocal;
    @FXML
    private JFXComboBox<Aluno> cbAluno;
    @FXML
    private JFXComboBox<String> cbStatus;
    @FXML
    private JFXButton btNovo;
    @FXML
    private JFXDatePicker dtDataAgend;
    @FXML
    private JFXButton btInserir;
    @FXML
    private TableColumn<Agenda, String> tcNomeAluno;
    @FXML
    private TableColumn<Agenda, String> tcCPF;
    @FXML
    private TableColumn<Agenda, String> tcProfessor;
    @FXML
    private TableColumn<Agenda, String> tcData;
    @FXML
    private TableColumn<Agenda, String> tcStatus;
    @FXML
    private JFXButton btVoltar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private JFXButton btSalvarOp;
    @FXML
    private JFXComboBox<String> cbFiltro;
    @FXML
    private JFXDatePicker dtDataFIMf;
    @FXML
    private JFXDatePicker dtDataInif;
   @FXML
    private JFXButton btApagar;
    @FXML
    private JFXButton btAlterar;
    @FXML
    private JFXButton btPesquisar;
    @FXML
    private JFXButton btLimpar;
    @FXML
    private TableView<Agenda> tabela;
    @FXML
    private JFXTextField txAluno;
    @FXML
    private JFXComboBox<Professor> cbProfessorF;
    
    private boolean flagalterou;
    private boolean alterou;
    private List<Agenda> listaauxliar;
    private boolean alterando;
    private int indexalterando;
    @FXML
    private JFXTextField txHoraIni;
    @FXML
    private JFXTextField txHoraFim;
    @FXML
    private TableColumn<Agenda, String> tcHoraIni;
    @FXML
    private TableColumn<Agenda, String> tcHoraFim;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        seta_comboboxBanco();
        seta_combobox();
        estadoOriginal();
        seta_pesquisa();
        seta_tabela();
        listaauxliar = new ArrayList();
        alterou = false;
        carregaTabela('I');
        alterando = false;
        setaStyleNormal();
        MaskFieldUtil.maxField(txHoraIni,5);
        MaskFieldUtil.maxField(txHoraFim, 5);
        flagalterou = false;

    }

    public void seta_tabela() {

        tcNomeAluno.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAluno().getNome()));
        tcCPF.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAluno().getCpf()));
        tcData.setCellValueFactory(new PropertyValueFactory("dataProvaFORM"));
        tcHoraIni.setCellValueFactory(new PropertyValueFactory("horaini"));
        tcHoraFim.setCellValueFactory(new PropertyValueFactory("horafim"));
        tcProfessor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProfessor().getFunc().getNome()));
        tcStatus.setCellValueFactory(new PropertyValueFactory("status"));
        
        
    }

    public void seta_combobox() {

        List<String> Local = new ArrayList();
        List<String> Status = new ArrayList();
        List<String> Filtro = new ArrayList();

        Status.add("Aguardando");
        Status.add("Faltou");
        Status.add("Realizou");
        Filtro.add("Aluno");
        Filtro.add("Data");
        Filtro.add("Professor");
        Local.add("Sede");
   
        ObservableList<String> modelostatus = FXCollections.observableArrayList(Status);
        ObservableList<String> modelolocal = FXCollections.observableArrayList(Local);
        ObservableList<String> modelofiltro = FXCollections.observableArrayList(Filtro);
        
        cbStatus.setItems(modelostatus);
        cbFiltro.setItems(modelofiltro);
        cbLocal.setItems(modelolocal);
      

        cbStatus.setValue("Aguardando");
        cbFiltro.setValue("Aluno");
        cbLocal.setValue("Sede");
    }

    public void carregaTabela(char chamada) {

        DALAgenda dalagenda = new DALAgenda();
        List<Agenda> agenda;
        agenda = dalagenda.get("order by dataprova,horainicio,horafim");

        if (chamada == 'I') {
            listaauxliar = agenda;
            tabela.setItems(FXCollections.observableArrayList(listaauxliar));
        } else if (chamada == 'L') {
            
           Collections.sort(listaauxliar, Comparator.comparing(Agenda::getDataProva).thenComparing(Agenda::getHoraini));
           tabela.setItems(FXCollections.observableArrayList(listaauxliar));
        }

    }
    
    private void setaStyleNormal(){
        
        setDataNormal(dtDataAgend);
        setComboboxNormal(cbProfessor);
        setComboboxNormal(cbLocal);
        setTextFieldnormal(txHoraIni);
        setTextFieldnormal(txHoraFim);
        setComboboxNormal(cbAluno);
        setComboboxNormal(cbStatus);
    }
    
    private void estado_edicao(){
        
        dtDataAgend.setValue(null);
        dtDataAgend.requestFocus();
        cbProfessor.getSelectionModel().select(0);
        cbLocal.getSelectionModel().select(0);
        cbAluno.getSelectionModel().select(0);
        cbStatus.getSelectionModel().select(0);
        txHoraIni.clear();
        txHoraFim.clear();
        btAlterar.setDisable(true);
        btApagar.setDisable(true);
        
        setaStyleNormal();
    }
    
    private void seta_pesquisa(){
        
        cbProfessorF.setDisable(true);
        txAluno.clear();
        txAluno.setDisable(false);
        cbFiltro.setValue("Aluno");
        dtDataInif.setDisable(true);
        dtDataFIMf.setDisable(true);
        dtDataInif.setValue(LocalDate.now());
        dtDataFIMf.setValue(LocalDate.now().plusDays(30));
        
    }
    
    private void estadoOriginal(){
        
        btAlterar.setDisable(true);
        btApagar.setDisable(true);
        btSalvarOp.setDisable(true);
        dtDataAgend.requestFocus();
    }
    
    public void seta_comboboxBanco(){
     
        DALProfessor dalp = new DALProfessor();
        DALAluno dala = new DALAluno();
        List<Professor> profs;
        List<Aluno> alunos;
        
        profs = dalp.get("");
        alunos = dala.get("");
        
        
        alunos.sort(Comparator.comparing(Aluno::getNome));
        
        ObservableList<Professor> modelopf =  FXCollections.observableArrayList(profs);
        ObservableList<Aluno> modeloaluno =  FXCollections.observableArrayList(alunos);
        
        cbProfessor.setItems(modelopf);
        cbAluno.setItems(modeloaluno);
        cbProfessorF.setItems(modelopf);
        
        cbAluno.getSelectionModel().select(0);
        cbProfessor.getSelectionModel().select(0);
        cbProfessorF.getSelectionModel().select(0);
    }
    
    @FXML
    private void evtNovo(ActionEvent event) {
        
        estado_edicao();
        alterando = false;
    }

    @FXML
    private void evtDataPesq(MouseEvent event) {
        
        List<Agenda> agendadata = new ArrayList();
        LocalDate age = dtDataAgend.getValue();
        
        if(age != null && (age.isEqual(LocalDate.now())|| age.isAfter(LocalDate.now())))
            setDataNormal(dtDataAgend);
        
        for (int i = 0; i < listaauxliar.size(); i++) {
            
            if(listaauxliar.get(i).getDataProva().equals(age))
                agendadata.add(listaauxliar.get(i));
        }
        
        tabela.setItems(FXCollections.observableArrayList(agendadata));
        
    }

    private boolean validaHoraIni(String hora) {

        boolean ok = true;
        Alert a = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        char ultimocarac;
        
        try {
            sdf.parse(hora);
        } catch (ParseException e) {
            ok = false;
        }

        if (!ok) {
            
            setTextFieldErro(txHoraIni);
            a = new Alert(Alert.AlertType.WARNING, "Hora inicial vazia ou inválida!", ButtonType.CLOSE);
            txHoraIni.requestFocus();

        } else if (ok && hora.length() != 5) {

            ok = false;
            setTextFieldErro(txHoraIni);
            a = new Alert(Alert.AlertType.WARNING, "Hora inicial incompleta!", ButtonType.CLOSE);
            txHoraIni.requestFocus();
            
        } else if (hora.length() == 5) {
            ultimocarac = hora.charAt(hora.length() - 1);

            if (ultimocarac < '0' || ultimocarac > '9') {
                ok = false;
                setTextFieldErro(txHoraIni);
                a = new Alert(Alert.AlertType.WARNING, "Hora inicial inválida!", ButtonType.CLOSE);
                txHoraIni.requestFocus();
            }
            else
              setTextFieldnormal(txHoraIni);
        }
     

        if(a != null)
            a.showAndWait();
        
        return ok;
    }
    
    private boolean validaHoraFim(String hora) {

        boolean ok = true;
        Alert a = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        char ultimocarac;
        
        try {
            sdf.parse(hora);
        } catch (ParseException e) {
            ok = false;
        }

        if (!ok) {
            
            setTextFieldErro(txHoraFim);
            a = new Alert(Alert.AlertType.WARNING, "Hora final vazia ou inválida!", ButtonType.CLOSE);
            txHoraFim.requestFocus();

        } 
        else if(ok  && hora.length() != 5){
            
            ok = false;
            setTextFieldErro(txHoraFim);
            a = new Alert(Alert.AlertType.WARNING, "Hora final incompleta!", ButtonType.CLOSE);
            txHoraFim.requestFocus();
        }
        else if (ok && hora.length() == 5) {

            ultimocarac = hora.charAt(hora.length() - 1);

            if (ultimocarac < '0' || ultimocarac > '9') {
                ok = false;
                setTextFieldErro(txHoraFim);
                a = new Alert(Alert.AlertType.WARNING, "Hora final inválida!", ButtonType.CLOSE);
                txHoraFim.requestFocus();
            }

        }
        if (ok && hora.compareToIgnoreCase(txHoraIni.getText()) <= 0) {
           
            ok = false;
            setTextFieldErro(txHoraFim);
            a = new Alert(Alert.AlertType.WARNING, "Hora final menor ou igual a hora inicial!", ButtonType.CLOSE);
            txHoraFim.requestFocus();
        }
        else if(ok) {
            setTextFieldnormal(txHoraFim);
        }

        if(a != null)
            a.showAndWait();
        
        return ok;
    }
    
    private boolean validaAlunoData(Aluno al, LocalDate date) {

        Agenda aux = null;
        boolean ok = true;
        Alert a = null;

        for (int i = 0; i < listaauxliar.size() && ok; i++) {

            aux = listaauxliar.get(i);

            if (aux.getAluno().getCpf().equals(al.getCpf()) && aux.getDataProva().equals(date)) {
                ok = false;
            }
        }

        if (!ok) {

            if (alterando) {

                if (aux != null && aux.getAluno().getCpf().equals(al.getCpf()) && listaauxliar.indexOf(aux) != indexalterando) {

                    a = new Alert(Alert.AlertType.WARNING, "Aluno já possui prova agendada nesta data!", ButtonType.CLOSE);
                    cbAluno.requestFocus();
                    setComboboxErro(cbAluno);
                } else {
                    ok = true;
                    setComboboxNormal(cbAluno);
                }

            } else {
                a = new Alert(Alert.AlertType.WARNING, "Aluno já possui prova agendada nesta data!", ButtonType.CLOSE);
                cbAluno.requestFocus();
                setComboboxErro(cbAluno);
            }

        }
        else{
            ok = true;
            setComboboxNormal(cbAluno);
        }

        if (a != null) {
            a.showAndWait();
        }

        return ok;

    }
    
    public boolean validaProfessorHorarioData(String prof, LocalDate date, String horaini,String horafim) {

        Agenda aux = null;
        boolean ok = true;
        Alert a = null;
        List<Agenda> listaProfHor = new ArrayList();
        boolean flag = false;
        List<Integer> indices = new ArrayList();
        int indiceaux;
        
        for (int i = 0; i < listaauxliar.size(); i++) {

            aux = listaauxliar.get(i);

            if (aux.getProfessor().getFunc().getCpf().equals(prof) && aux.getDataProva().equals(date)) {
                ok = false;
                listaProfHor.add(aux);
                indices.add(i);
            }
        }

        if (!ok) {

            for (int i = 0; i < listaProfHor.size() && !flag; i++) {

                aux = listaProfHor.get(i);
                indiceaux = indices.get(i);
                
                if (alterando) {
                    if ((horaini.compareTo(aux.getHoraini()) >= 0 && horafim.compareTo(aux.getHorafim()) <= 0)
                            || ((horaini.compareToIgnoreCase(aux.getHorafim()) < 0 && horafim.compareTo(aux.getHoraini()) > 0))) {
                        
                        if(indiceaux != indexalterando)
                            flag = true;
                    }

                } else {
                    if ((horaini.compareTo(aux.getHoraini()) >= 0 && horafim.compareTo(aux.getHorafim()) <= 0)
                            || ((horaini.compareToIgnoreCase(aux.getHorafim()) < 0 && horafim.compareTo(aux.getHoraini()) > 0))) {
                        flag = true;
                    }

                }
            }

            if (!flag) {
                ok = true;
                setComboboxNormal(cbProfessor);
                setTextFieldnormal(txHoraFim);
                setTextFieldnormal(txHoraIni);
            } else {
                ok = false;
                cbProfessor.requestFocus();
                setComboboxErro(cbProfessor);
                setTextFieldErro(txHoraFim);
                setTextFieldErro(txHoraIni);
                a = new Alert(Alert.AlertType.WARNING, "Professor já possui uma prova agenda neste horario!!", ButtonType.CLOSE);
            }
        }

        if (a != null) {
            a.showAndWait();
        }
        return ok;
    }
    
    private boolean validaData(LocalDate date) {

        boolean ok = true;
        Alert a = null;

        if (date == null) {

            ok = false;
            a = new Alert(Alert.AlertType.WARNING, "Data de agendamento não pode estar vazia!", ButtonType.CLOSE);
            dtDataAgend.requestFocus();
            setDataErro(dtDataAgend);

        } else if (!alterando && date.isBefore(LocalDate.now())) {

            ok = false;
            a = new Alert(Alert.AlertType.WARNING, "Data de agendamento antes do dia atual!", ButtonType.CLOSE);
            dtDataAgend.requestFocus();
             setDataErro(dtDataAgend);
        } 
        else
            setDataNormal(dtDataAgend);
        
        if(a != null)
         a.showAndWait();
        
        return ok;
    }
    
    private void setTextFieldErro(JFXTextField nome) {

        nome.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                + "-fx-background-color: #BEBEBE;"
                + "-fx-font-weight: bold;");
    }

    private void setTextFieldnormal(JFXTextField nome) {

        nome.setStyle("-fx-border-width: 0;"
                + "-fx-background-color: #BEBEBE;"
                + "-fx-font-weight: bold;");

    }
    
     private void setDataErro(JFXDatePicker nome) {

        nome.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                + "-fx-background-color: #BEBEBE;"
                + "-fx-font-weight: bold;");
    }

    private void setDataNormal(JFXDatePicker nome) {

        nome.setStyle("-fx-border-width: 0;"
                + "-fx-background-color: #BEBEBE;"
                + "-fx-font-weight: bold;");

    }
    
    private void setComboboxErro(JFXComboBox nome) {

        nome.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                + "-fx-background-color: #BEBEBE;"
                + "-fx-font-weight: bold;");
    }

     private void setComboboxNormal(JFXComboBox nome) {

       nome.setStyle("-fx-border-width: 0;"
                + "-fx-background-color: #BEBEBE;"
                + "-fx-font-weight: bold;");
    }
     
    private char retornaStatus(String status){
        
        switch (status) {
            case "Aguardando":
                return 'A';
            case "Realizou":
                return 'R';
            default:
                return 'F';
        }
    }
    
    @FXML
    private void evtInserir(ActionEvent event) {

        Agenda a = new Agenda();
        boolean problem = false;

        if (validaData(dtDataAgend.getValue())) {

            if (validaAlunoData(cbAluno.getSelectionModel().getSelectedItem(), dtDataAgend.getValue())) {

                if (validaHoraIni(txHoraIni.getText())) {

                    if (validaHoraFim(txHoraFim.getText())) {

                        if (validaProfessorHorarioData(cbProfessor.getSelectionModel().getSelectedItem().getFunc().getCpf(),
                            dtDataAgend.getValue(), txHoraIni.getText(),txHoraFim.getText())) {

                            a.setAluno(cbAluno.getSelectionModel().getSelectedItem());
                            a.setDataProva(dtDataAgend.getValue());
                            a.setProfessor(cbProfessor.getSelectionModel().getSelectedItem());
                            a.setLocal(cbLocal.getSelectionModel().getSelectedItem());
                            a.setHoraini(txHoraIni.getText());
                            a.setHorafim(txHoraFim.getText());
                            a.setStatus(cbStatus.getSelectionModel().getSelectedItem());
                            a.setSituacao_prova(retornaStatus(cbStatus.getSelectionModel().getSelectedItem()));
                            a.setaData();
                            if (alterando) {

                                if (indexalterando < listaauxliar.size()) {
                                    listaauxliar.remove(indexalterando);
                                    listaauxliar.add(a);
                                 
                                }
                                else{
                                     problem = true;
                                }

                            } else {
                                listaauxliar.add(a);

                            }
                            Alert alert;

                            if (problem) {
                                
                                alert = new Alert(Alert.AlertType.INFORMATION, "Agendamento já excluido, impossivel alterar!", ButtonType.CLOSE);
                                 alert.showAndWait();

                            } else {

                                if (alterando) {
                                    alert = new Alert(Alert.AlertType.INFORMATION, "Agendamento atualizado com sucesso!", ButtonType.CLOSE);
                                } else {
                                    alert = new Alert(Alert.AlertType.INFORMATION, "Agendamento inserido com sucesso!", ButtonType.CLOSE);
                                }

                                alterando = false;
                                carregaTabela('L');
                                alert.showAndWait();
                                alterou = true;
                                btSalvarOp.setDisable(false);
                                evtNovo(event);
                            }

                        }
                    }
                }
            }
        }

    }

    @FXML
    private void evtVoltar(ActionEvent event) {

        if (alterou) {
            evtSalvarOp(event);
        }

        FXMLPrincipalController.snprincipal.setCenter(null);
        FXMLPrincipalController.nome.setText("");
    }

    @FXML
    private void evtCancelarOP(ActionEvent event) {
        
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Deseja cancelar todas as operações realizadas?", ButtonType.YES,ButtonType.NO);
        
        if(a.showAndWait().get() == ButtonType.YES){
            
            alterou = false;
            carregaTabela('I');
            btSalvarOp.setDisable(true);
            estado_edicao();
        }
    }

    @FXML
    private void evtSalvarOp(ActionEvent event) {

        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Deseja salvar todas modificações?", ButtonType.YES, ButtonType.NO);
        boolean ok = false;

        if (a.showAndWait().get() == ButtonType.YES) {

            DALAgenda dale = new DALAgenda();
            ok = dale.InserirTudo(listaauxliar);

            Alert b = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            if (ok) {
                b.setContentText("Todas informações salvas com sucesso!");
            } else {
                b.setContentText("Problemas ao salvar as informações!");
            }
            
            b.showAndWait();
        }

        if (ok) {
            FXMLPrincipalController.snprincipal.setCenter(null);
            FXMLPrincipalController.nome.setText("");
        }

    }

    @FXML
    private void evtFiltroComb(ActionEvent event) {

        String filtro = cbFiltro.getSelectionModel().getSelectedItem();

        if (filtro.contains("Data")) {

            dtDataInif.setDisable(false);
            dtDataFIMf.setDisable(false);
            txAluno.setDisable(true);
            cbProfessorF.setDisable(true);
        } else {

            dtDataInif.setDisable(true);
            dtDataFIMf.setDisable(true);

            if (filtro.contains("Aluno")) {

                txAluno.setDisable(false);
                cbProfessorF.setDisable(true);
            } else {

                txAluno.setDisable(true);
                cbProfessorF.setDisable(false);
            }

        }
    }

    @FXML
    private void evtApagar(ActionEvent event) {
        
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES,ButtonType.NO);
        
        if(a.showAndWait().get() == ButtonType.YES){
            
            listaauxliar.remove(tabela.getSelectionModel().getSelectedItem());
            carregaTabela('L');
            alterou = true;
            btSalvarOp.setDisable(false);
        }
    }

    @FXML
    private void evtAlterar(ActionEvent event) {
        
          if(tabela.getSelectionModel().getSelectedItem() != null){
        
              flagalterou = true;
            Agenda a = tabela.getSelectionModel().getSelectedItem();
            
            dtDataAgend.setValue(a.getDataProva());
            cbProfessor.getSelectionModel().select(a.getProfessor());
            cbLocal.getSelectionModel().select(a.getLocal());
            txHoraIni.setText(a.getHoraini());
            txHoraFim.setText(a.getHorafim());
            cbAluno.getSelectionModel().select(a.getAluno());
            cbStatus.getSelectionModel().select(a.getStatus());

            alterando = true;
            indexalterando = tabela.getSelectionModel().getSelectedIndex();
            dtDataAgend.requestFocus();
            setaStyleNormal();
          }
    }
    
    @FXML
    private void evtPesquisar(ActionEvent event) {
      
        String filtro = cbFiltro.getSelectionModel().getSelectedItem();
        LocalDate dataini, datafim;
        List<Agenda> novasagendas = new ArrayList();
        String nome_aluno;
        Professor paux;
        
        
        if(filtro.contains("Data")){
            
            dataini = dtDataInif.getValue();
            datafim = dtDataFIMf.getValue();
            
            for (int i = 0; i < listaauxliar.size(); i++) {
                
                if((listaauxliar.get(i).getDataProva().isAfter(dataini) || listaauxliar.get(i).getDataProva().isEqual(dataini)) && (listaauxliar.get(i).getDataProva().isBefore(datafim) || listaauxliar.get(i).getDataProva().isEqual(datafim)))
                    novasagendas.add(listaauxliar.get(i));
            }
        }
        else{
            
            if(filtro.contains("Aluno")){
                
                nome_aluno = txAluno.getText().toLowerCase();

                for (int i = 0; i < listaauxliar.size(); i++) {

                    if (listaauxliar.get(i).getAluno().getNome().toLowerCase().contains(nome_aluno)) {
                        novasagendas.add(listaauxliar.get(i));
                    }
                }
                
            }
            else{
                
                paux = cbProfessorF.getSelectionModel().getSelectedItem();
                
                for (int i = 0; i < listaauxliar.size(); i++) {

                    if (listaauxliar.get(i).getProfessor().getFunc().getNome().equals(paux.getFunc().getNome())) {
                        novasagendas.add(listaauxliar.get(i));
                    }
                }
            }
        }
        
        tabela.setItems(FXCollections.observableArrayList(novasagendas));
    }

    @FXML
    private void evtLimpar(ActionEvent event) {
        
        seta_pesquisa();
        carregaTabela('L');
    }

    @FXML
    private void evTabela(MouseEvent event) {

        if (tabela.getSelectionModel().getSelectedIndex() >= 0) {
            btApagar.setDisable(false);
            btAlterar.setDisable(false);
        } else {

            btApagar.setDisable(true);
            btAlterar.setDisable(true);
        }
    }

    @FXML
    private void evtteste(ActionEvent event) {
        
        if(!flagalterou && dtDataAgend != null)
             validaAlunoData(cbAluno.getSelectionModel().getSelectedItem(), dtDataAgend.getValue());
        else
            flagalterou = false;
    }
    
}
