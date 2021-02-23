/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
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
import startenglish.db.DAL.DALFuncionario;
import startenglish.db.DAL.DALLogin;
import startenglish.db.DAL.DALProfessor;
import startenglish.db.Entidades.Funcionario;
import startenglish.db.Entidades.Login;
import startenglish.db.Entidades.Professor;
import startenglish.db.util.Banco;
import startenglish.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author azeve
 */
public class FXMLControleLoginController implements Initializable {

    @FXML
    private JFXButton btNovo;
    @FXML
    private JFXButton btAlterar;
    @FXML
    private JFXButton btExcluir;
    @FXML
    private JFXButton btConfirmar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private AnchorPane pnDados;
    @FXML
    private JFXTextField txUsuario;
    private JFXTextField txStatus;
    @FXML
    private JFXPasswordField txSenha;
    @FXML
    private JFXComboBox<Funcionario> cbFuncionario;
    @FXML
    private AnchorPane pnBusca;
    @FXML
    private JFXButton btPesquisar;
    @FXML
    private JFXTextField txPesquisa;
    @FXML
    private TableView<Login> tabela;
    @FXML
    private TableColumn<Login, String> tabelaUser;
    @FXML
    private TableColumn<Login, String> tabelaSenha;
    @FXML
    private TableColumn<Login, Character> tabelaStatus;
    @FXML
    private TableColumn<Login, Integer> tabelaNivel;
    
    private List<Login> listaTabela;
    
    @FXML
    private JFXTextField txNivel;
    private Login logVelho;
    @FXML
    private ComboBox<String> cbStatus;
    @FXML
    private ComboBox<String> cbPesquisa;
    @FXML
    private ComboBox<String> cbStatusPesquisa;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
          //cbFuncionario.setStyle("-fx-text-fill: white;");
        
          tabelaUser.setCellValueFactory(new PropertyValueFactory("user"));
          tabelaSenha.setCellValueFactory(new PropertyValueFactory("senha"));
          tabelaStatus.setCellValueFactory(new PropertyValueFactory("status"));
          tabelaNivel.setCellValueFactory(new PropertyValueFactory("nivel"));
          
          
          MaskFieldUtil.maxField(txUsuario, 20);
          MaskFieldUtil.maxField(txSenha, 10);
          //MaskFieldUtil.maxField(txStatus, 1);
         
          seta_combobox();
       
