package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.awt.image.BufferedImage;
import java.io.File;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.json.JSONObject;
import startenglish.db.DAL.DALEndereco;
import startenglish.db.DAL.DALParametrizacao;
import startenglish.db.Entidades.Endereco;
import startenglish.db.Entidades.Parametrizacao;
import startenglish.db.util.Banco;
import startenglish.util.ConsultaAPI;
import startenglish.util.MaskFieldUtil;


public class FXMLParametrizacaoController implements Initializable {

    // Botões e text fields
    @FXML
    private JFXButton btNovo;
    @FXML
    private JFXButton btAlterar;
    @FXML
    private JFXButton btApagar;
    @FXML
    private JFXButton btConfirmar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private JFXTextField txNome;
    @FXML
    private JFXTextField txTelefone;
    @FXML
    private JFXTextField txRazao;
    @FXML
    private JFXTextField txEmail;
    @FXML
    private JFXTextField txIDendereco;
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
    private JFXTextField txCNPJ;
    @FXML
    private JFXButton btEscolher;
    
    // tabela
    @FXML
    private TableView<Parametrizacao> tabela;
    @FXML
    private TableColumn<Parametrizacao, String> colunaNome;
    @FXML
    private TableColumn<Parametrizacao, String> colunaTelefone;
    @FXML
    private TableColumn<Parametrizacao, String> colunaCNPJ;
    @FXML
    private TableColumn<Parametrizacao, String> colunaRazaoSocial;
       
    // outros
    @FXML
    private AnchorPane pndados;
    @FXML
    private ImageView img;

