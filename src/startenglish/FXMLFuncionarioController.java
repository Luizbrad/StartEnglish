/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;
import org.json.JSONObject;
import startenglish.db.DAL.DALEndereco;
import startenglish.db.DAL.DALFuncionario;
import startenglish.db.DAL.DALProfessor;
import startenglish.db.Entidades.Endereco;
import startenglish.db.Entidades.Funcionario;
import startenglish.db.Entidades.Professor;
import startenglish.db.util.Banco;
import startenglish.util.ConsultaAPI;
import startenglish.util.MaskFieldUtil;
import startenglish.util.ValidarCpf;

/**
 * FXML Controller class
 *
 * @author azeve
 */
public class FXMLFuncionarioController implements Initializable {

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
    private JFXTextField txNome;
    @FXML
    private JFXTextField txTelefone;
    @FXML
    private JFXTextField txEmail;
    @FXML
    private JFXTextField txRg;
    @FXML
    private JFXTextField txCpf;
    @FXML
    private JFXTextField txRua;
    @FXML
    private JFXTextField txCEP;
    @FXML
    private JFXTextField txId;
    @FXML
    private JFXTextField txBairro;
    @FXML
    private JFXTextField txNumero;
    @FXML
    private JFXTextField txCidade;
    @FXML
    private JFXTextField txIdFunc;
    @FXML
    private CheckBox checkProf;
    @FXML
    private JFXButton btPesquisar;
    @FXML
    private JFXTextField txPesquisa;
    @FXML
    private TableView<Funcionario> tabela;
    @FXML
    private TableColumn<Funcionario, String> tabelaNome;
    @FXML
    private TableColumn<Funcionario, String> tabelaCPF;
    @FXML
    private TableColumn<Funcionario, String> tabelaTelefone;
    @FXML
    private TableColumn<Funcionario, String> tabelaEmail;
    @FXML
    private TableColumn<Funcionario, Boolean> tabelaCheck;
    @FXML
    private AnchorPane pnBusca;
    @FXML
    private AnchorPane pnDados;
    
    private Funcionario func_alterando;
    private JSONObject json;
    private boolean errocep;
    @FXML
    private ComboBox<String> cbBusca;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
                
        tabelaNome.setCellValueFactory(new PropertyValueFactory("Nome"));
        tabelaCPF.setCellValueFactory(new PropertyValueFactory("cpf"));
        tabelaEmail.setCellValueFactory(new PropertyValueFactory("email"));
        tabelaTelefone.setCellValueFactory(new PropertyValueFactory("fone"));
        tabelaCheck.setCellValueFactory(new PropertyValueFactory("professor"));
        
        MaskFieldUtil.cepField(txCEP);
        MaskFieldUtil.cpfField(txCpf);
        MaskFieldUtil.foneField(txTelefone);
        MaskFieldUtil.maxField(txEmail, 30);
        MaskFieldUtil.maxField(txNome, 20);
        MaskFieldUtil.maxField(txRua, 30);
        MaskFieldUtil.maxField(txBairro, 50);
        MaskFieldUtil.numericField(txNumero);
        MaskFieldUtil.maxField(txCidade, 30);
        MaskFieldUtil.maxField(txRg,9);
        
        seta_combobox();
        
