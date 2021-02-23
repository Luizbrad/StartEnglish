/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
import startenglish.db.DAL.DALEncomendaDeLivro;
import startenglish.db.DAL.DALFuncionario;
import startenglish.db.DAL.DALLivro;
import startenglish.db.Entidades.EncomendaDeLivros;
import startenglish.db.Entidades.Funcionario;
import startenglish.db.Entidades.ItemEncomenda;
import startenglish.db.Entidades.Livro;
import startenglish.db.util.Banco;
import startenglish.util.MaskFieldUtil;

/**
 * FXML Controller class
 *
 * @author azeve
 */
public class FXMLEncomendaLivrosController implements Initializable {

    @FXML
    private JFXButton btNovo;
    private JFXButton btAlterar;
    private JFXButton btExcluir;
    @FXML
    private JFXButton btConfirmar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private AnchorPane pndados;
    @FXML
    private JFXTextField txtID;
    @FXML
    private ComboBox<Funcionario> cbFuncionario;
    @FXML
    private JFXDatePicker dtPEncomenda;
    @FXML
    private JFXDatePicker dtPPrevisao;
    @FXML
    private JFXTextField txtFornecedor;
    @FXML
    private JFXTextField txtValorTotal;
    @FXML
    private TableView<ItemEncomenda> tabelaItens;
    @FXML
    private TableColumn<ItemEncomenda, Livro> tbItensLivro;
    @FXML
    private TableColumn<ItemEncomenda, Integer> tbItensQtd;
    @FXML
    private TableColumn<ItemEncomenda, Double> tbItensValor;
    @FXML
    private ComboBox<Livro> cbLivro;
    @FXML
    private JFXTextField txtValorUni;
    @FXML
    private JFXButton btAdicionar;
    @FXML
    private JFXTextField txtQtd;
    @FXML
    private JFXTextField txPesquisa;
    @FXML
    private JFXDatePicker dtpdataini;
    @FXML
    private JFXDatePicker dtpdatafim;
    @FXML
    private JFXComboBox<String> comboBox;
    @FXML
    private JFXButton btPesquisar;
    @FXML
    private TableView<EncomendaDeLivros> tabela;
    @FXML
    private TableColumn<EncomendaDeLivros, Integer> tabelaID;
    @FXML
    private TableColumn<EncomendaDeLivros, String> tabelaFornecedor;
    @FXML
    private TableColumn<EncomendaDeLivros, Funcionario> tabelaFuncionario;
    @FXML
    private TableColumn<EncomendaDeLivros, LocalDate> tabelaDataEncomenda;
    @FXML
    private TableColumn<EncomendaDeLivros, Double> tabelaValor;
    @FXML
    private JFXButton btRemover;
    @FXML
    private TableColumn<EncomendaDeLivros, LocalDate> tabelaPrevisao;
    
