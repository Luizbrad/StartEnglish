
package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import startenglish.db.DAL.DALCurso;
import startenglish.db.DAL.DALLivro;
import startenglish.db.Entidades.Livro;
import startenglish.db.util.Banco;
import startenglish.util.MaskFieldUtil;

public class FXMLLivroController implements Initializable {

    @FXML
    private JFXButton btNovo;
    @FXML
    private JFXTextField txNome;
    @FXML
    private JFXButton btPesquisar;
    @FXML
    private JFXTextField txPesquisa;
    @FXML
    private TableView<Livro> tabela;
    @FXML
    private TableColumn<Livro,String> tabelaNome;
    @FXML
    private JFXTextField txValor;
    @FXML
    private JFXTextField txID;
    @FXML
    private TableColumn<Livro, String> tabelaValor;
    @FXML
    private JFXTextField txEditora;
    @FXML
    private JFXTextField txVolume;
    @FXML
    private JFXComboBox<String> cbFiltro;
    @FXML
    private TableColumn<Livro,String> tabelaEditora;
    @FXML
    private TableColumn<Livro,String> tabelaVolume;
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
    private AnchorPane pnbusca;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        tabelaNome.setCellValueFactory(new PropertyValueFactory("Nome"));
        tabelaValor.setCellValueFactory(new PropertyValueFactory("valor"));
        tabelaEditora.setCellValueFactory(new PropertyValueFactory("editora"));
        tabelaVolume.setCellValueFactory(new PropertyValueFactory("volume"));
        
        MaskFieldUtil.maxField(txEditora, 25);
        MaskFieldUtil.maxField(txVolume, 5);
        MaskFieldUtil.maxField(txNome, 30);
        MaskFieldUtil.maxField(txValor,8);    
        MaskFieldUtil.monetaryField(txValor);
        