        EstadoOriginal();
        
    }
    
    private void seta_combobox()
    {
        List<String> listaPesquisa = new ArrayList<>();
        listaPesquisa.add("Nome");
        listaPesquisa.add("CPF");
        listaPesquisa.add("E-mail");
        
        cbBusca.setItems(FXCollections.observableArrayList(listaPesquisa));
        
        cbBusca.setValue("Nome");
        
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
        checkProf.setSelected(false);
        
        
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
        txNome.requestFocus();
        
    }
    
    private void CarregaTabela(String filtro)
    {
        DALFuncionario dalF = new DALFuncionario();
        DALProfessor dalP = new DALProfessor();
        Professor prof = new Professor();
        
        List <Funcionario> listaFunc = dalF.get(filtro);
        
        for (int i = 0; i < listaFunc.size() ; i++) {
            prof = dalP.get(listaFunc.get(i).getID());
            if(prof != null)
                listaFunc.get(i).setProfessor(Boolean.TRUE);
            else
                listaFunc.get(i).setProfessor(Boolean.FALSE);
            
        }
        ObservableList <Funcionario> modelo;
        modelo = FXCollections.observableArrayList(listaFunc);
        
        tabela.setItems(modelo);
    }
    
    private boolean isValidEmailRegex(String email){
        
        boolean IsEmailValid = false;
        
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pat = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(email);
        
        if(mat.matches())
            IsEmailValid = true;
        
        
        return IsEmailValid;
    }
    
    private boolean validaCEP(String CEP){
    
        boolean ok = true;
        
        Alert a = null;
        
        if(CEP.isEmpty()){
                      
            ok = false;
            //setTextFieldErro(txCEP);
            
            a = new Alert(Alert.AlertType.WARNING, "CEP não pode estar vazio!", ButtonType.CLOSE);
            txCEP.requestFocus();
                
        }
        else if(!CEP.isEmpty() && CEP.length()!= 9){
            
            ok = false;
            //setTextFieldErro(txCEP);
            
            a = new Alert(Alert.AlertType.WARNING, "CEP está imcompleto!", ButtonType.CLOSE);
            txCEP.requestFocus();
         
        }
        else{
         
            //setTextFieldnormal(txCEP);
            insereCampos();
        }
          
         
        if(a != null)
            a.showAndWait();      
        
        return ok;
    }
    
    
    private boolean insereCampos() 
    {
        String sjson=ConsultaAPI.consultaCep(txCEP.getText(),"json");
        json=new JSONObject(sjson);
        
          txBairro.setText("aguarde...");
       txRua.setText("aguarde...");
            txCidade.setText("aguarde...");

        if(json.has("erro")){
            
            Alert a = new Alert(Alert.AlertType.WARNING, "CEP não encontrado", ButtonType.CLOSE);
            a.showAndWait();
            txCEP.requestFocus();
            
            errocep = true;
            return false;
        
        }
        else{
            
            errocep = false;
            txRua.setText(json.getString("logradouro"));
            txCidade.setText(json.getString("localidade"));
            txBairro.setText(json.getString("bairro"));
            
        }
       
        return true;
    }

    @FXML
    private void evtNovo(ActionEvent event) {
        EstadoEdicao();
    }

    @FXML
    private void evtAlterar(ActionEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex() >= 0)
        {
            Funcionario f = (Funcionario)tabela.getSelectionModel().getSelectedItem();
            txIdFunc.setText(""+f.getID());
            txNome.setText(f.getNome());
            txRg.setText(f.getRg());
            txCpf.setText(f.getCpf());
            txEmail.setText(f.getEmail());
            txTelefone.setText(f.getFone());
            txId.setText(""+f.getEndereco().getEnderecoID());
            txRua.setText(f.getEndereco().getRua());
            txNumero.setText(""+f.getEndereco().getNumero());
            txCEP.setText(f.getEndereco().getCEP());
            txBairro.setText(f.getEndereco().getBairro());
            txCidade.setText(f.getEndereco().getCidade());
           
            
            func_alterando = f;
            DALProfessor dale = new DALProfessor();
            Professor p = new Professor();
            
            p = dale.get(f.getID());
            
            if(p != null)
            {
                checkProf.setSelected(true);
                func_alterando.setProfessor(Boolean.TRUE);
            }                
            else
            {
                checkProf.setSelected(false);
                func_alterando.setProfessor(Boolean.FALSE);
            }
                           
            EstadoEdicao();
        }
    }

    @FXML
    private void evtExcluir(ActionEvent event) {
        boolean ok = true;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Confirma a exclusão");
        if (a.showAndWait().get() == ButtonType.OK) {
            DALFuncionario dalf = new DALFuncionario();
            DALProfessor dalp = new DALProfessor();
            DALEndereco dale = new DALEndereco();
            Funcionario f;
            f = tabela.getSelectionModel().getSelectedItem();
            a = new Alert(Alert.AlertType.INFORMATION);
            try{

                   Banco.getCon().getConnect().setAutoCommit(false);

                   if(f.getProfessor())
                    {
                          ok = dalp.apagar(f.getID());
                    }
                   
                   if(ok)
                    ok = dalf.apagar(f);
                    if(ok){
                       ok = dale.apagar(f.getEndereco().getEnderecoID());
                    }
                    else
                       ok = false;
              }
              catch(SQLException ex){System.out.println(ex.getMessage()); ok = false;}

                       
             try{

                 if(ok){

                   a = new Alert(Alert.AlertType.CONFIRMATION,"Funcionário excluído!!", ButtonType.OK);
                   Banco.getCon().getConnect().commit();
                } 
                else{
                      a = new Alert(Alert.AlertType.CONFIRMATION,"Problemas ao deletar funcionário!!", ButtonType.OK);
                      Banco.getCon().getConnect().rollback();
                }
             }
             catch(SQLException ex){}
             
            a.showAndWait();
            CarregaTabela("");
        }
    }

    @FXML
    private void evtConfirmar(ActionEvent event) {
        int cod,codEnd;
        startenglish.util.ValidarCpf cpfvalida = new ValidarCpf();
        boolean ok = true;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        Endereco end = new Endereco();
        DALEndereco dale = new DALEndereco();
        DALFuncionario dalf = new DALFuncionario();
        DALProfessor dalp = new DALProfessor();
        
        try 
        {
            cod = Integer.parseInt(txIdFunc.getText());
            codEnd = Integer.parseInt(txId.getText());
        } 
        catch (Exception e) 
        {
            cod = 0;
            codEnd = 0;
        }
        
        Funcionario f = new Funcionario();
        f.setID(cod);
        end.setEnderecoID(codEnd);
        
        if(cod == 0)
        {
            if(!txNome.getText().isEmpty())
                if(!txCpf.getText().isEmpty() && cpfvalida.isCPF(txCpf.getText()))
                    if(!txCEP.getText().isEmpty() && validaCEP(txCEP.getText()))
                    {

                        f.setNome(txNome.getText());
                        f.setCpf(txCpf.getText());
                        if(!txRg.getText().isEmpty())
                            f.setRg(txRg.getText());
                        if(!txEmail.getText().isEmpty())
                            f.setEmail(txEmail.getText());
                        if(!txTelefone.getText().isEmpty())
                            f.setFone(txTelefone.getText());
                        if(!txRua.getText().isEmpty())                 
                            end.setRua(txRua.getText());
                        if(!txNumero.getText().isEmpty())
                            end.setNumero(Integer.parseInt(txNumero.getText()));
                        if(!txCEP.getText().isEmpty())
                            end.setCEP(txCEP.getText());
                        if(!txBairro.getText().isEmpty())
                            end.setBairro(txBairro.getText());
                        if(!txCidade.getText().isEmpty())
                            end.setCidade(txCidade.getText());
                        f.setEndereco(end);

                              try{

                                   Banco.getCon().getConnect().setAutoCommit(false);


                                   ok = dale.gravar(end); 
                                    if(ok){
                                        int aux = Banco.getCon().getMaxPK("endereco", "enderecoid");
                                        f.getEndereco().setEnderecoID(aux);
                                        ok = dalf.gravar(f);                                 
                                    }
                                    else
                                       ok = false;
                              }
                              catch(SQLException ex){System.out.println(ex.getMessage()); ok = false;}
                                
                              if(checkProf.isSelected())
                              {
                                  cod = Banco.getCon().getMaxPK("funcionario", "funcid");
                                  f.setID(cod);
                                  Professor p = new Professor(f);
                                  ok = dalp.gravar(p);
                              }
                              
                             try{

                                 if(ok){

                                   a = new Alert(Alert.AlertType.CONFIRMATION,"Funcionário salvo!!", ButtonType.OK);
                                   Banco.getCon().getConnect().commit();
                                } 
                                else{
                                      a = new Alert(Alert.AlertType.CONFIRMATION,"Problemas ao cadastrar funcionário!!", ButtonType.OK);
                                      Banco.getCon().getConnect().rollback();
                                }
                             }catch(SQLException ex){}
                             
                             
                        
                    }else{
                                a = new Alert(Alert.AlertType.ERROR,"CEP inválido ou inexistente",ButtonType.OK);
                                txCEP.requestFocus();
                                a.showAndWait();
                    }
                else
                {
                    a = new Alert(Alert.AlertType.ERROR,"CPF inválido ou inexistente",ButtonType.OK);
                    txCpf.requestFocus();
                    a.showAndWait();
                }
            else
            {
                a = new Alert(Alert.AlertType.WARNING,"Nome não informado",ButtonType.OK);
                txNome.requestFocus();
                a.showAndWait();
            }
        }else
        {
            if(!txNome.getText().isEmpty())
                if(!txCpf.getText().isEmpty() && cpfvalida.isCPF(txCpf.getText()))
                    if(!txCEP.getText().isEmpty() && validaCEP(txCEP.getText()))
                    {

                        f.setNome(txNome.getText());
                        f.setCpf(txCpf.getText());
                        if(!txRg.getText().isEmpty())
                            f.setRg(txRg.getText());
                        if(!txEmail.getText().isEmpty())
                            f.setEmail(txEmail.getText());
                        if(!txTelefone.getText().isEmpty())
                            f.setFone(txTelefone.getText());
                        if(!txRua.getText().isEmpty())                 
                            end.setRua(txRua.getText());
                        if(!txNumero.getText().isEmpty())
                            end.setNumero(Integer.parseInt(txNumero.getText()));
                        if(!txCEP.getText().isEmpty())
                            end.setCEP(txCEP.getText());
                        if(!txBairro.getText().isEmpty())
                            end.setBairro(txBairro.getText());
                        if(!txCidade.getText().isEmpty())
                            end.setCidade(txCidade.getText());
                        f.setEndereco(end);

                              try{

                                   Banco.getCon().getConnect().setAutoCommit(false);


                                   ok = dale.alterar(end); 
                                    if(ok){
                                       
                                       ok = dalf.alterar(f);                                 
                                    }
                                    else
                                       ok = false;
                              }
                              catch(SQLException ex){System.out.println(ex.getMessage()); ok = false;}
                                
                              
                              if(func_alterando.getProfessor() && !checkProf.isSelected())
                              {
                                  ok = dalp.apagar(f.getID());
                              }
                              else if(!func_alterando.getProfessor() && checkProf.isSelected())
                              {
                                  Professor prof = new Professor(f);
                                  ok = dalp.gravar(prof);
                              }
                              
                             try{

                                 if(ok){

                                   a = new Alert(Alert.AlertType.CONFIRMATION,"Funcionário salvo!!", ButtonType.OK);
                                   Banco.getCon().getConnect().commit();
                                } 
                                else{
                                      a = new Alert(Alert.AlertType.CONFIRMATION,"Problemas ao cadastrar funcionário!!", ButtonType.OK);
                                      Banco.getCon().getConnect().rollback();
                                }
                             }catch(SQLException ex){}
                             
                    }else{
                                a = new Alert(Alert.AlertType.ERROR,"CEP inválido ou inexistente",ButtonType.OK);
                                txCEP.requestFocus();
                                a.showAndWait();
                    }
                else
                {
                    a = new Alert(Alert.AlertType.ERROR,"CPF inválido ou inexistente",ButtonType.OK);
                    txCpf.requestFocus();
                    a.showAndWait();
                }
            else
            {
                a = new Alert(Alert.AlertType.WARNING,"Nome não informado",ButtonType.OK);
                txNome.requestFocus();
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
    }

    @FXML
    private void evtPesquisar(ActionEvent event) {
        String filtro = cbBusca.getSelectionModel().getSelectedItem();
        
        if(filtro.contains("Nome"))
            CarregaTabela("upper(nome) like '%"+txPesquisa.getText().toUpperCase()+"%'");
        if(filtro.contains("CPF"))
            CarregaTabela("cpf like '%"+txPesquisa.getText().toUpperCase()+"%'");
        if(filtro.contains("E-mail"))
            CarregaTabela("upper(email) like '%"+txPesquisa.getText().toUpperCase()+"%'");
    }

    @FXML
    private void evtClicarTabela(MouseEvent event) {
        if(tabela.getSelectionModel().getSelectedIndex()>=0){
            
            btAlterar.setDisable(false);
            btExcluir.setDisable(false);
        }
    }


    @FXML
    private void evtValidaCEP(MouseEvent event) {
        validaCEP(txCEP.getText());
    }

    @FXML
    private void evtcbBusca(ActionEvent event) {
    }
    
}