    private List<ItemEncomenda> listaItens;
    @FXML
    private JFXButton btVer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setaComponentes();
//        seta_combobox();
        EstadoOriginal();
    }
    
    private void setaComponentes()
    {
        tbItensLivro.setCellValueFactory(new PropertyValueFactory("livro"));
        tbItensQtd.setCellValueFactory(new PropertyValueFactory("quantidade"));
        tbItensValor.setCellValueFactory(new PropertyValueFactory("valor"));
        
        tabelaID.setCellValueFactory(new PropertyValueFactory("codigoEnc"));
        tabelaFornecedor.setCellValueFactory(new PropertyValueFactory("fornecedor"));
        tabelaFuncionario.setCellValueFactory(new PropertyValueFactory("func"));
        tabelaDataEncomenda.setCellValueFactory(new PropertyValueFactory("dataEncomenda"));
        tabelaPrevisao.setCellValueFactory(new PropertyValueFactory("previsaoEntrega"));
        tabelaValor.setCellValueFactory(new PropertyValueFactory("valor"));
        
        
        listaItens = new ArrayList<ItemEncomenda>();
        MaskFieldUtil.maxField(txtFornecedor, 30);
        MaskFieldUtil.maxField(txtQtd, 4);
        MaskFieldUtil.numericField(txtQtd);
        //MaskFieldUtil.monetaryField(txtValorTotal);
        //MaskFieldUtil.monetaryField(txtValorUni);
        
    }
    
    private void seta_combobox()
    {
        List<String> listaPesquisa = new ArrayList<>();
        listaPesquisa.add("Data Encomenda");
        listaPesquisa.add("Fornecedor");
        listaPesquisa.add("Previsão");
        
        comboBox.setItems(FXCollections.observableArrayList(listaPesquisa));
        
        seta_pesquisa();
        
           
           DALFuncionario dale = new DALFuncionario();
           List<Funcionario> lista = dale.get("");           
        
           ObservableList<Funcionario> modelo = FXCollections.observableArrayList(lista);
           cbFuncionario.setItems(modelo);
           
           DALLivro dall = new DALLivro();
           List<Livro> lista2 = dall.get("");           
        
           ObservableList<Livro> modelo2 = FXCollections.observableArrayList(lista2);
           
           cbLivro.setItems(modelo2);                   
        
    }
    
    private void seta_pesquisa(){
    
        txPesquisa.clear();
        dtpdataini.setDisable(true);
        dtpdatafim.setDisable(true);
        comboBox.setValue("Fornecedor");
        dtpdataini.setValue(LocalDate.now());
        dtpdatafim.setValue(LocalDate.now());
    }
    
    private void EstadoOriginal()
    {       
        habilitatudo();
        seta_combobox();
        btNovo.setDisable(false);
        btCancelar.setDisable(false);
        btConfirmar.setDisable(true);
        btVer.setDisable(false);
        txPesquisa.clear();
        listaItens.clear();
        ObservableList<ItemEncomenda> modelo;
        modelo = FXCollections.observableArrayList(listaItens);
        tabelaItens.setItems(modelo);
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
        habilitatudo();
        comboBox.getItems().clear();
        seta_combobox();
        pndados.setDisable(false);
        btConfirmar.setDisable(false);
        btVer.setDisable(true);
        txPesquisa.clear();
        cbLivro.requestFocus();        
    }
    
    private void Visualizando()
    {
        pndados.setDisable(false);
        btConfirmar.setDisable(true);
        txPesquisa.clear();
        desabilitatudo();
    }
    
    private void desabilitatudo()
    {
        
        txtFornecedor.setEditable(false);
        txtQtd.setEditable(false);
        btAdicionar.setDisable(true);
        btRemover.setDisable(true);
        dtPEncomenda.setDisable(true);
        dtPPrevisao.setDisable(true);
    }
    
    private void habilitatudo()
    {
        txtFornecedor.setEditable(true);
        txtQtd.setEditable(true);
        btAdicionar.setDisable(false);
        btRemover.setDisable(false);
        dtPEncomenda.setDisable(false);
        dtPPrevisao.setDisable(false);
    }

    @FXML
    private void evtNovo(ActionEvent event) {
        EstadoEdicao();
    }

    @FXML
    private void evtConfirmar(ActionEvent event) throws SQLException {
        boolean ok = false;
        Alert a = null;
        
        if(!listaItens.isEmpty())
        {
            EncomendaDeLivros encomenda = new EncomendaDeLivros();
            encomenda.setItens(listaItens);
            double auxiliaar = 0;
            
            try {
               auxiliaar = Double.parseDouble(txtValorTotal.getText());
            } catch (Exception e) {
            }
            
            encomenda.setValor(auxiliaar);
            if(!txtFornecedor.getText().isEmpty())
            {
                encomenda.setFornecedor(txtFornecedor.getText());
                if(cbFuncionario.getSelectionModel().getSelectedIndex() != -1)
                {
                    encomenda.setFunc(cbFuncionario.getValue());
                    if(validaDatas(dtPEncomenda.getValue(),dtPPrevisao.getValue()))
                    {
                        encomenda.setDataEncomenda(dtPEncomenda.getValue());
                        encomenda.setPrevisaoEntrega(dtPPrevisao.getValue());
                        
                        DALEncomendaDeLivro dale = new DALEncomendaDeLivro();
                        
                        try {

                                Banco.getCon().getConnect().setAutoCommit(false);

                                

                                    ok = dale.GravarEncomenda(encomenda);
                                    
                                    if (ok) {
                                        encomenda.setCodigoEnc(dale.getMaxPK());
                                        ok = dale.GravarItens(encomenda);
                                        if (ok) {
                                            a = new Alert(Alert.AlertType.CONFIRMATION, "Encomenda registrada com sucesso!!", ButtonType.OK);
                                            EstadoOriginal();
                                        } else {
                                            a = new Alert(Alert.AlertType.ERROR, "Problemas ao gravar os itens da encomenda!!", ButtonType.OK);
                                        }
                                    } else {
                                       a = new Alert(Alert.AlertType.ERROR, "Problemas ao gravar encomenda!!", ButtonType.OK);
                                    }
                                   
                                a.showAndWait();
                                if (ok) {

                                    Banco.getCon().getConnect().commit();
                                    EstadoOriginal();
                                    CarregaTabela("");
                                } else {
                                    Banco.getCon().getConnect().rollback();
                                }

                            } catch (SQLException ex) {
                                System.out.println(ex.getMessage());
                            }
                    }
                    
                }else{
                    a = new Alert(Alert.AlertType.WARNING, "Selecione o funcionario!!", ButtonType.CLOSE);
                    a.showAndWait();
                    cbFuncionario.requestFocus();
                }
                
            }else{
                a = new Alert(Alert.AlertType.WARNING, "Informe o fornecedor!!", ButtonType.CLOSE);
                a.showAndWait();
                txtFornecedor.requestFocus();
            }
        }else{
            a = new Alert(Alert.AlertType.WARNING, "Sua encomenda nao possui itens!!", ButtonType.CLOSE);
            a.showAndWait();
            cbLivro.requestFocus();
        }
    }
    
    private boolean validaDatas(LocalDate dataEnc, LocalDate dataPrev)
    {
        boolean ok = false;
        Alert a = null;
        Date datahoje = new Date();
        LocalDate hoje = datahoje.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
        
        
        if(dataEnc !=  null && dataPrev != null)
        {
            
            if(dataEnc.isAfter(hoje))
            {
                a= new Alert(Alert.AlertType.WARNING, "A data da Encomenda está em data futura!!", ButtonType.CLOSE);
                a.showAndWait();
                dtPEncomenda.requestFocus();
            }
            else{
                if(dataPrev.isAfter(dataEnc))
                {
                    ok = true;
                }
                else{
                    a= new Alert(Alert.AlertType.WARNING, "A Previsão está antes da data de Encomenda!!", ButtonType.CLOSE);
                    a.showAndWait();
                    dtPPrevisao.requestFocus();
                }
            }
        }else{
            a= new Alert(Alert.AlertType.WARNING, "Datas de encomenda e previsão obrigatórias!!", ButtonType.CLOSE);
            a.showAndWait();
            dtPEncomenda.requestFocus();
        }
        
        return ok;
    }
    
    private boolean validaDatasPesquisa(LocalDate dataEnc, LocalDate dataPrev)
    {
        boolean ok = false;
        Alert a = null;
        Date datahoje = new Date();
        LocalDate hoje = datahoje.toInstant().atZone( ZoneId.systemDefault() ).toLocalDate();
        
        
        if(dataEnc !=  null && dataPrev != null)
        {
            
                if(dataPrev.isAfter(dataEnc))
                {
                    ok = true;
                }
                else{
                    a= new Alert(Alert.AlertType.WARNING, "A data final não pode ser anterior a inicial!!", ButtonType.CLOSE);
                    a.showAndWait();
                    dtpdatafim.requestFocus();
                }
            
        }else{
            a= new Alert(Alert.AlertType.WARNING, "Datas de encomenda e previsão obrigatórias!!", ButtonType.CLOSE);
            a.showAndWait();
            dtpdataini.requestFocus();
        }
        
        return ok;
    }

    @FXML
    private void evtCancelar(ActionEvent event) {
      
        
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
    private void evtClickTabela(MouseEvent event) {
    }

    @FXML
    private void evtPesquisa(ActionEvent event) {
        String filtro = comboBox.getSelectionModel().getSelectedItem(),texto;
        String sql = "";
        LocalDate dataini = null,datafim = null;
        boolean ok = false;
        
                
//            sql = "previsaoentrega is not null AND";
            
            if(filtro.contains("Data")){
                if(validaDatasPesquisa(dtpdataini.getValue(),dtpdatafim.getValue()))
                {
                    dataini = dtpdataini.getValue();
                    datafim = dtpdatafim.getValue();
                    sql +=" dataencomenda BETWEEN '"+dataini+"' AND '"+datafim+"'";
                    ok = true;
                } 
            }
            else
            if(filtro.equals("Fornecedor")){
                ok = true;
                texto = txPesquisa.getText();
                sql +=" upper(fornecedor) like '%"+texto.toUpperCase()+"%'";
            }
            else if(filtro.equals("Previsão"))
            {   
                if(validaDatasPesquisa(dtpdataini.getValue(),dtpdatafim.getValue()))
                {
                    dataini = dtpdataini.getValue();
                    datafim = dtpdatafim.getValue();
                    sql +=" dataencomenda BETWEEN '"+dataini+"' AND '"+datafim+"'";
                    ok= true;
                }
            }
      if(ok)  
        CarregaTabela(sql);
    }

    @FXML
    private void evtComboBox(ActionEvent event) {
        String filtro = comboBox.getSelectionModel().getSelectedItem();

        if (filtro != null) {

            CarregaTabela("");
            if (filtro.contains("Data")) {

                txPesquisa.setDisable(true);
                dtpdataini.setDisable(false);
                dtpdatafim.setDisable(false);
            } else if (filtro.equals("Fornecedor")) {

                txPesquisa.setDisable(false);
                dtpdatafim.setDisable(true);
                dtpdataini.setDisable(true);
            } else if (filtro.equals("Previsão")) {
                txPesquisa.setDisable(true);
                dtpdataini.setDisable(false);
                dtpdatafim.setDisable(false);
            }
        }

    }

    @FXML
    private void evtLimparFiltros(ActionEvent event) {
        seta_pesquisa();
        CarregaTabela("");
    }

    @FXML
    private void evtSelLivro(ActionEvent event) {
        Livro livroaux = cbLivro.getSelectionModel().getSelectedItem();
       
        if(livroaux != null)
            txtValorUni.setText(""+livroaux.getValor());
        
    }

    @FXML
    private void evtAdd(ActionEvent event) {
        ItemEncomenda item = new ItemEncomenda();
        item.setLivro(cbLivro.getValue());
        try {
            item.setQuantidade(Integer.parseInt(txtQtd.getText()));
        } catch (Exception e) {
        }
        
        String auxiliarconta;
        DecimalFormat df = new DecimalFormat("#.##");
        auxiliarconta = df.format(item.getQuantidade()*item.getLivro().getValor());
        auxiliarconta = auxiliarconta.replace(',', '.');
        try {
           item.setValor(Double.parseDouble(auxiliarconta)); 
        } catch (Exception e) {
        }
        
        
        if(listaItens.isEmpty())
        {
           listaItens.add(item);
        }
        else{
            int flag = -1;
            for (int i = 0; i < listaItens.size()&& flag == -1; i++) 
                if(listaItens.get(i).getLivro().equals(item.getLivro()))
                    flag=i;
            if(flag != -1)
            {
                df = new DecimalFormat("#.##");
                auxiliarconta = df.format(listaItens.get(flag).getValor()+item.getValor());
                auxiliarconta = auxiliarconta.replace(',', '.');
                
                item.setQuantidade(listaItens.get(flag).getQuantidade()+item.getQuantidade());
                try {
                        item.setValor(Double.parseDouble(auxiliarconta)); 
                    } catch (Exception e) {
                    }
                
                listaItens.remove(flag);
                listaItens.add(item);
            }                
            else
                listaItens.add(item);
        }
        txtQtd.clear();
        txtValorUni.clear();
        cbLivro.getSelectionModel().select(-1);
        tabelaItens.setItems(FXCollections.observableArrayList(listaItens));
        double valor = calculaTot();
        String auxiliarcontaa;
        DecimalFormat decimal = new DecimalFormat("#.##");
        auxiliarcontaa = decimal.format(valor);
        auxiliarcontaa = auxiliarcontaa.replace(',', '.');
        
        txtValorTotal.setText(auxiliarcontaa);
        
    }
    
    private double calculaTot()
    {
        double valor = 0;
        for (int i = 0; i < listaItens.size() ; i++) {
            
                   valor+=listaItens.get(i).getValor();
        }
        
        return valor;
    }

    @FXML
    private void evtRemove(ActionEvent event) {
        int auxiliar = tabelaItens.getSelectionModel().getSelectedIndex();
        listaItens.remove(auxiliar);
        tabelaItens.setItems(FXCollections.observableArrayList(listaItens));
        double valor = calculaTot();String auxiliarcontaa;
        DecimalFormat decimal = new DecimalFormat("#.##");
        auxiliarcontaa = decimal.format(valor);
        auxiliarcontaa = auxiliarcontaa.replace(',', '.');
        
        txtValorTotal.setText(auxiliarcontaa);
    }

    
    private void CarregaTabela(String filtro)
    {
        DALEncomendaDeLivro dale = new DALEncomendaDeLivro();
        List<EncomendaDeLivros> listaTabela = dale.BuscaEncomendas(filtro);
        
        ObservableList<EncomendaDeLivros> modelo;
        modelo = FXCollections.observableArrayList(listaTabela);
        tabela.setItems(modelo);
    }
    
    private void CarregaTabItens(int id)
    {
        DALEncomendaDeLivro dale = new DALEncomendaDeLivro();
        listaItens = dale.BuscaItens(id);
        
        ObservableList<ItemEncomenda> modelo;
        modelo = FXCollections.observableArrayList(listaItens);
        tabelaItens.setItems(modelo);
    }
    

    @FXML
    private void evtVer(ActionEvent event) {
        if(tabela.getSelectionModel().getSelectedItem() != null){
        
            EncomendaDeLivros e = tabela.getSelectionModel().getSelectedItem();
            txtID.setText(""+e.getCodigoEnc());
            txtFornecedor.setText(e.getFornecedor());
            dtPEncomenda.setValue(e.getDataEncomenda());
            dtPPrevisao.setValue(e.getPrevisaoEntrega());
            txtValorTotal.setText(""+e.getValor());
            cbFuncionario.getSelectionModel().select(e.getFunc());
            
            CarregaTabItens(e.getCodigoEnc());
            
            Visualizando();
            
        }
        
    }
    
}