        EstadoOriginal();
    }
    
    private void seta_combobox()
    {
        List<String> listaPesquisa = new ArrayList<>();
        listaPesquisa.add("Usuario");
        listaPesquisa.add("Status");
        
        cbPesquisa.setItems(FXCollections.observableArrayList(listaPesquisa));
        
        List<String> listaAux = new ArrayList<>();
        listaAux.add("Ativo");
        listaAux.add("Inativo");
        
        cbStatusPesquisa.setItems(FXCollections.observableArrayList(listaAux));
        
        
        cbPesquisa.setValue("Usuario");
        txPesquisa.setDisable(false);
        cbStatusPesquisa.setDisable(true);
    }
    
    private void EstadoOriginal()
    {        
        btNovo.setDisable(false);
        btAlterar.setDisable(true);
        btExcluir.setDisable(true);
        btCancelar.setDisable(false);
        btConfirmar.setDisable(true);
        pnBusca.setDisable(false);
        txPesquisa.clear();
        pnDados.setDisable(true);
        
        
        
        ObservableList <Node> componentes=pnDados.getChildren(); //”limpa” os componentes
        for(Node n : componentes)
        {
            if (n instanceof TextInputControl)  // textfield, textarea e htmleditor
                ((TextInputControl)n).setText("");
            if(n instanceof ComboBox)
                ((ComboBox)n).getItems().clear();
        }
      
        CarregaTabela("");
    }
    
    private void EstadoEdicao()
    {
        pnBusca.setDisable(true);
        pnDados.setDisable(false);
        btConfirmar.setDisable(false);
        btExcluir.setDisable(true);
        btAlterar.setDisable(true);
        txPesquisa.clear();
        txUsuario.requestFocus();
        
        List<String> listaStatus = new ArrayList<>();
        listaStatus.add("Ativo");
        listaStatus.add("Inativo");
        
        cbStatus.setItems(FXCollections.observableArrayList(listaStatus));
        
        try {
           Professor prof = new Professor();
           DALProfessor dalP = new DALProfessor();
           DALFuncionario dale = new DALFuncionario();
           List<Funcionario> lista = dale.get("");
           
           for (int i = 0; i < lista.size() ; i++) {
            prof = dalP.get(lista.get(i).getID());
            if(prof != null)
                lista.get(i).setProfessor(Boolean.TRUE);
            else
                lista.get(i).setProfessor(Boolean.FALSE);
            
        }
           ObservableList<Funcionario> modelo = FXCollections.observableArrayList(lista);
           cbFuncionario.setItems(modelo);
                      
        } catch (Exception e) {
        }
        
    }
    
    private void CarregaTabela(String filtro)
    {
        
        DALLogin dale = new DALLogin();
        List <Login> listaaux = dale.getList(filtro);
        //listaTabela = lista;
        ObservableList<Login> listaTabela;
        listaTabela = FXCollections.observableArrayList(listaaux);
        
        tabela.setItems(listaTabela);
        
    }

    @FXML
    private void evtNovo(ActionEvent event) {
        EstadoEdicao();
    }

    @FXML
    private void evtAlterar(ActionEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex() >= 0)
        {
            EstadoEdicao();
            
            Login l = (Login)tabela.getSelectionModel().getSelectedItem();
            logVelho = (Login)tabela.getSelectionModel().getSelectedItem();
            txUsuario.setText(l.getUser());
            txSenha.setText(l.getSenha());
            
            if(l.getStatus() == 'A')
                cbStatus.getSelectionModel().select(0);
            else cbStatus.getSelectionModel().select(1);
            
            cbFuncionario.getSelectionModel().select(0);
            cbFuncionario.getSelectionModel().select(l.getFunc());
            txNivel.setText(""+l.getNivel());

                           
            
        }
    }

    @FXML
    private void evtExcluir(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir permanentemente esses dados?", ButtonType.YES,ButtonType.NO);
        boolean ok = true;
        
        Login l = new Login();
        DALLogin dale = new DALLogin();
            
        l = tabela.getSelectionModel().getSelectedItem();
        if(l.getNivel()!=1)
        {
            if (a.showAndWait().get() == ButtonType.YES) {

                try {

                    Banco.getCon().getConnect().setAutoCommit(false);

                    ok = dale.apagar(l);

                    if (ok) {

                        Banco.getCon().getConnect().commit();
                        a = new Alert(Alert.AlertType.CONFIRMATION, " Exclusão realizada com sucesso!!", ButtonType.OK);
                    } else {

                        Banco.getCon().getConnect().rollback();
                        a = new Alert(Alert.AlertType.ERROR, "Erro ao deletar Login!", ButtonType.OK);
                    }

                    a.showAndWait();

                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

            } else {
                try {
                    Banco.getCon().getConnect().setAutoCommit(false);

                    ok = dale.exclusaologica(l);

                    if (ok) {

                        Banco.getCon().getConnect().commit();
                        a = new Alert(Alert.AlertType.CONFIRMATION, " Exclusão lógica realizada com sucesso!!", ButtonType.OK);
                    } else {

                        Banco.getCon().getConnect().rollback();
                        a = new Alert(Alert.AlertType.ERROR, "Erro ao deletar lógicamente!", ButtonType.OK);
                    }

                    a.showAndWait();
                } catch (Exception e) {
                }
            }
        }
        else{
            a = new Alert(Alert.AlertType.ERROR, "Não pode ser excluido, por ser o administrador do sistema!", ButtonType.OK);
            a.showAndWait();
        }
        
        
        //EstadoEdicao();    
        CarregaTabela("");
    }

    private boolean validaUsuario(String user)
    {
        boolean ok = false;
        Login auxLogin;
          
        if(!user.isEmpty())
        {
            DALLogin dale = new DALLogin();
            auxLogin = dale.verif(user);
            
            if(auxLogin == null)
                ok = true;
        }
                
        return ok;
    }
    
    @FXML
    private void evtConfirmar(ActionEvent event) {
        int nivel;
        boolean ok = true;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        
        DALLogin dale = new DALLogin();
        DALFuncionario dalf = new DALFuncionario();
        
        try 
        {
            nivel = Integer.parseInt(txNivel.getText());
        } 
        catch (Exception e) 
        {
            nivel = 0; 
        }
        
        Login l = new Login();
        Funcionario f = new Funcionario();
//        f.setID(cod);
//        end.setEnderecoID(codEnd);
        
        if(nivel == 0)
        {
            if(validaUsuario(txUsuario.getText()))
                if(!txSenha.getText().isEmpty())
                    if(cbFuncionario.getSelectionModel().getSelectedItem() != null)
                    {
                        f = cbFuncionario.getSelectionModel().getSelectedItem();
                        l.setUser(txUsuario.getText());
                        l.setSenha(txSenha.getText());
                        l.setFunc(f);
                        
                        if(cbStatus.getSelectionModel().getSelectedIndex()!=-1)
                        {
                            if(cbStatus.getSelectionModel().getSelectedIndex() == 0)
                                l.setStatus('A');
                            if(cbStatus.getSelectionModel().getSelectedIndex() == 1)
                                l.setStatus('I');
                        }
                        
//                        if(!txStatus.getText().isEmpty()) l.setStatus(txStatus.getText().charAt(0));
//                        else l.setStatus('A');
                        
                        if(l.getFunc().getProfessor())
                            l.setNivel(3);
                        else l.setNivel(2);
                        
                              try{

                                   Banco.getCon().getConnect().setAutoCommit(false);


                                   ok = dale.gravar(l); 
                                    
                              }
                              catch(SQLException ex){System.out.println(ex.getMessage()); ok = false;}
                              
                             try{

                                 if(ok){

                                   a = new Alert(Alert.AlertType.CONFIRMATION,"Novo Login salvo!!", ButtonType.OK);
                                   Banco.getCon().getConnect().commit();
                                } 
                                else{
                                      a = new Alert(Alert.AlertType.CONFIRMATION,"Problemas ao cadastrar novo login!!", ButtonType.OK);
                                      Banco.getCon().getConnect().rollback();
                                }
                             }catch(SQLException ex){}
                             
                             
                        
                    }else{
                                a = new Alert(Alert.AlertType.ERROR,"Selecione um funcionário para ser vinculado ao Login",ButtonType.OK);
                                cbFuncionario.requestFocus();
                                a.showAndWait();
                    }
                else
                {
                    a = new Alert(Alert.AlertType.ERROR,"Por favor insira uma senha válida",ButtonType.OK);
                    txSenha.requestFocus();
                    a.showAndWait();
                }
            else
            {
                a = new Alert(Alert.AlertType.WARNING,"Informe um usuario, para cadastrar seu novo login!",ButtonType.OK);
                txUsuario.requestFocus();
                a.showAndWait();
            }
        }else
        {
            
            if(!txUsuario.getText().isEmpty())
                if(!txSenha.getText().isEmpty())
                    if(cbFuncionario.getSelectionModel().getSelectedItem() != null)
                    {
                        f = cbFuncionario.getSelectionModel().getSelectedItem();
                        l.setUser(txUsuario.getText());
                        l.setSenha(txSenha.getText());
                        l.setFunc(f);
                        
                        if(cbStatus.getSelectionModel().getSelectedIndex()!=-1)
                        {
                            if(cbStatus.getSelectionModel().getSelectedIndex() == 0)
                                l.setStatus('A');
                            else
                                l.setStatus('I');
                        }
                        
//                        if(!txStatus.getText().isEmpty()) l.setStatus(txStatus.getText().charAt(0));
//                        else l.setStatus('A');
                        if(nivel!=1){
                            if(l.getFunc().getProfessor())
                                l.setNivel(3);
                            else l.setNivel(2); 
                        }
                        else l.setNivel(nivel);
                        
                                   ok = dale.alterar(l,logVelho.getUser()); 
                               
                             try{

                                 if(ok){

                                   a = new Alert(Alert.AlertType.CONFIRMATION,"Login alterado!!", ButtonType.OK);
                                   Banco.getCon().getConnect().commit();
                                } 
                                else{
                                      a = new Alert(Alert.AlertType.CONFIRMATION,"Problemas ao atualizar o login!!", ButtonType.OK);
                                      Banco.getCon().getConnect().rollback();
                                }
                             }catch(SQLException ex){}
                             
                             
                        
                    }else{
                                a = new Alert(Alert.AlertType.ERROR,"Selecione um funcionário para ser vinculado ao Login",ButtonType.OK);
                                cbFuncionario.requestFocus();
                                a.showAndWait();
                    }
                else
                {
                    a = new Alert(Alert.AlertType.ERROR,"Por favor insira uma senha válida",ButtonType.OK);
                    txSenha.requestFocus();
                    a.showAndWait();
                }
            else
            {
                a = new Alert(Alert.AlertType.WARNING,"Informe um usuario, para alterar seu login!",ButtonType.OK);
                txUsuario.requestFocus();
                a.showAndWait();
            }
        }
        
        if(ok)
        {
            a.showAndWait();
            EstadoOriginal();
            CarregaTabela(""); 
        }
        
    }

    @FXML
    private void evtCancelar(ActionEvent event) {
        if (!pnDados.isDisabled())
        {
            EstadoOriginal();
        }
         else{
            
            FXMLPrincipalController.snprincipal.setCenter(null);
            FXMLPrincipalController.nome.setText("");
           
        }
    }

    @FXML
    private void evtPesquisar(ActionEvent event) {
        String filtro = cbPesquisa.getSelectionModel().getSelectedItem();
        
        if(filtro.contains("Usuario"))
            CarregaTabela("upper(usuario) like '%"+txPesquisa.getText().toUpperCase()+"%'");
        else 
            if(cbStatusPesquisa.getSelectionModel().getSelectedIndex() == 0)
                CarregaTabela("upper(status) like 'A'");
            else
                CarregaTabela("upper(status) like 'I'");
    }

    @FXML
    private void evtClicarTabela(MouseEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            
            btAlterar.setDisable(false);
            btExcluir.setDisable(false);
        }
    }

    @FXML
    private void evtcbPesquisa(ActionEvent event) {
        String filtro = cbPesquisa.getSelectionModel().getSelectedItem();
        
        if(filtro.contains("Usuario")){
            
            txPesquisa.setDisable(false);
            cbStatusPesquisa.setDisable(true);           
        }
        else{
            
            txPesquisa.setDisable(true);
            cbStatusPesquisa.setDisable(false);
        }
    }
    
}
