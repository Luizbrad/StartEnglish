package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.json.JSONObject;
import startenglish.db.DAL.DALAluno;
import startenglish.db.DAL.DALEndereco;
import startenglish.db.DAL.DALLivro;
import startenglish.db.Entidades.Aluno;
import startenglish.db.Entidades.Livro;
import startenglish.db.util.Banco;
import startenglish.util.ConsultaAPI;
import startenglish.util.MaskFieldUtil;
import startenglish.util.ValidarCpf;

public class FXMLAlunoController implements Initializable {

    @FXML
    private TableView<Aluno> tabela;
    @FXML
    private TableColumn<Aluno,String> tabelaNome;
    @FXML
    private TableColumn<Aluno,String> tabelaCPF;
    @FXML
    private TableColumn<Aluno,String> tabelaTelefone;
    @FXML
    private TableColumn<Aluno,String> tabelaEmail;
    @FXML
    private JFXButton btNovo;
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
    private JFXTextField txBairro;
    @FXML
    private JFXTextField txNumero;
    @FXML
    private JFXTextField txCidade;
    @FXML
    private JFXButton btPesquisar;
    @FXML
    private JFXTextField txPesquisa;
    @FXML
    private JFXTextField txIDEnd;
    @FXML
    private JFXTextField txID;
    @FXML
    private JFXComboBox<String> cbFiltro;
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
    private JFXComboBox<String> cbIdade;
  
    private JSONObject json;
    private boolean errocep;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        tabelaNome.setCellValueFactory(new PropertyValueFactory("nome"));
        tabelaCPF.setCellValueFactory(new PropertyValueFactory("cpf"));
        tabelaEmail.setCellValueFactory(new PropertyValueFactory("email"));
        tabelaTelefone.setCellValueFactory(new PropertyValueFactory("fone"));
        
        MaskFieldUtil.maxField(txNome, 30);
        MaskFieldUtil.maxField(txCpf, 15);
        MaskFieldUtil.cpfField(txCpf);      
        MaskFieldUtil.maxField(txEmail, 30);
        MaskFieldUtil.maxField(txRg,20);
        MaskFieldUtil.foneField(txTelefone);
        MaskFieldUtil.maxField(txTelefone,20);
        MaskFieldUtil.maxField(txRua, 30);
        MaskFieldUtil.cepField(txCEP);
        MaskFieldUtil.maxField(txBairro, 50);
        MaskFieldUtil.numericField(txNumero);
        MaskFieldUtil.maxField(txNumero, 10);
        MaskFieldUtil.maxField(txCidade, 30);
        
        List<String> combo = new ArrayList();
        combo.add("Nome");
        combo.add("Cpf");
        combo.add("Email");
        
        ObservableList<String> modelo = FXCollections.observableArrayList(combo);
        cbFiltro.setItems(modelo);
        cbFiltro.setValue("Nome");
        