        List<String> combo = new ArrayList();
        combo.add("Nome");
        combo.add("Editora");
        
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
        DALLivro dalL = new DALLivro();       
        List <Livro> listaLivro = dalL.get(filtro);
        listaLivro = dalL.get(filtro);           
        ObservableList <Livro> modelo;
        modelo = FXCollections.observableArrayList(listaLivro);
        for (int i = 0; i < modelo.size(); i++)
        {
           modelo.get(i).setValor(modelo.get(i).getValor());
        }
        tabela.setItems(modelo);
    }

    private boolean validaPrecoTam(String preco)
    {      
        boolean ok = false;
        int i;
        
        for (i = 0;  i<preco.length() && preco.charAt(i) != '.' ; i++){}
            
        
        if(i <=4 )
            ok = true;
        
        return ok;
    }
    
    private boolean validaPreco(String preco)
    {       
        String auxiliar="";
        for (int i = 0; i < preco.length(); i++)
        {
            if(preco.charAt(i)!='.')
            {
                if(preco.charAt(i)==',')
                    auxiliar+='.';
                else
                    auxiliar+=preco.charAt(i);
            }
        }
        
        double preco_inserido = 0;
        Alert a = null;
        boolean ok = true,problema = false,tamanho = true;
        
        try{
            
            preco_inserido = Double.parseDouble(auxiliar);
            tamanho = validaPrecoTam(auxiliar);
            
        }catch(NumberFormatException ex){problema = true;}
        
        if(preco.isEmpty())
        {           
            ok = false;
            txValor.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            a = new Alert(Alert.AlertType.WARNING, "Valor obrigatório!!", ButtonType.CLOSE);
            txValor.requestFocus();           
        }
        else if(!preco.isEmpty() && problema)
        {           
            ok = false;
            txValor.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            a = new Alert(Alert.AlertType.WARNING, "Valor inválido!", ButtonType.CLOSE);
            txValor.requestFocus();
        }
        else if(!preco.isEmpty() && preco_inserido <=0)
        {            
            ok = false;
            txValor.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            a = new Alert(Alert.AlertType.WARNING, "Valor igual ou menor que 0!", ButtonType.CLOSE);
            txValor.requestFocus();
        }
        else if(!tamanho)
        {           
            ok = false;
            txValor.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            a = new Alert(Alert.AlertType.WARNING, "É permitido apenas 4 digitos antes da casa decimal!", ButtonType.CLOSE);
            txValor.requestFocus();           
        }
        else
        {
            txValor.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
        }
            
        
        if(a != null)
            a.showAndWait();
        return ok;
    }
    
    private boolean validaChars(String nome,String volume,String editora)
    {        
        boolean ok = true;
        Alert a=null;
        if(nome.isEmpty())
        {        
            ok = false;
            txNome.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txNome.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Nome do livro obrigatório!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
            
        }
        else
        {
            txNome.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
        }
        if(volume.isEmpty())
        {
           ok = false;
            txVolume.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txVolume.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Volume obrigatório!!", ButtonType.CLOSE);           
                a.showAndWait(); 
            }
        }
        else
        {
          txVolume.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");  
        }
        if(editora.isEmpty())
        {
          ok = false;
            txEditora.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txEditora.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Nome da editora obrigatório!!", ButtonType.CLOSE);
                a.showAndWait(); 
            }
        }
        else
        {
           txEditora.setStyle("-fx-border-width: 0;"
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
            Livro l = tabela.getSelectionModel().getSelectedItem();
            Double auxiliar;
            String auxiliardec;
            txID.setText(""+l.getLivroID());
            txNome.setText(l.getNome());
            txEditora.setText(l.getEditora());
            auxiliar=l.getValor();
            
            int conversor= auxiliar.intValue();
            
            auxiliar=auxiliar-conversor;
            auxiliardec=auxiliar.toString();
            txValor.setText(""+l.getValor());
            if(auxiliardec.length()==3 && auxiliardec.charAt(2)!='0')
                txValor.setText(txValor.getText()+'0');
            if(auxiliar==0)
                txValor.setText(txValor.getText()+'0');
                
            txVolume.setText(l.getVolume());
            
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
            Livro l = new Livro();
            DALLivro dal = new DALLivro();
            
            l = tabela.getSelectionModel().getSelectedItem();
            
            try{
                
                Banco.getCon().getConnect().setAutoCommit(false);
                
                ok = dal.apagar(l.getLivroID());
                
                if(ok)
                {                   
                     Banco.getCon().getConnect().commit();
                    a =  new Alert(Alert.AlertType.CONFIRMATION," Exclusão ocorrida com sucesso!!", ButtonType.OK);
                }
                else
                {                  
                    Banco.getCon().getConnect().rollback();
                    a =  new Alert(Alert.AlertType.ERROR, "Erro ao deletar livro!", ButtonType.OK);
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
        if(validaChars(txNome.getText(),txVolume.getText(),txEditora.getText()))
        {
            if(validaPreco(txValor.getText()))
            {
                double valor = 0;
                int cod;
                String auxiliar="";
                    String preco=txValor.getText();
                    for (int i = 0; i < preco.length(); i++)
                    {
                        if(preco.charAt(i)!='.')
                        {
                            if(preco.charAt(i)==',')
                                auxiliar+='.';
                            else
                                auxiliar+=preco.charAt(i);
                        }
                    }
                try 
                {
                    valor = Double.parseDouble(auxiliar);
                    cod = Integer.parseInt(txID.getText());
                                                           
                }
                catch (NumberFormatException ex) 
                {
                    cod = 0;
                    
                }
                DALLivro dal = new DALLivro();
                Livro l = new Livro();
                l.setEditora(txEditora.getText());
                l.setNome(txNome.getText());
                l.setVolume(txVolume.getText());
                l.setValor(valor);
                l.setLivroID(cod);
                Alert a = null;
                boolean ok;
                try
                {
                    Banco.getCon().getConnect().setAutoCommit(false);

                    if (cod == 0)
                    {
                        ok = dal.gravar(l);

                        if (ok)
                        {
                            a = new Alert(Alert.AlertType.CONFIRMATION, "Livro inserido!!", ButtonType.OK);
                        } 
                        else 
                        {
                            a = new Alert(Alert.AlertType.ERROR, "Problemas ao inserir o livro!!", ButtonType.OK);
                        }

                    } 
                    else
                    { 
                        ok = dal.alterar(l);

                        if(ok)
                        {
                            a = new Alert(Alert.AlertType.CONFIRMATION, "Livro atualizado!!", ButtonType.OK);
                        } 
                        else
                        {
                            a = new Alert(Alert.AlertType.ERROR, "Problemas ao atualizar o livro!!", ButtonType.OK);
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

    @FXML
    private void evtPesquisar(ActionEvent event) 
    {
        if(cbFiltro.getSelectionModel().getSelectedItem().equals("Nome"))
            CarregaTabela("livro.nome like '%"+txPesquisa.getText()+"%'");
        else
            CarregaTabela("livro.editora like '%"+txPesquisa.getText()+"%'");

    }

    @FXML
    private void clickTabela(MouseEvent event) 
    {
        if(tabela.getSelectionModel().getSelectedIndex()>=0)
        {
           btAlterar.setDisable(false);
           btExcluir.setDisable(false);
        }
    }
}