    private File arq;
    private Image aux;
    private String nome_antigo;
    private boolean flag; 
    private JSONObject json;
    private boolean errocep;
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        seta_tabela();
        seta_mascaras();
        estadoOriginal();
        errocep = false;
    }    

    private void seta_tabela(){
        
        colunaNome.setCellValueFactory(new PropertyValueFactory("nome"));
        colunaCNPJ.setCellValueFactory(new PropertyValueFactory("CNPJ"));
        colunaRazaoSocial.setCellValueFactory(new PropertyValueFactory("RazaoSocial"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory("telefone"));
    }
    
    private void seta_mascaras(){
     
        MaskFieldUtil.maxField(txNome, 30);
        MaskFieldUtil.foneField(txTelefone);
        MaskFieldUtil.maxField(txRazao, 30);
        MaskFieldUtil.maxField(txEmail, 30);
        MaskFieldUtil.maxField(txRua, 30);
        MaskFieldUtil.cepField(txCEP);
        MaskFieldUtil.maxField(txBairro, 50);
        MaskFieldUtil.numericField(txNumero);
        MaskFieldUtil.maxField(txNumero, 10);
        MaskFieldUtil.maxField(txCidade, 30);
        MaskFieldUtil.cnpjField(txCNPJ);
    }
    
    private void carregarTabela(){
     
        DALParametrizacao dalpar = new DALParametrizacao();
        List<Parametrizacao> para = new ArrayList();
        Parametrizacao p = dalpar.get();
        para.add(p);
        
        if(para.get(0) == null)
             btNovo.setDisable(false);
        else
             btNovo.setDisable(true);
        
        ObservableList<Parametrizacao> modelo;
        modelo = FXCollections.observableArrayList(para);
        tabela.setItems(modelo);
    }
    
    private void estadoOriginal(){
               
        flag = false;
        pndados.setDisable(true);
        btConfirmar.setDisable(true);
        btCancelar.setDisable(false);
        btApagar.setDisable(true);
        btAlterar.setDisable(true);
        btNovo.setDisable(false);
        txIDendereco.setDisable(true);
        
        ObservableList <Node> componentes=pndados.getChildren(); 
        for(Node n : componentes)
        {
            if (n instanceof TextInputControl){
                 setTextFieldnormal((JFXTextField)(TextInputControl)n);
                  ((TextInputControl)n).setText("");
            }
               
            
            if(n instanceof ComboBox)
                ((ComboBox)n).getItems().clear();
        }
        
        carregarTabela();
    }
    
    private void estadoedicao(){
        
        pndados.setDisable(false);
        btConfirmar.setDisable(false);
        btApagar.setDisable(true);
        btAlterar.setDisable(true);
        txNome.requestFocus();
         
    }
    
    @FXML
    private void evtNovo(ActionEvent event) {
        
          estadoedicao();    
    }

    @FXML
    private void evtAlterar(ActionEvent event) {
               
        if(tabela.getSelectionModel().getSelectedItem() != null){
            
             DALParametrizacao dalpar = new DALParametrizacao();
             DALEndereco dale = new DALEndereco();
             Parametrizacao p;
             Endereco e;
             
             p = tabela.getSelectionModel().getSelectedItem();
        
             txNome.setText(p.getNome());
             txCNPJ.setText(p.getCNPJ());
             txTelefone.setText(p.getTelefone());
             txRazao.setText(p.getRazaoSocial());
             txEmail.setText(p.getEmail());
             nome_antigo = txNome.getText();
             e = dale.get(p.getEndereco().getEnderecoID());
             
             txIDendereco.setText(""+e.getEnderecoID());
             if(!e.getRua().isEmpty())
                  txRua.setText(e.getRua());
             txCEP.setText(e.getCEP());
             
            if(!e.getBairro().isEmpty())
                txBairro.setText(e.getBairro());
            if(e.getNumero() != 0)
                txNumero.setText(""+e.getNumero());
            if(!e.getCidade().isEmpty())
                txCidade.setText(e.getCidade());
               
            InputStream in;  
            in = dalpar.getFoto(p.getNome());
            
            if(in != null){
                
                BufferedImage bimagem;
                try 
                {           
                    bimagem = ImageIO.read(in);
                    img.setImage(SwingFXUtils.toFXImage(bimagem, null));
                } 
                catch (IOException ex) 
                {
                    Logger.getLogger(FXMLParametrizacaoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
               estadoedicao();
             
        }
    }

    @FXML
    private void evtApagar(ActionEvent event) {
        
        if(tabela.getItems().get(0) != null){
         
              boolean ok = true;
          Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES,ButtonType.NO),b;
          
          if(a.showAndWait().get() == ButtonType.YES){
              
              DALParametrizacao dalpar = new DALParametrizacao();
              DALEndereco dale = new DALEndereco();
              Parametrizacao p;
              
              p = tabela.getSelectionModel().getSelectedItem();
              
              try{
                  
                   Banco.getCon().getConnect().setAutoCommit(false);
                   
                   ok = dalpar.apagar(p);
                   
                    if(ok){

                       ok = dale.apagar(p.getEndereco().getEnderecoID());
                                   
                    }
                    else
                       ok = false;
              }
              catch(SQLException ex){System.out.println(ex.getMessage()); ok = false;}
                    
             try{
                 
                 if(ok){
                 
                   b = new Alert(Alert.AlertType.CONFIRMATION,"Parametrizacao Excluído!!", ButtonType.OK);
                   b.showAndWait();
                   Banco.getCon().getConnect().commit();
                   FXMLPrincipalController.desabilita_parametri(true);
                   FXMLPrincipalController.chamaimagem();
                } 
                else{
                     
                    b = new Alert(Alert.AlertType.ERROR,"Problemas ao deletar a parametrizacao, verificar no banco!!", ButtonType.OK);
                    b.showAndWait();
                    Banco.getCon().getConnect().rollback();
                }
             }
             catch(SQLException ex){}
             
               carregarTabela();
          }
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
    
    private boolean validaNome(String nome){
    
        boolean ok = true;
        
        if(nome.isEmpty()){
                      
            ok = false;
                     
            setTextFieldErro(txNome);
            Alert a = new Alert(Alert.AlertType.WARNING, "Nome da empresa não pode estar vazio!", ButtonType.CLOSE);
            txNome.requestFocus();
            a.showAndWait();      
        }
        else
            setTextFieldnormal(txNome);
         
         
        return ok;
    }
    
    
    private boolean isCNPJvalid(String CNPJ){
        
        char dig13,dig14;
        int soma,resto,digito,peso;
        
        soma = 0; peso = 2;
        
        for (int i = 15; i >= 0; i--) {
            
            if(CNPJ.charAt(i) != '.' && CNPJ.charAt(i) != '-' && CNPJ.charAt(i) != '/'){
                
                digito = CNPJ.charAt(i) - 48;
                soma += (digito * peso);
                peso++;

                if(peso == 10)
                    peso = 2;
            }
        }
        
        resto = soma % 11;
        if(resto == 0 || resto == 1)
            dig13 = '0';
        else
            dig13 = (char)((11-resto) + 48);
        
        soma = 0; peso = 2;
        
        for (int i = 16; i >= 0; i--) {
            
            if(CNPJ.charAt(i) != '.' && CNPJ.charAt(i) != '-' && CNPJ.charAt(i) != '/'){
                
                digito = CNPJ.charAt(i) - 48;
                soma += (digito * peso);
                peso++;

                if(peso == 10)
                    peso = 2;
            }
           
        }
        
        resto = soma % 11;
        if(resto == 0 || resto == 1)
            dig14 = '0';
        else
            dig14 = (char)((11-resto) + 48);
        
        
        if(CNPJ.charAt(16) == dig13 & CNPJ.charAt(17) == dig14)
            return true;
        return false;
    }
    
    private boolean validaCNPJ(String CNPJ){
    
        boolean ok = true;
        
        Alert a = null;
        
        if(CNPJ.isEmpty()){
                      
            ok = false;
            setTextFieldErro(txCNPJ);
            
            a = new Alert(Alert.AlertType.WARNING, "CNPJ da empresa não pode estar vazio!", ButtonType.CLOSE);
            txCNPJ.requestFocus();
                
        }
        else if(!CNPJ.isEmpty() && CNPJ.length()!= 18){
            
            ok = false;
            setTextFieldErro(txCNPJ);
            
            a = new Alert(Alert.AlertType.WARNING, "CNPJ da empresa incompleto!", ButtonType.CLOSE);
            txCNPJ.requestFocus();
         
        }
        else if(!CNPJ.isEmpty() && !isCNPJvalid(CNPJ)){
            
            ok = false;
            setTextFieldErro(txCNPJ);
            
            a = new Alert(Alert.AlertType.WARNING, "CNPJ não existe!!", ButtonType.CLOSE);
            txCNPJ.requestFocus();
            
        }
        else
            setTextFieldnormal(txCNPJ);
         
        if(a != null)
            a.showAndWait();      
        
        return ok;
    }
    
    private boolean validaTelefone(String telefone){
    
        boolean ok = true;
        
        Alert a = null;
        
        if(telefone.isEmpty()){
                      
            ok = false;
            setTextFieldErro(txTelefone);
            
            a = new Alert(Alert.AlertType.WARNING, "Telefone não pode estar vazio!", ButtonType.CLOSE);
            txTelefone.requestFocus();
                
        }
        else if(!telefone.isEmpty() && telefone.length()!= 14 && telefone.length()!= 13){
            
            ok = false;
            setTextFieldErro(txTelefone);
            
            a = new Alert(Alert.AlertType.WARNING, "Telefone está incompleto!", ButtonType.CLOSE);
            txTelefone.requestFocus();
         
        }
        else
            setTextFieldnormal(txTelefone);
         
        if(a != null)
            a.showAndWait();      
        
        return ok;
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
    
    private boolean validaEmail(String email){
    
        boolean ok = true;
        
        Alert a = null;
        
        if(email.isEmpty()){
                      
            ok = false;
                       
            setTextFieldErro(txEmail);
            a = new Alert(Alert.AlertType.WARNING, "E-mail não pode ser vazio!", ButtonType.CLOSE);
            txEmail.requestFocus();
           
        }
        else if(!email.isEmpty() && !isValidEmailRegex(email)){
            
            ok = false;
                       
            setTextFieldErro(txEmail);
            a = new Alert(Alert.AlertType.WARNING, "E-mail inválido!", ButtonType.CLOSE);
            txEmail.requestFocus();
        }
        else
            setTextFieldnormal(txEmail);
         
         
        if(a != null)
               a.showAndWait();
        return ok;
    }
     
    private boolean validaRazao(String razao){
    
        boolean ok = true;
        
        if(razao.isEmpty()){
                      
            ok = false;
                       
            setTextFieldErro(txRazao);
            Alert a = new Alert(Alert.AlertType.WARNING, "Razão social não pode estar vazio!", ButtonType.CLOSE);
            txRazao.requestFocus();
            a.showAndWait();      
        }
        else
            setTextFieldnormal(txRazao);
         
         
        return ok;
    }
     
    
      private boolean validaCEP(String CEP){
    
        boolean ok = true;
        
        Alert a = null;
        
        if(CEP.isEmpty()){
                      
            ok = false;
            setTextFieldErro(txCEP);
            
            a = new Alert(Alert.AlertType.WARNING, "CEP não pode estar vazio!", ButtonType.CLOSE);
            txCEP.requestFocus();
                
        }
        else if(!CEP.isEmpty() && CEP.length()!= 9){
            
            ok = false;
            setTextFieldErro(txCEP);
            
            a = new Alert(Alert.AlertType.WARNING, "CEP está incompleto!", ButtonType.CLOSE);
            txCEP.requestFocus();
         
        }
        else{
         
            setTextFieldnormal(txCEP);
            insereCampos();
        }
          
         
        if(a != null)
            a.showAndWait();      
        
        return ok;
    }
      
    @FXML
    private void evtConfirmar(ActionEvent event) {
        
        boolean ok = true,ok2 = true;
        int code;
         Alert a  = null;
       
        if (validaNome(txNome.getText())) {

            if (validaCNPJ(txCNPJ.getText())) {

                if (validaRazao(txRazao.getText())) {

                    if (validaTelefone(txTelefone.getText())) {

                        if (validaEmail(txEmail.getText())) {

                            if (validaCEP(txCEP.getText())) {

                                if (!errocep) {

                                    if (img.getImage() != null) {

                                        int cod;

                                        try {

                                            cod = Integer.parseInt(txIDendereco.getText());
                                        } catch (NumberFormatException e) {
                                            cod = 0;
                                        }

                                        DALParametrizacao dalpar = new DALParametrizacao();
                                        Parametrizacao par = new Parametrizacao();
                                        DALEndereco dale = new DALEndereco();

                                        par.setNome(txNome.getText());
                                        par.setCNPJ(txCNPJ.getText());
                                        par.setTelefone(txTelefone.getText());
                                        par.setRazaoSocial(txRazao.getText());
                                        par.setEmail(txEmail.getText());

                                        par.getEndereco().setEnderecoID(cod);
                                        par.getEndereco().setCEP(txCEP.getText());

                                        if (!txRua.getText().isEmpty()) {
                                            par.getEndereco().setRua(txRua.getText());
                                        }
                                        if (!txBairro.getText().isEmpty()) {
                                            par.getEndereco().setBairro(txBairro.getText());
                                        }
                                        if (!txNumero.getText().isEmpty()) {
                                            par.getEndereco().setNumero(Integer.parseInt(txNumero.getText()));
                                        }
                                        if (!txCidade.getText().isEmpty()) {
                                            par.getEndereco().setCidade(txCidade.getText());
                                        }

                                        Alert b = null;

                                        try {

                                            Banco.getCon().getConnect().setAutoCommit(false);

                                            // inserindo
                                            if (cod == 0) {

                                                ok = dale.gravar(par.getEndereco());

                                                if (ok) {

                                                    code = Banco.getCon().getMaxPK("Endereco", "EnderecoID");
                                                    par.getEndereco().setEnderecoID(code);
                                                    ok = dalpar.gravar(par);

                                                    if (ok) {

                                                        try {
                                                            ok = dalpar.gravarFoto(par.getNome(), arq);

                                                            if (ok) {
                                                                b = new Alert(Alert.AlertType.CONFIRMATION, "Parametrizacao,imagem e endereço inseridos!!", ButtonType.OK);
                                                            } else {
                                                                b = new Alert(Alert.AlertType.ERROR, "Problemas ao inserir a imagem!!", ButtonType.OK);
                                                            }

                                                        } catch (IOException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    } else {
                                                        b = new Alert(Alert.AlertType.ERROR, "Problemas ao inserir Parametrizacao!!", ButtonType.OK);
                                                    }
                                                } else {
                                                    b = new Alert(Alert.AlertType.ERROR, "Problemas ao inserir Endereço!!", ButtonType.OK);
                                                }

                                            } else { // alterando

                                                ok = dale.alterar(par.getEndereco());

                                                if (ok) {

                                                    ok = dalpar.alterar(par, nome_antigo);

                                                    if (ok) {

                                                        if (flag) {
                                                            try {
                                                                ok = dalpar.gravarFoto(nome_antigo, arq);

                                                                if (ok) {
                                                                    b = new Alert(Alert.AlertType.CONFIRMATION, "Parametrizacao,imagem e endereço atualizados!!", ButtonType.OK);
                                                                } else {
                                                                    b = new Alert(Alert.AlertType.ERROR, "Problemas ao atualizar a imagem!!", ButtonType.OK);
                                                                }

                                                            } catch (IOException e) {
                                                                System.out.println(e.getMessage());
                                                            }
                                                        } else {
                                                            b = new Alert(Alert.AlertType.CONFIRMATION, "Parametrizacao,imagem e endereço atualizados!!", ButtonType.OK);
                                                        }

                                                    } else {
                                                        b = new Alert(Alert.AlertType.ERROR, "Problemas ao atualizar Parametrizacao!!", ButtonType.OK);
                                                    }
                                                } else {
                                                    b = new Alert(Alert.AlertType.ERROR, "Problemas ao atualizar Endereço!!", ButtonType.OK);
                                                }

                                            }

                                            if (ok) {

                                                Banco.getCon().getConnect().commit();
                                                estadoOriginal();
                                                carregarTabela();
                                            } else {
                                                Banco.getCon().getConnect().rollback();
                                            }

                                            b.showAndWait();
                                            evtLimpar(event);

                                        } catch (SQLException sql) {
                                            System.out.println(sql.getMessage());
                                        }

                                    } else {

                                        a = new Alert(Alert.AlertType.WARNING, "Imagem obrigatória", ButtonType.CLOSE);
                                        btEscolher.requestFocus();
                                        a.showAndWait();

                                    }
                                }
                                else{
                                    
                                    a = new Alert(Alert.AlertType.WARNING, "CEP não existe, insira outro", ButtonType.CLOSE);
                                    txCEP.requestFocus();
                                    a.showAndWait();
                                    
                                }

                            }
                        }
                    }

                }

            }

        }

    }

    @FXML
    private void evtCancelar(ActionEvent event) throws IOException {
        
        if (!pndados.isDisabled())
        {
            img.setImage(null);
            estadoOriginal();
        } 
        else{
            
                       
            if(tabela.getItems().get(0) == null){
                            
                
                Alert a = new Alert(Alert.AlertType.WARNING, "Não pode-se sair da parametrização sem antes inserir os dados!", ButtonType.CLOSE);
                txNome.requestFocus();
                a.showAndWait();
                
                
            }
            else{
                
                FXMLPrincipalController.desabilita_parametri(false);
                FXMLPrincipalController.chamaimagem();
                FXMLPrincipalController.snprincipal.setCenter(null);
                FXMLPrincipalController.nome.setText("");
            }
            
        }
           
    }

    @FXML
    private void evtEscolher(ActionEvent event) {
        
         FileChooser fs = new FileChooser();
        arq = fs.showOpenDialog(null);
        
        if(arq != null){
            
            flag = true;
            aux=new Image(arq.toURI().toString());
            img.setImage(aux);
            img.setFitWidth(aux.getWidth());
            img.setFitHeight(aux.getHeight());
        }
    
    }

    @FXML
    private void evtLimpar(ActionEvent event) {
        
        img.setImage(null);
    }

    @FXML
    private void clickTabela(MouseEvent event) {
        
          if(tabela.getSelectionModel().getSelectedIndex()>=0){
            
            btAlterar.setDisable(false);
            btApagar.setDisable(false);
        }
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
    private void evtValidaCEP(MouseEvent event) 
    {        
       validaCEP(txCEP.getText());              
    }

}