        EstadoOriginal();
    }    

    private void EstadoOriginal()
    {        
        btNovo.setDisable(false);
        btAlterar.setDisable(true);
        btExcluir.setDisable(true);
        btCancelar.setDisable(false);
        btConfirmar.setDisable(true);
        txPesquisa.clear();
        pndados.setDisable(true);
        ObservableList <Node> componentes=pndados.getChildren(); //”limpa” os componentes
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
        pndados.setDisable(false);
        btConfirmar.setDisable(false);
        btExcluir.setDisable(true);
        btAlterar.setDisable(true);
        txPesquisa.clear();
        txNome.requestFocus();
        
    }
    
    private void CarregaTabela(String filtro)
    {
        DALAluno dalL = new DALAluno();       
        List <Aluno> listaAluno = dalL.get(filtro);
        listaAluno = dalL.get(filtro);           
        ObservableList <Aluno> modelo;
        modelo = FXCollections.observableArrayList(listaAluno);       
        tabela.setItems(modelo);
    }
    
    private boolean isValidEmailRegex(String email)
    {       
        boolean IsEmailValid = false;
        
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pat = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher mat = pat.matcher(email);
        
        if(mat.matches())
            IsEmailValid = true;
        
        
        return IsEmailValid;
    }
    
    private boolean valida(String nome,String cpf,String rg,String email,String telefone,String numero,String CEP)
    {        
        boolean ok = true;
        Alert a=null;
        ValidarCpf c = new ValidarCpf();
        if(nome.isEmpty())
        {        
            ok = false;
            txNome.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txNome.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Nome do aluno obrigatório!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
            
        }
        else
        {
            txNome.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
        }
        if(cpf.isEmpty())
        {
           ok = false;
            txCpf.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txCpf.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "CPF obrigatório!!", ButtonType.CLOSE);           
                a.showAndWait(); 
            }
        }
        else if(!cpf.isEmpty() && !c.isCPF(cpf))
        {
            ok = false;
            txCpf.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txCpf.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "CPF inválido!!", ButtonType.CLOSE);           
                a.showAndWait(); 
            }
        }
        else
        {
          txCpf.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");  
        }
        if(rg.isEmpty())
        {
          ok = false;
            txRg.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txRg.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Rg obrigatório!!", ButtonType.CLOSE);
                a.showAndWait(); 
            }
        }
        else
        {
           txRg.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");                    
        }
        if(email.isEmpty())
        {
          ok = false;
            txEmail.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txEmail.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Email obrigatório!!", ButtonType.CLOSE);
                a.showAndWait(); 
            }
        }
        else if(!email.isEmpty() && !isValidEmailRegex(email))
        {           
            ok = false;
                       
            txEmail.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            if(a==null)
            {
                a = new Alert(Alert.AlertType.WARNING, "E-mail inválido!", ButtonType.CLOSE);
                txEmail.requestFocus();
                a.showAndWait(); 
            }
        }
        else
        {
           txEmail.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");                    
        }
        if(telefone.isEmpty())
        {           
        
          ok = false;
            txTelefone.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txTelefone.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Telefone obrigatório!!", ButtonType.CLOSE);
                a.showAndWait(); 
            }
        }
        else if(!telefone.isEmpty() && telefone.length()!= 14 && telefone.length()!=13)
        {           
            ok = false;
            txTelefone.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            if(a==null)
            {
                txTelefone.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Telefone está incorreto!", ButtonType.CLOSE);
                txTelefone.requestFocus();
            }
        }
        else
        {
           txTelefone.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");                    
        }
        
        if(numero.isEmpty())
        {
          ok = false;
            txNumero.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txNumero.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Número obrigatório!!", ButtonType.CLOSE);
                a.showAndWait(); 
            }
        }
        else
        {
            try
            {
                Integer.parseInt(numero);
                txNumero.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");  
            }
            catch (NumberFormatException ex) 
            {
                ok=false;
                txNumero.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
                if(a==null)
                {
                    txNumero.requestFocus();
                    a = new Alert(Alert.AlertType.WARNING, "Número inválido!!", ButtonType.CLOSE);
                    a.showAndWait(); 
                }              
            }
        }
        if(CEP.isEmpty())
        {
          ok = false;
            txCEP.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txCEP.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "CEP obrigatório!!", ButtonType.CLOSE);
                a.showAndWait(); 
            }
        }
        else if(!CEP.isEmpty() && CEP.length()!= 9)
        {
            
            ok = false;
            txCEP.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            a = new Alert(Alert.AlertType.WARNING, "CEP está incorreto!", ButtonType.CLOSE);
            txCEP.requestFocus();
         
        }
        else
        {
           txCEP.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");                    
        }
        
        
        
        return ok;
    }
    
    @FXML
    private void evtNovo(ActionEvent event) 
    {
        EstadoEdicao();
    }

    @FXML
    private void evtAlterar(ActionEvent event) 
    {
        if(tabela.getSelectionModel().getSelectedItem() != null)
        {     
            Aluno a = tabela.getSelectionModel().getSelectedItem();
            
            txID.setText(""+a.getID());
            txNome.setText(a.getNome());
            txCpf.setText(a.getCpf());
            txEmail.setText(a.getEmail());
            txRg.setText(a.getRg());
            txTelefone.setText(a.getFone());
            txBairro.setText(a.getEndereco().getBairro());
            txCEP.setText(a.getEndereco().getCEP());
            txCidade.setText(a.getEndereco().getCidade());
            txRua.setText(a.getEndereco().getRua());
            txIDEnd.setText(""+a.getEndereco().getEnderecoID());
            txNumero.setText(""+a.getEndereco().getNumero());
            
            EstadoEdicao();
        }
    }

    @FXML
    private void evtExcluir(ActionEvent event) 
    {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES,ButtonType.NO);
        boolean ok = true;
        
        if(a.showAndWait().get() == ButtonType.YES)
        {           
            Aluno alu = new Aluno();
            DALAluno dal = new DALAluno();
            
            alu = tabela.getSelectionModel().getSelectedItem();
            
            try{
                
                Banco.getCon().getConnect().setAutoCommit(false);
                
                ok = dal.apagar(alu);
                
                if(ok)
                {                   
                     Banco.getCon().getConnect().commit();
                    a =  new Alert(Alert.AlertType.CONFIRMATION," Exclusão ocorrida com sucesso!!", ButtonType.OK);
                }
                else
                {                  
                    Banco.getCon().getConnect().rollback();
                    a =  new Alert(Alert.AlertType.ERROR, "Erro ao deletar aluno! Verifique a Agenda de Provas, Lista de Espera e Matrícula!", ButtonType.OK);
                }
                
                a.showAndWait();
                
            }catch(SQLException ex){System.out.println(ex.getMessage());}
            
            EstadoOriginal();    
            CarregaTabela("");
        } 
    }

    @FXML
    private void evtConfirmar(ActionEvent event) 
    {
        String CEP = txCEP.getText();
        
        if(!CEP.isEmpty() && CEP.length()== 9)
            errocep=false;
        if(valida(txNome.getText(),txCpf.getText(),txRg.getText(),txEmail.getText(),txTelefone.getText(),txNumero.getText(),txCEP.getText()) && !errocep)
        {
                int number = 0;
                int cod,codEnd;
                try 
                {
                    number = Integer.parseInt(txNumero.getText());
                    cod = Integer.parseInt(txID.getText());
                    codEnd=Integer.parseInt(txIDEnd.getText());                                       
                }
                catch (NumberFormatException ex) 
                {
                    cod = 0;     
                    codEnd=0;
                }
                DALAluno daa = new DALAluno();
                DALEndereco dale = new DALEndereco();
                Aluno alu = new Aluno();
                alu.setNome(txNome.getText());
                alu.setCpf(txCpf.getText());
                alu.setRg(txRg.getText());
                alu.setEmail(txEmail.getText());
                alu.setFone(txTelefone.getText());
                alu.setID(cod);
                alu.getEndereco().setCEP(txCEP.getText());
                alu.getEndereco().setNumero(number);
                alu.getEndereco().setEnderecoID(codEnd);
                if (!txRua.getText().isEmpty())
                {
                    alu.getEndereco().setRua(txRua.getText());
                }
                if (!txBairro.getText().isEmpty()) 
                {
                    alu.getEndereco().setBairro(txBairro.getText());
                }
                if (!txCidade.getText().isEmpty()) 
                {
                    alu.getEndereco().setCidade(txCidade.getText());
                }
                
                Alert a = null;
                boolean ok;
                try
                {
                    Banco.getCon().getConnect().setAutoCommit(false);

                    if (cod == 0)
                    {
                        ok = dale.gravar(alu.getEndereco());
                        if(ok)
                        {
                            codEnd = Banco.getCon().getMaxPK("Endereco", "EnderecoID");
                            alu.getEndereco().setEnderecoID(codEnd);
                            ok = daa.gravar(alu);

                            if (ok)
                            {
                                a = new Alert(Alert.AlertType.CONFIRMATION, "Aluno inserido!!", ButtonType.OK);
                            } 
                            else 
                            {
                                a = new Alert(Alert.AlertType.ERROR, "Problemas ao inserir o aluno!!", ButtonType.OK);
                            }
                        }
                        else
                        {
                            a = new Alert(Alert.AlertType.ERROR, "Problemas ao inserir o endereço do aluno!!", ButtonType.OK);
                        }
                        

                    } 
                    else
                    { 
                        ok = dale.alterar(alu.getEndereco());
                        if(ok)
                        {
                           ok = daa.alterar(alu);

                            if(ok)
                            {
                                a = new Alert(Alert.AlertType.CONFIRMATION, "Aluno atualizado!!", ButtonType.OK);
                            } 
                            else
                            {
                                a = new Alert(Alert.AlertType.ERROR, "Problemas ao atualizar o aluno!!", ButtonType.OK);
                            } 
                        }
                        else
                        {
                            a = new Alert(Alert.AlertType.ERROR, "Problemas ao atualizar o endereço do aluno!!", ButtonType.OK);
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
        if (!pndados.isDisabled())
        {
            EstadoOriginal();
        } 
        else{
            
            FXMLPrincipalController.snprincipal.setCenter(null);
            FXMLPrincipalController.nome.setText("");
           
        }
    }

    /*@FXML
    private void evtSearchCEP(MouseEvent event)
    {
        String CEP = txCEP.getText();
        if(!CEP.isEmpty() && CEP.length()== 9)
        {
            String sjson=ConsultaAPI.consultaCep(txCEP.getText(),"json");
            json=new JSONObject(sjson);
        
            txBairro.setText("Aguarde...");
            txRua.setText("Aguarde...");
            txCidade.setText("Aguarde...");

            if(json.has("erro"))
            {           
                Alert a = new Alert(Alert.AlertType.WARNING, "CEP não encontrado", ButtonType.CLOSE);
                a.showAndWait();
                txCEP.requestFocus();

                errocep = true;
            }
            else
            {
                errocep = false;
                txRua.setText(json.getString("logradouro"));
                txCidade.setText(json.getString("localidade"));
                txBairro.setText(json.getString("bairro"));
            }
        }
    }*/

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
    private void evtPesquisar(ActionEvent event)
    {
        if(cbFiltro.getSelectionModel().getSelectedItem().equals("Nome"))
            CarregaTabela("Aluno.nome like '%"+txPesquisa.getText()+"%'");
        else if(cbFiltro.getSelectionModel().getSelectedItem().equals("Cpf"))
            CarregaTabela("Aluno.cpf like '%"+txPesquisa.getText()+"%'");
        else
            CarregaTabela("Aluno.email like '%"+txPesquisa.getText()+"%'");
    }

    
}
