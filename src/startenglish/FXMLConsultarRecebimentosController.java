package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.layout.AnchorPane;
import startenglish.db.DAL.DALRecebimento;
import startenglish.db.Entidades.Recebimentos;
import startenglish.util.MaskFieldUtil;


public class FXMLConsultarRecebimentosController implements Initializable {

    @FXML
    private AnchorPane pnFiltros;
    @FXML
    private JFXComboBox<String> cbSelecionarFiltro;
    @FXML
    private JFXDatePicker dtEmissIni;
    @FXML
    private JFXDatePicker dtEmissFim;
    @FXML
    private JFXTextField txNomeAluno;
    @FXML
    private JFXButton btPesquisar;
    @FXML
    private JFXButton btLimparFiltros;
    @FXML
    private JFXDatePicker dtVencIni;
    @FXML
    private JFXDatePicker dtVencFim;
    @FXML
    private JFXDatePicker dtPagaIni;
    @FXML
    private JFXCheckBox cbStatus;
    @FXML
    private TableView<Recebimentos> tabelaRecibmentos;
    @FXML
    private JFXButton btVoltar;
    @FXML
    private JFXButton btSelecionarRecebimentos;
    @FXML
    private TableColumn<Recebimentos, Double> tcValor;
    @FXML
    private TableColumn<Recebimentos, Double> tcValorPago;
    @FXML
    private TableColumn<Recebimentos, String> tcDataEmissao;
    @FXML
    private TableColumn<Recebimentos, String> tcDataVencimento;
    @FXML
    private TableColumn<Recebimentos, String> tcDataPagamentoo;
    @FXML
    private TableColumn<Recebimentos, Integer> tcNumMat;
    @FXML
    private TableColumn<Recebimentos, String> tcNomeAluno;
    @FXML
    private TableColumn<Recebimentos, String> tcStatus;
    @FXML
    private AnchorPane pnRegistro;
    @FXML
    private JFXTextField txValor;
    @FXML
    private JFXDatePicker dtDataReceb;
    @FXML
    private JFXTextField txValorPago;
    @FXML
    private JFXButton btConfirmar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private JFXButton btEstorno;
    @FXML
    private JFXButton btCancelarOperacoes;
    @FXML
    private JFXButton btFinalizar;

