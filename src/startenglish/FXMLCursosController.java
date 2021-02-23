package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import startenglish.db.Entidades.Cursos;
import startenglish.db.util.Banco;
import startenglish.util.MaskFieldUtil;


public class FXMLCursosController implements Initializable {

    @FXML
    private JFXButton btNovo;
    @FXML
    private JFXTextField txId;
    @FXML
    private JFXButton btPesquisar;
    @FXML
    private JFXTextField txPesquisa;
    @FXML
    private JFXTextField txNomeCurso;
    @FXML
    private JFXTextField txPreco;
    @FXML
    private JFXTextField txDescricao;
    @FXML
    private TableColumn<Cursos, String> tabelaNomeCurso;
    @FXML
    private TableColumn<Cursos, Double> tabelaPreco;
    @FXML
    private TableView<Cursos> tableview;
    @FXML
    private JFXButton btAlterar;
    @FXML
    private JFXButton btExcluir;
    @FXML
    private JFXButton btConfirmar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private AnchorPane pndados;
    @FXML
    private AnchorPane pnpesquisa;
    @FXML
    private JFXDatePicker dtpDataLanc;
    @FXML
    private JFXDatePicker dtpDataEnc;
    @FXML
    private JFXCheckBox cEncerrar;
    @FXML
    private JFXTextField txEtapa;
    @FXML
    private JFXCheckBox cPesquisar;
    @FXML
    private TableColumn<Cursos, String> tabelaEtapa;
    @FXML
    private TableColumn<Cursos, LocalDate> tabelaData;
    @FXML
    private TableColumn<Cursos, LocalDate> tabelaEncerramento;
    @FXML
    private JFXComboBox<String> comboBox;
    @FXML
    private JFXDatePicker dtpdataini;
    @FXML
    private JFXDatePicker dtpdatafim;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        seta_tabela();
        seta_maskaras();        
        seta_combobox();
        estadoOriginal();
    }    

    private void seta_tabela(){
        
        tabelaNomeCurso.setCellValueFactory(new PropertyValueFactory("nomeCurso"));
        tabelaPreco.setCellValueFactory(new PropertyValueFactory("preco"));
        tabelaEtapa.setCellValueFactory(new PropertyValueFactory("etapa"));
        tabelaData.setCellValueFactory(new PropertyValueFactory("data_lancamento"));
        tabelaEncerramento.setCellValueFactory(new PropertyValueFactory("data_encerramento"));        
    }
    
    private void seta_maskaras(){
        
        MaskFieldUtil.maxField(txNomeCurso, 20);
        MaskFieldUtil.maxField(txDescricao, 50);
        MaskFieldUtil.maxField(txPreco, 7);
        MaskFieldUtil.maxField(txEtapa,10);
    }
    
    private void seta_combobox(){
        
        List<String> combo = new ArrayList();
        combo.add("Nome");
        combo.add("Etapa");
        combo.add("Data Lançamento");
        combo.add("Data encerramento");
        
        ObservableList<String> modelo = FXCollections.observableArrayList(combo);
        comboBox.setItems(modelo);
        comboBox.setValue("Nome");
        
        seta_pesquisa();
    }
    
    private void seta_pesquisa(){
    
        txPesquisa.clear();
        dtpdataini.setDisable(true);
        dtpdatafim.setDisable(true);
        comboBox.setValue("Nome");
        dtpdataini.setValue(LocalDate.now());
        dtpdatafim.setValue(LocalDate.now().plusDays(365));
    }
    
    private void carregaTabela(String filtro){
        
        DALCurso dalcurso = new DALCurso();
        List<Cursos> cursos = new ArrayList();
        cursos = dalcurso.get(filtro);
        ObservableList<Cursos> modelo;
        modelo = FXCollections.observableArrayList(cursos);
        tableview.setItems(modelo);
     
        
    }
    
    private void estadoOriginal(){
     
        pndados.setDisable(true);
        btConfirmar.setDisable(true);
        btCancelar.setDisable(false);
        btAlterar.setDisable(true);
        btExcluir.setDisable(true);
        btNovo.setDisable(false);
        pnpesquisa.setDisable(false);
        
        ObservableList <Node> componentes=pndados.getChildren(); 
        for(Node n : componentes)
        {
            if (n instanceof TextInputControl){
                 ((TextInputControl)n).setText("");
                  setTextFieldnormal((JFXTextField)(TextInputControl)n);
            }
               
            if(n instanceof ComboBox)
                ((ComboBox)n).getItems().clear();
        }
      
        dtpDataEnc.setValue(null);
        dtpDataLanc.setValue(LocalDate.now());
        carregaTabela("");
    }
    
    private void estadoedicao(){
        
        pndados.setDisable(false);
        btConfirmar.setDisable(false);
        btExcluir.setDisable(true);
        btExcluir.setDisable(true);
        txNomeCurso.requestFocus();
        pnpesquisa.setDisable(false);
    }
    
    @FXML
    private void evtNovo(ActionEvent event) {
        
        estadoedicao();
        cEncerrar.setSelected(false);
        dtpDataEnc.setDisable(true);
    }

    @FXML
    private void evtAlterar(ActionEvent event) {
        
        if(tableview.getSelectionModel().getSelectedItem() != null){
        
            Cursos c = tableview.getSelectionModel().getSelectedItem();
            
            txId.setText(""+c.getCursoID());
            txNomeCurso.setText(c.getNomeCurso());
            txDescricao.setText(c.getDescricao());
            txPreco.setText(""+c.getPreco());
            txEtapa.setText(c.getEtapa());
            dtpDataEnc.setValue(c.getData_encerramento());
            dtpDataLanc.setValue(c.getData_lancamento());
            
            if(c.getData_encerramento() != null){
                
                dtpDataEnc.setDisable(false);
                cEncerrar.setSelected(true);
            }
            
            estadoedicao();
        }
        
    }

    @FXML
    private void evtExcluir(ActionEvent event) {
        
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES,ButtonType.NO);
        boolean ok = true;
        
        if(a.showAndWait().get() == ButtonType.YES){
            
            Cursos c = new Cursos();
            DALCurso dalc = new DALCurso();
            
            c = tableview.getSelectionModel().getSelectedItem();
            
            try{
                
                Banco.getCon().getConnect().setAutoCommit(false);
                
                ok = dalc.apagar(c.getCursoID());
                
                if(ok){
                    
                     Banco.getCon().getConnect().commit();
                    a =  new Alert(Alert.AlertType.CONFIRMATION," Exclusão ocorrida com sucesso!!", ButtonType.OK);
                }
                else{
                    
                    Banco.getCon().getConnect().rollback();
                    a =  new Alert(Alert.AlertType.ERROR, "Erro ao deletar curso, verificar em turmas e lista de espera", ButtonType.OK);
                }
                
                a.showAndWait();
                
            }catch(SQLException ex){System.out.println(ex.getMessage());}
            
            estadoedicao();    
            carregaTabela("");
        }
    }

     private void setTextFieldErro(JFXTextField nome){
        
        nome.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
    }
    
    private void setTextFieldnormal(JFXTextField nome){
    
        nome.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
        
    }
    
     
    private boolean validarNome(String nome){
        
        boolean ok = true;
        
        if(nome.isEmpty()){
          
            ok = false;
            setTextFieldErro(txNomeCurso);
            Alert a = new Alert(Alert.AlertType.WARNING, "Nome do curso obrigatório!!", ButtonType.CLOSE);
            txNomeCurso.requestFocus();
            a.showAndWait();
        }
        else
            setTextFieldnormal(txNomeCurso);
        
        return ok;
    }
    
    private boolean validaPrecoTam(String preco){
        
        boolean ok = false;
        int i;
        
        for (i = 0;  i<preco.length() && preco.charAt(i) != '.' ; i++){}
            
        
        if(i <=4 )
            ok = true;
        
        return ok;
    }
    
    private boolean validaPreco(String preco){
        
        double preco_inserido = 0;
        Alert a = null;
        boolean ok = true,problema = false,tamanho = true;
        
        try{
            
            preco_inserido = Double.parseDouble(preco);
            tamanho = validaPrecoTam(preco);
            
        }catch(NumberFormatException ex){problema = true;}
        
        if(preco.isEmpty()){
            
            ok = false;
            setTextFieldErro(txPreco);
            a = new Alert(Alert.AlertType.WARNING, "Preço obrigatório!!", ButtonType.CLOSE);
            txPreco.requestFocus();
            
        }
        else if(!preco.isEmpty() && problema){
            
            ok = false;
            setTextFieldErro(txPreco);
            a = new Alert(Alert.AlertType.WARNING, "Preço inválido!", ButtonType.CLOSE);
            txPreco.requestFocus();
        }
        else if(!preco.isEmpty() && preco_inserido <=0){
            
            ok = false;
            setTextFieldErro(txPreco);
            a = new Alert(Alert.AlertType.WARNING, "Preço igual ou menor que 0!", ButtonType.CLOSE);
            txPreco.requestFocus();
        }
        else if(!tamanho){
            
            ok = false;
            setTextFieldErro(txPreco);
            a = new Alert(Alert.AlertType.WARNING, "É permitido apenas 4 digitos antes da casa decimal!", ButtonType.CLOSE);
            txPreco.requestFocus();
            
        }
        else
            setTextFieldnormal(txPreco);
        
        if(a != null)
               a.showAndWait();
        return ok;
    }
    
    private boolean validaEtapa(String etapa){
        
        boolean ok = true;
        
        if(etapa.isEmpty()){
          
            ok = false;
            setTextFieldErro(txEtapa);
            Alert a = new Alert(Alert.AlertType.WARNING, "Etapa obrigatória!!", ButtonType.CLOSE);
            txEtapa.requestFocus();
            a.showAndWait();
        }
        else
            setTextFieldnormal(txEtapa);
        
        return ok;
    }
    
    private boolean validaDataLanc(LocalDate date_lanc){
        
        boolean ok = true;
         
        if(date_lanc == null){
            
            ok = false;
       
            Alert a = new Alert(Alert.AlertType.WARNING, "Data do lançamento do curso obrigatório!!", ButtonType.CLOSE);
            dtpDataLanc.requestFocus();
            a.showAndWait();
        }
        
        return ok;
    }
    
    private boolean validaDataEncerrameento(LocalDate date_ence){
        
        boolean ok = true;
        Alert a = null;
        
        if(date_ence == null){
                          
            ok = false;
            a= new Alert(Alert.AlertType.WARNING, "Data do encerramento do curso obrigatório!!", ButtonType.CLOSE);
            dtpDataEnc.requestFocus();
          
        }
        else if(date_ence.isBefore(dtpDataLanc.getValue())){
            
            ok = false;
            a = new Alert(Alert.AlertType.WARNING, "Data do encerramento está antes da data de lançamento!!", ButtonType.CLOSE);
            dtpDataEnc.requestFocus();
            
        }
        
        if(a != null)
            a.showAndWait();
        return ok;
    }
    
    
    @FXML
    private void evtConfirmar(ActionEvent event) {
             
        double preco = 0;
        int cod;
        boolean ok = true,encerrado = false,validado = false;
        
        
        if (validarNome(txNomeCurso.getText())) {

            if (validaPreco(txPreco.getText())) {

                if (validaDataLanc(dtpDataLanc.getValue())) {

                    if (cEncerrar.isSelected()) {
                        encerrado = true;
                    }

                    if (encerrado) {
                        validado = validaDataEncerrameento(dtpDataEnc.getValue());
                    }

                    if (!encerrado || validado) {

                        if (validaEtapa(txEtapa.getText())) {

                            try {

                                preco = Double.parseDouble(txPreco.getText());
                                cod = Integer.parseInt(txId.getText());
                                    
                                
                            } catch (NumberFormatException ex) {
                                cod = 0;
                            }

                            DALCurso dalc = new DALCurso();
                            Cursos c = new Cursos();

                            c.setCursoID(cod);
                            c.setNomeCurso(txNomeCurso.getText());
                            c.setPreco(preco);
                            c.setEtapa(txEtapa.getText());
                            c.setData_lancamento(dtpDataLanc.getValue());

                            if(encerrado)
                                c.setData_encerramento(dtpDataEnc.getValue());
                            
                            if(!encerrado && dtpDataEnc.getValue() != null)
                                c.setData_encerramento(null);
                            
                            if (!txDescricao.getText().isEmpty()) {
                                c.setDescricao(txDescricao.getText());
                            }

                            Alert a = null;
                            try {

                                Banco.getCon().getConnect().setAutoCommit(false);

                                if (cod == 0) { // inserindo

                                    ok = dalc.gravar(c);

                                    if (ok) {
                                        a = new Alert(Alert.AlertType.CONFIRMATION, "Curso inserido!!", ButtonType.OK);
                                    } else {
                                        a = new Alert(Alert.AlertType.ERROR, "Problemas ao inserir o curso!!", ButtonType.OK);
                                    }

                                } else { // alterando

                                    ok = dalc.alterar(c);

                                    if (ok) {
                                        a = new Alert(Alert.AlertType.CONFIRMATION, "Curso atualizado!!", ButtonType.OK);
                                    } else {
                                        a = new Alert(Alert.AlertType.ERROR, "Problemas ao atualizar o curso!!", ButtonType.OK);
                                    }

                                }

                                a.showAndWait();
                                if (ok) {

                                    Banco.getCon().getConnect().commit();
                                    estadoOriginal();
                                    carregaTabela("");
                                } else {
                                    Banco.getCon().getConnect().rollback();
                                }

                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                }

            }

        }

    }

    @FXML
    private void evtCancelar(ActionEvent event) {
        
        if (!pndados.isDisabled())
        {
            estadoOriginal();
        } 
        else{
            
            FXMLPrincipalController.snprincipal.setCenter(null);
            FXMLPrincipalController.nome.setText("");
           
        }
          
    }

    @FXML
    private void evtPesquisa(ActionEvent event) {
        
        String filtro = comboBox.getSelectionModel().getSelectedItem(),texto;
        String sql = "";
        LocalDate dataini = null,datafim = null;
        
        if(cPesquisar.isSelected()){
        
            sql = "dataencerramento is not null AND";
            
            if(filtro.contains("Data")){
                
                dataini = dtpdataini.getValue();
                datafim = dtpdatafim.getValue();
                
                if(filtro.equals("Data encerramento"))
                   sql +=" dataencerramento BETWEEN '"+dataini+"' AND '"+datafim+"'";                 
                else
                  sql +=" datalancamento BETWEEN '"+dataini+"' AND '"+datafim+"'";
                
            }
            else{
                
                texto = txPesquisa.getText();
                
                if(filtro.equals("Nome"))
                    sql +=" upper(nomecurso) like '%"+texto.toUpperCase()+"%'";
                else
                    sql += " upper(etapa) like '%" + texto.toUpperCase() + "%'";

            }
        } else {
            
            sql = "dataencerramento is null AND";
            
            if(filtro.contains("Data")){
                
                dataini = dtpdataini.getValue();
                datafim = dtpdatafim.getValue();
                
                sql +=" datalancamento BETWEEN '"+dataini+"' AND '"+datafim+"'";
            }
            else{
                
                texto = txPesquisa.getText();
                
                if(filtro.equals("Nome"))
                    sql +=" upper(nomecurso) like '%"+texto.toUpperCase()+"%'";
                else
                    sql += " upper(etapa) like '%" + texto.toUpperCase() + "%'";
                
            }
        }
        
      carregaTabela(sql);
        
    }

    @FXML
    private void clicktabela(MouseEvent event) {
        
         if(tableview.getSelectionModel().getSelectedIndex()>=0)
        {
           btAlterar.setDisable(false);
           btExcluir.setDisable(false);
        }
    }

    @FXML
    private void evtEncerrar(MouseEvent event) {
        
        if(cEncerrar.isSelected())
            dtpDataEnc.setDisable(false);
        else
            dtpDataEnc.setDisable(true);
    }

    @FXML
    private void evtComboBox(ActionEvent event) {
        
         String filtro = comboBox.getSelectionModel().getSelectedItem();
        
        if(filtro.contains("Data")){
            
            txPesquisa.setDisable(true);
            dtpdataini.setDisable(false);
            dtpdatafim.setDisable(false);
            
            if(filtro.equals("Data encerramento")){
                
                cPesquisar.setSelected(true);
                cPesquisar.setDisable(true);
            }
            else{
                
                cPesquisar.setSelected(false);
                cPesquisar.setDisable(false);
            }
           
        }
        else{
            
            txPesquisa.setDisable(false);
            dtpdatafim.setDisable(true);
            dtpdataini.setDisable(true);
        }
    }

    @FXML
    private void evtLimparFiltros(ActionEvent event) {
        
        seta_pesquisa();
        carregaTabela("");
    }
    
}