    private int recebimentoIDmem;
    private boolean alterou;
    private Recebimentos recebAtual;
    private int indexAtual;
    private List<Recebimentos>recebs;
    @FXML
    private JFXDatePicker dtPagFim;
    @FXML
    private JFXTextField txAlunoFiltro;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
        setaTabela();
        estadoOriginal();
        setaCombobox();
        carregaTabela('I');
        MaskFieldUtil.maxField(txNomeAluno, 30);
        MaskFieldUtil.maxField(txValorPago, 10);
        MaskFieldUtil.monetaryField(txValorPago);
        MaskFieldUtil.monetaryField(txValor);
        setaIDrecebAtual();
        
      
    }    
       
    private void estadoOriginal(){
        
        alterou = false;
        
        btSelecionarRecebimentos.setDisable(true);
        estadoEdicao();
        pnRegistro.setDisable(true);
        btFinalizar.setDisable(true);
      
        estadoOriginalPesquisa();
    }
    
    private void estadoEdicao(){
        
        txNomeAluno.clear();
        txValor.clear();
        dtDataReceb.setValue(null);
        txValorPago.clear();
        setTextFieldnormal(txValorPago);
        setDataNormal(dtDataReceb);
        pnRegistro.setDisable(false);
    }
    
    private void setaIDrecebAtual(){
        
        int idAux = recebs.get(0).getRecebimentoid();
        
        for (int i = 0; i < recebs.size(); i++) {
            
            if(recebs.get(i).getRecebimentoid() > idAux)
                idAux = recebs.get(i).getRecebimentoid();
        }
        
       recebimentoIDmem = idAux;
    }
    
    private void carregaTabela(char chamada) {

        DALRecebimento dalRECEB = new DALRecebimento();
        List<Recebimentos> recebimentos = new ArrayList();
      
        if (chamada == 'I') {
            recebimentos = dalRECEB.get("");
            recebs = recebimentos;
            tabelaRecibmentos.setItems(FXCollections.observableArrayList(recebs));
        } else if (chamada == 'L') {            
                      
            Collections.sort(recebs, Comparator.comparing(Recebimentos::getDtvencimento));
            tabelaRecibmentos.setItems(FXCollections.observableArrayList(recebs));
        }

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
    
    private void setaCombobox(){
        
        List<String> Filtros = new ArrayList();
        
        Filtros.add("Data de Emissão");
        Filtros.add("Data de Vencimento");
        Filtros.add("Data de Pagamento");
        Filtros.add("Nome do Aluno");
        
        ObservableList<String> modeloFiltros = FXCollections.observableArrayList(Filtros);
        cbSelecionarFiltro.setItems(modeloFiltros);      
        
    }
    
    private void setaTabela(){
        
        tcValor.setCellValueFactory(new PropertyValueFactory("valor"));
        tcValorPago.setCellValueFactory(new PropertyValueFactory("valorpago"));
        tcDataEmissao.setCellValueFactory(new PropertyValueFactory("dtEmissaoFORM"));
        tcDataVencimento.setCellValueFactory(new PropertyValueFactory("dtVencimentoFORM"));
        tcDataPagamentoo.setCellValueFactory(new PropertyValueFactory("dtRecebFORM"));
        tcNumMat.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getMat().getNummat()));
        tcNomeAluno.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMat().getAluno().getNome()));
        tcStatus.setCellValueFactory(new PropertyValueFactory("pago"));
    }
    
    private void estadoOriginalPesquisa(){
        
        cbSelecionarFiltro.getSelectionModel().select("Data de Emissão");
        dtEmissFim.setDisable(false);
        dtEmissIni.setDisable(false);
        dtPagaIni.setDisable(true);
        dtPagFim.setDisable(true);
        dtVencIni.setDisable(true);
        dtVencFim.setDisable(true);
        txAlunoFiltro.clear();
        txAlunoFiltro.setDisable(true);
        
        dtEmissIni.setValue(LocalDate.now());
        dtEmissFim.setValue(LocalDate.now().plusDays(30));
        
        cbStatus.disarm();
    }
 
    @FXML
    private void evtPesquisar(ActionEvent event) {

        String filtro = cbSelecionarFiltro.getSelectionModel().getSelectedItem();
        String nomeAluno;
        LocalDate ini, fim;
        List<Recebimentos> novaLista = new ArrayList();
        Alert a = null;
        boolean ok = true;
      
        if (filtro.contains("Emissão")) {

            if (dtEmissFim.getValue().isBefore(dtEmissIni.getValue())) {
                a = new Alert(Alert.AlertType.WARNING, "Data de emissão final esta antes que a data de emissão inicial", ButtonType.CLOSE);
                ok = false;
            }

        } else if (filtro.contains("Vencimento")) {

            if (dtVencFim.getValue().isBefore(dtVencIni.getValue())) {
                a = new Alert(Alert.AlertType.WARNING, "Data de vencimento final esta antes que a data de vencimento inicial", ButtonType.CLOSE);
                ok = false;
            }
        } else if (filtro.contains("Pagamento")) {

            if (dtPagFim.getValue().isBefore(dtPagaIni.getValue())) {
                a = new Alert(Alert.AlertType.WARNING, "Data de pagamento final esta antes que a data de pagamento inicial", ButtonType.CLOSE);
                ok = false;
            }
        }

        
        if(ok){
            
            if (cbStatus.isSelected()) { //PAGOS

                if (filtro.contains("Nome do Aluno")) {

                    nomeAluno = txAlunoFiltro.getText();

                    for (int i = 0; i < recebs.size(); i++) {

                        if (recebs.get(i).getMat().getAluno().getNome().toLowerCase().contains(nomeAluno.toLowerCase()) && recebs.get(i).getPago().equals("Pago")) {
                            novaLista.add(recebs.get(i));
                        }
                    }

                } else {

                    if (filtro.contains("Emissão")) {

                        ini = dtEmissIni.getValue();
                        fim = dtEmissFim.getValue();

                        for (int i = 0; i < recebs.size(); i++) {

                            if ((recebs.get(i).getDtemissoa().isAfter(ini) || recebs.get(i).getDtemissoa().isEqual(ini))
                                    && (recebs.get(i).getDtemissoa().isBefore(fim) || recebs.get(i).getDtemissoa().isEqual(fim))
                                    && recebs.get(i).getPago().equals("Pago")) {

                                novaLista.add(recebs.get(i));
                            }
                        }

                    } else if (filtro.contains("Vencimento")) {

                        ini = dtVencIni.getValue();
                        fim = dtVencFim.getValue();

                        for (int i = 0; i < recebs.size(); i++) {

                            if ((recebs.get(i).getDtvencimento().isAfter(ini) || recebs.get(i).getDtvencimento().isEqual(ini))
                                    && (recebs.get(i).getDtvencimento().isBefore(fim) || recebs.get(i).getDtvencimento().isEqual(fim))
                                    && recebs.get(i).getPago().equals("Pago")) {

                                novaLista.add(recebs.get(i));
                            }
                        }

                    } else {

                        ini = dtPagaIni.getValue();
                        fim = dtPagFim.getValue();

                        for (int i = 0; i < recebs.size(); i++) {

                            if (recebs.get(i).getDtreceb() != null) {

                                if ((recebs.get(i).getDtreceb().isAfter(ini) || recebs.get(i).getDtreceb().isEqual(ini))
                                        && (recebs.get(i).getDtreceb().isBefore(fim) || recebs.get(i).getDtreceb().isEqual(fim))
                                        && recebs.get(i).getPago().equals("Pago")) {

                                    novaLista.add(recebs.get(i));
                                }
                            }

                        }

                    }
                }
            } else { // NAO PAGOS

                if (filtro.contains("Nome do Aluno")) {

                    nomeAluno = txAlunoFiltro.getText();

                    for (int i = 0; i < recebs.size(); i++) {

                        if (recebs.get(i).getMat().getAluno().getNome().toLowerCase().contains(nomeAluno.toLowerCase()) && recebs.get(i).getPago().equals("Não Pago")) {
                            novaLista.add(recebs.get(i));
                        }
                    }
                } else {

                    if (filtro.contains("Emissão")) {

                        ini = dtEmissIni.getValue();
                        fim = dtEmissFim.getValue();

                        for (int i = 0; i < recebs.size(); i++) {

                            if ((recebs.get(i).getDtemissoa().isAfter(ini) || recebs.get(i).getDtemissoa().isEqual(ini))
                                    && (recebs.get(i).getDtemissoa().isBefore(fim) || recebs.get(i).getDtemissoa().isEqual(fim))
                                    && recebs.get(i).getPago().equals("Não Pago")) {

                                novaLista.add(recebs.get(i));
                            }
                        }

                    } else if (filtro.contains("Vencimento")) {

                        ini = dtVencIni.getValue();
                        fim = dtVencFim.getValue();

                        for (int i = 0; i < recebs.size(); i++) {

                            if ((recebs.get(i).getDtvencimento().isAfter(ini) || recebs.get(i).getDtvencimento().isEqual(ini))
                                    && (recebs.get(i).getDtvencimento().isBefore(fim) || recebs.get(i).getDtvencimento().isEqual(fim))
                                    && recebs.get(i).getPago().equals("Não Pago")) {

                                novaLista.add(recebs.get(i));
                            }
                        }

                    } else {

                        ini = dtPagaIni.getValue();
                        fim = dtPagFim.getValue();

                        for (int i = 0; i < recebs.size(); i++) {

                            if (recebs.get(i).getDtreceb() != null) {

                                if ((recebs.get(i).getDtreceb().isAfter(ini) || recebs.get(i).getDtreceb().isEqual(ini))
                                        && (recebs.get(i).getDtreceb().isBefore(fim) || recebs.get(i).getDtreceb().isEqual(fim))
                                        && recebs.get(i).getPago().equals("Não Pago")) {

                                    novaLista.add(recebs.get(i));
                                }
                            }

                        }
                    }
                }
            }
            
            if (filtro.contains("Emissão")) {
                Collections.sort(novaLista, Comparator.comparing(Recebimentos::getDtemissoa));
            } else if (filtro.contains("Vencimento")) {
                Collections.sort(novaLista, Comparator.comparing(Recebimentos::getDtvencimento));
            } else if (filtro.contains("Pagamento")) {
                Collections.sort(novaLista, Comparator.comparing(Recebimentos::getDtreceb));
            }

            tabelaRecibmentos.setItems(FXCollections.observableArrayList(novaLista));
        } else
            a.showAndWait();

    }

    @FXML
    private void evtLimpar(ActionEvent event) {
        
        carregaTabela('L');
        cbStatus.setSelected(false);
        estadoOriginalPesquisa();
    }

    @FXML
    private void evtTabela(MouseEvent event) {
        
        if(tabelaRecibmentos.getSelectionModel().getSelectedIndex() >=0){
            btSelecionarRecebimentos.setDisable(false);
        }
        else{
            btSelecionarRecebimentos.setDisable(true);
        }
    }

    @FXML
    private void evtVoltar(ActionEvent event) {
        
        if (alterou) {
            evtFinalizar(event);
        }

        FXMLPrincipalController.snprincipal.setCenter(null);
        FXMLPrincipalController.nome.setText("");
    }

    @FXML
    private void evtSelecionar(ActionEvent event) {
        
        Double valor,valorAux;
        int conversor;
        String auxiliardec;
        DecimalFormat df;
                
        if(tabelaRecibmentos.getSelectionModel().getSelectedIndex()>=0){
        
            estadoEdicao();
            Recebimentos receb = tabelaRecibmentos.getSelectionModel().getSelectedItem();

            txNomeAluno.setText(receb.getMat().getAluno().getNome());
            valor = valorAux = receb.getValor();
            conversor = valor.intValue();
            valor = valor - conversor;
                       
            txValor.setText(""+valorAux);
            df = new DecimalFormat("#.##");
            auxiliardec = df.format(valor);
            auxiliardec = auxiliardec.replace(',', '.');
            
              
             if(auxiliardec.length() == 3 && auxiliardec.charAt(2) != '0')
                txValor.setText(txValor.getText()+'0');
             if(valor == 0)
                    txValor.setText(txValor  .getText()+'0');   
             
            dtDataReceb.setValue(LocalDate.now());

            if (receb.getValorpago() != 0) {
               valorAux =  valor = receb.getValorpago();
            } else {
                valorAux = valor = receb.getValor();
            }

            conversor = valor.intValue();
            valor = valor - conversor;
                        
            txValorPago.setText(""+valorAux);
            df = new DecimalFormat("#.##");
            auxiliardec = df.format(valor);
            auxiliardec = auxiliardec.replace(',', '.');
            
              
             if(auxiliardec.length() == 3 && auxiliardec.charAt(2) != '0')
                txValorPago.setText(txValorPago.getText()+'0');
             if(valor == 0)
                    txValorPago.setText(txValorPago  .getText()+'0');    
             
            recebAtual = receb;
            indexAtual = recebs.indexOf(recebAtual); // alterei
            
            if(recebAtual.getDtreceb() != null){
                btEstorno.setDisable(false);
                btConfirmar.setDisable(true);
                
                dtDataReceb.setEditable(false);
                txValorPago.setEditable(false);
            }
            else{
                btEstorno.setDisable(true);
                btConfirmar.setDisable(false);
                
                dtDataReceb.setEditable(true);
                txValorPago.setEditable(true);
            }
          
        }
    }
    
    private Recebimentos retornaNaoPago(List<Recebimentos> recebsMat){
     
        for (int i = 0; i < recebsMat.size(); i++) {
                     
            if(recebsMat.get(i).getPago().equals("Não Pago") && recebsMat.get(i).getAmarracao() == recebAtual.getAmarracao())
                return recebsMat.get(i);

        }
       
        return null;
    }
    
    private boolean validaRecebatualaux(List<Recebimentos> recebsMat){
        
        boolean flag = true;
        Recebimentos recebAux;
        
        for (int i = 0; i < recebsMat.size() && flag; i++) {
                    
            recebAux = recebsMat.get(i);
           if(recebAux.getPago().equals("Pago")){
               
               if(recebAux.getDtreceb().equals(recebAtual.getDtreceb())){
                   
                   if(recebAtual.getRecebimentoid() < recebAux.getRecebimentoid())
                       flag = !flag;
               }
               else if(recebAtual.getDtreceb().isBefore(recebAux.getDtreceb()))
                   flag = !flag;
           }

        }
       
        return flag;
    }
    
    private List<Recebimentos> recebMat(){
        
        List<Recebimentos> recebsMat = new ArrayList();
        
        for (int i = 0; i < recebs.size(); i++) {
            
            if(recebs.get(i).getMat().getNummat() == recebAtual.getMat().getNummat())
                recebsMat.add(recebs.get(i));
        }
        
        Collections.sort(recebsMat, Comparator.comparing(Recebimentos::getRecebimentoid));
        return recebsMat;
    }
    
    private boolean validaRecebimentoAtual(List<Recebimentos> recebsMat){
        
        boolean ok;
        Alert a = null;
        
        ok = validaRecebatualaux(recebsMat);
        
        if (!ok) {

            a = new Alert(Alert.AlertType.WARNING, "Este recebimento não é o mais atual, logo não pode ser estornado!!", ButtonType.CLOSE);
        }
        
         if(a != null)
             a.showAndWait();
        return ok;
    }
     
  
    private boolean validaValor(String valor){
        
        String auxiliar="";
        for (int i = 0; i < valor.length(); i++)
        {
            if(valor.charAt(i)!='.')
            {
                if(valor.charAt(i)==',')
                    auxiliar+='.';
                else
                    auxiliar+=valor.charAt(i);
            }
        }
        
        Alert a = null;
        boolean ok = true;
        double valorTrans = 0;
        
        try{
            valorTrans = Double.parseDouble(auxiliar);
                       
        }catch(NumberFormatException e){ ok = false;}
        
        if(valor.isEmpty()){
            
            a = new Alert(Alert.AlertType.WARNING, "Valor pago não pode estar vazio!!", ButtonType.CLOSE);
            setTextFieldErro(txValorPago);
            txValorPago.requestFocus();
        }       
        else if(ok && valorTrans == 0) {
            
            ok = false;
            a = new Alert(Alert.AlertType.WARNING, "Valor pago negativo ou igual a zero!", ButtonType.CLOSE);
            setTextFieldErro(txValorPago);
            txValorPago.requestFocus();
        }        
        else 
            setTextFieldnormal(txValorPago);
        
        if(a != null)
            a.showAndWait();
        
        return ok;
    }
          
    private Double converteValor(String valor) {

        String novoValor = "";
       
        for (int i = 0; i < valor.length(); i++) {
            if (valor.charAt(i) != '.') {
                if (valor.charAt(i) == ',') {
                    novoValor += '.';
                } else {
                    novoValor += valor.charAt(i);
                }
            }
        }        
        return Double.parseDouble(novoValor);
     
    }
    
    private boolean validaDataAtual(){
        
        Alert a = null;
        List<Recebimentos> recebsMat = recebMat();
        boolean ok = true;
        
        boolean flag = true;
        Recebimentos recebAux;
        
        for (int i = 0; i < recebsMat.size() && flag; i++) {
                    
            recebAux = recebsMat.get(i);
           if(recebAux.getPago().equals("Não Pago")){
               
               if(recebAtual.getDtvencimento().isAfter(recebAux.getDtvencimento()))
                   flag = !flag;
           }

        }
  
        if(!flag)
        {
            ok = false;
             a = new Alert(Alert.AlertType.WARNING, "Este recebimento não é o mais atual desta matrícula", ButtonType.CLOSE);
        }
        else
            setDataNormal(dtDataReceb);
        
        if(a != null)
            a.showAndWait();
        
        return ok;
    }
    
    @FXML
    private void evtConfirmar(ActionEvent event) {

        double diferenca;
        String diferenca2;
        double valorPago;
        Alert alert = null;
        Recebimentos rec, recebAux;
        boolean ok = true;
        
        if (validaDataAtual()) {

            if (validaValor(txValorPago.getText())) {

                try {
                    valorPago = converteValor(txValorPago.getText());

                } catch (NumberFormatException e) {
                    valorPago = 0;
                }

                if (valorPago > recebAtual.getValor()) {
                    alert = new Alert(Alert.AlertType.CONFIRMATION, "Valor pago maior que o do recebimento, Deseja continuar?", ButtonType.YES, ButtonType.NO);
                } else {
                    ok = false;
                }

                if (!ok || (alert != null && alert.showAndWait().get() == ButtonType.YES)) {

                    diferenca = recebAtual.getValor() - valorPago;

                    recebAux = new Recebimentos();
                    recebAux.setRecebimentoid(recebAtual.getRecebimentoid());
                    recebAux.setCaixa(recebAtual.getCaixa());
                    recebAux.setMat(recebAtual.getMat());
                    recebAux.setDtemissoa(recebAtual.getDtemissoa());
                    recebAux.setDtvencimento(recebAtual.getDtvencimento());
                    recebAux.setValor(recebAtual.getValor());
                    recebAux.setValorpago(valorPago);
                    recebAux.setDtreceb(dtDataReceb.getValue());
                    recebAux.setPago("Pago");
                    recebAux.setaDatas();
                    recebAux.setAmarracao(recebAtual.getAmarracao());
                    recebs.add(recebAux);

                    if (diferenca > 0) {

                        DecimalFormat df = new DecimalFormat("#.##");
                        diferenca2 = df.format(diferenca);
                        diferenca2 = diferenca2.replace(',', '.');
                        recebimentoIDmem++;
                        rec = new Recebimentos();

                        rec.setRecebimentoid(recebimentoIDmem);
                        rec.setCaixa(recebAtual.getCaixa());
                        rec.setMat(recebAtual.getMat());
                        rec.setDtemissoa(LocalDate.now());
                        rec.setDtvencimento(recebAtual.getDtvencimento()); //alterei
                        rec.setValor(Double.parseDouble(diferenca2));
                        rec.setaDatas();
                        rec.setAmarracao(recebAtual.getAmarracao());
                        recebs.add(rec);
                    }

                    recebs.remove(indexAtual);
                    alterou = true;
                    btFinalizar.setDisable(false);
                    alert = new Alert(Alert.AlertType.INFORMATION, "Recebimento pago com sucesso!", ButtonType.CLOSE);
                    alert.showAndWait();

                    carregaTabela('L');
                    estadoEdicao();
                    pnRegistro.setDisable(true);
                }

            }
        }

    }

    @FXML
    private void evtCancelar(ActionEvent event) {

       estadoEdicao();
       pnRegistro.setDisable(true);
    }

    @FXML
    private void evtEstorno(ActionEvent event) {

        Recebimentos rec,aux;
        Alert alert;
        List<Recebimentos> recebsMat = recebMat();

        if (validaRecebimentoAtual(recebsMat)) {

            alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realizar estorno?", ButtonType.YES, ButtonType.NO);

            if (alert.showAndWait().get() == ButtonType.YES) {

                rec = new Recebimentos();
                rec.setRecebimentoid(recebAtual.getRecebimentoid());
                rec.setCaixa(recebAtual.getCaixa());
                rec.setMat(recebAtual.getMat());
                rec.setDtemissoa(recebAtual.getDtemissoa());
                rec.setDtvencimento(recebAtual.getDtvencimento());
                rec.setValor(recebAtual.getValor());
                rec.setaDatas();
                rec.setAmarracao(recebAtual.getAmarracao());
                
                aux = retornaNaoPago(recebsMat);
                if(aux != null){
                         recebs.remove(recebs.indexOf(aux));
                }
                
                recebs.remove(recebs.indexOf(recebAtual));
                recebs.add(rec);
                             
                alterou = true;
                btFinalizar.setDisable(false);
                alert = new Alert(Alert.AlertType.INFORMATION, "Recebimento estornado com sucesso!", ButtonType.CLOSE);
                alert.showAndWait();

                carregaTabela('L');
                estadoEdicao();
                pnRegistro.setDisable(true);

            }
        }

    }

    @FXML
    private void evtCancelarOperacoes(ActionEvent event) {

        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Deseja cancelar todas as operações realizadas?", ButtonType.YES, ButtonType.NO);

        if (a.showAndWait().get() == ButtonType.YES) {

            carregaTabela('I');
            estadoOriginal();
        }
    }

    @FXML
    private void evtFinalizar(ActionEvent event) {
        
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Deseja salvar todas modificações?", ButtonType.YES, ButtonType.NO);
        boolean ok = false;
        
        if (a.showAndWait().get() == ButtonType.YES) {

            Collections.sort(recebs, Comparator.comparing(Recebimentos::getRecebimentoid));
            DALRecebimento dale = new DALRecebimento();
            ok = dale.InserirTudo(recebs);

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

    private void setaDatas(boolean flag, char data) {

        switch (data) {
            case 'E':
                dtEmissFim.setDisable(!flag);
                dtEmissIni.setDisable(!flag);
                dtVencIni.setDisable(flag);
                dtVencFim.setDisable(flag);
                dtPagFim.setDisable(flag);
                dtPagaIni.setDisable(flag);
                break;
            case 'V':
                dtEmissFim.setDisable(flag);
                dtEmissIni.setDisable(flag);
                dtVencIni.setDisable(!flag);
                dtVencFim.setDisable(!flag);
                dtPagFim.setDisable(flag);
                dtPagaIni.setDisable(flag);
                break;
            case 'P':
                dtEmissFim.setDisable(flag);
                dtEmissIni.setDisable(flag);
                dtVencIni.setDisable(flag);
                dtVencFim.setDisable(flag);
                dtPagFim.setDisable(!flag);
                dtPagaIni.setDisable(!flag);
                break;
            default:
                 dtEmissFim.setDisable(flag);
                dtEmissIni.setDisable(flag);
                dtVencIni.setDisable(flag);
                dtVencFim.setDisable(flag);
                dtPagFim.setDisable(flag);
                dtPagaIni.setDisable(flag);
                break;
        }

    }
    
    @FXML
    private void evtCBfiltro(ActionEvent event) {

        String filtro = cbSelecionarFiltro.getSelectionModel().getSelectedItem();

        cbStatus.setDisable(false);
        if (filtro.contains("Data")) {

            txAlunoFiltro.setDisable(true);
            
            if (filtro.contains("Emissão")) {
                setaDatas(true, 'E');

                dtEmissIni.setValue(LocalDate.now());
                dtEmissFim.setValue(LocalDate.now().plusDays(30));
            } else if (filtro.contains("Vencimento")) {
                setaDatas(true, 'V');

                dtVencIni.setValue(LocalDate.now());
                dtVencFim.setValue(LocalDate.now().plusDays(30));
            } else {
                setaDatas(true, 'P');
                dtPagaIni.setValue(LocalDate.now());
                dtPagFim.setValue(LocalDate.now().plusDays(30));
                cbStatus.setSelected(true);
                cbStatus.setDisable(true);
            }

           
        } else {

            txAlunoFiltro.setDisable(false);
            setaDatas(true, 'a');
        }
    }
    
}
