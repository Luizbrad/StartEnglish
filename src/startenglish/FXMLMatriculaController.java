package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import startenglish.db.DAL.DALAluno;
import startenglish.db.DAL.DALLivro;
import startenglish.db.DAL.DALMatricula;
import startenglish.db.DAL.DALRecebimento;
import startenglish.db.DAL.DALTurma;
import startenglish.db.Entidades.Aluno;
import startenglish.db.Entidades.Caixa;
import startenglish.db.Entidades.Livro;
import startenglish.db.Entidades.Matricula;
import startenglish.db.Entidades.Recebimentos;
import startenglish.db.Entidades.Turma;
import startenglish.db.util.Banco;
import startenglish.util.MaskFieldUtil;

public class FXMLMatriculaController implements Initializable 
{

    @FXML
    private AnchorPane pndados;
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
    private JFXCheckBox checkSegunda;
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
    private JFXButton btPesquisar;
    @FXML
    private JFXTextField txPesquisa;
    @FXML
    private JFXComboBox<String> cbFiltro;
    @FXML
    private TableView<Matricula> tabela;
    @FXML
    private TableColumn<Matricula, String> tabelaTurma;
    @FXML
    private TableColumn<Matricula, String> tabelaAtivo;
    @FXML
    private JFXTextField txNumMatricula;
    @FXML
    private JFXComboBox<Aluno> cbAluno;
    @FXML
    private JFXTextField txCPF;
    @FXML
    private JFXTextField txNomeResp;
    @FXML
    private JFXTextField txEscola;
    @FXML
    private JFXTextField txEmail;
    @FXML
    private JFXTextField txNivel;
    @FXML
    private JFXCheckBox checkAtivo;
    @FXML
    private JFXTextField txValor;
    @FXML
    private JFXTextField txDesconto;
    @FXML
    private JFXComboBox<String> cbParcelas;
    @FXML
    private JFXComboBox<Livro> cbLivro;
    @FXML
    private JFXButton btGerar;
    @FXML
    private JFXButton btVerificar;
    @FXML
    private TableColumn<Matricula, String> tabelaAluno;
    @FXML
    private TableColumn<Matricula, String> tabelaEmail;
    @FXML
    private TableColumn<Matricula, String> tabelaCPF;
    @FXML
    private TableColumn<Matricula, String> tabelaNivel;
    @FXML
    private JFXComboBox<Turma> cbTurma;
    @FXML
    private JFXTextField txCausa;
    @FXML
    private JFXCheckBox checkAtivoPesq;
    private String filtro_atual;
    @FXML
    private JFXCheckBox checkVerif;
    @FXML
    private JFXComboBox<Integer> cbDiaVenc;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
       tabelaAluno.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAluno().getNome()));
       tabelaEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAluno().getEmail()));
       tabelaCPF.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAluno().getCpf()));
       tabelaNivel.setCellValueFactory(new PropertyValueFactory("nivel"));
       tabelaTurma.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTurmaID().getNome()));
       tabelaAtivo.setCellValueFactory(new PropertyValueFactory("ativo"));
       List<Integer> diav = new ArrayList();
        for (int i = 1; i < 20; i++)
        {
            diav.add(i);            
        }
       ObservableList<Integer> modelodiav = FXCollections.observableArrayList(diav);
       cbDiaVenc.setItems(modelodiav);
       cbDiaVenc.getSelectionModel().selectFirst();
       
       MaskFieldUtil.maxField(txNomeResp,29);
       MaskFieldUtil.maxField(txEscola,50);
       MaskFieldUtil.maxField(txNivel,15);
       MaskFieldUtil.monetaryField(txValor);
       MaskFieldUtil.numericField(txDesconto);
       MaskFieldUtil.maxField(txCausa,255);
       MaskFieldUtil.maxField(txPesquisa,29);
       
       List<String> combo = new ArrayList();
        combo.add("Aluno");
        combo.add("CPF");
        combo.add("Nível");
       
        
        ObservableList<String> modelo = FXCollections.observableArrayList(combo);
        cbFiltro.setItems(modelo);
        cbFiltro.setValue("Aluno");
        filtro_atual = cbFiltro.getSelectionModel().getSelectedItem();
        
        List<Aluno> comboAlunos = new ArrayList();
        DALAluno prof = new DALAluno();
        comboAlunos=prof.get("");
        ObservableList<Aluno> modeloAlu = FXCollections.observableArrayList(comboAlunos);
        cbAluno.setItems(modeloAlu);
        
        List<Livro> comboLivros = new ArrayList();
        DALLivro liv = new DALLivro();
        comboLivros=liv.get("");
        ObservableList<Livro> modeloLiv = FXCollections.observableArrayList(comboLivros);
        cbLivro.setItems(modeloLiv);
        cbLivro.getSelectionModel().selectFirst();
        EstadoOriginal();
       
    }    
    
    private void CarregaTabela(String filtro)
    {
        DALMatricula dalm = new DALMatricula();       
        List <Matricula> listaMat = dalm.get(filtro);
        listaMat = dalm.get(filtro);           
        ObservableList <Matricula> modelo;
        modelo = FXCollections.observableArrayList(listaMat);       
        tabela.setItems(modelo);
    }
    
    private void EstadoOriginal()
    {
        cbTurma.setDisable(true);
        cbAluno.setDisable(true);
        cbAluno.getSelectionModel().clearSelection();
        cbLivro.getSelectionModel().selectFirst();
        txEmail.setDisable(true);
        txCPF.setDisable(true);
        txNomeResp.setDisable(true);
        txEscola.setDisable(true);
        txNivel.setDisable(true);
        txValor.setDisable(true);
        txDesconto.setDisable(true);
        txCausa.setDisable(true);
        cbLivro.setDisable(true);
        cbParcelas.setDisable(true);
        cbParcelas.getItems().clear();
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
        checkVerif.setSelected(false);
        btVerificar.setDisable(true);
        btGerar.setDisable(true);
        btInserir.setDisable(false);
        btAlterar.setDisable(true);
        btExcluir.setDisable(true);
        btCancelar.setDisable(false);
        btConfirmar.setDisable(true);
        txPesquisa.clear();
        cbFiltro.setDisable(false);
        ObservableList <Node> componentes=pndados.getChildren();
        for(Node n : componentes)
        {
            if (n instanceof TextInputControl)  // textfield, textarea e htmleditor
                ((TextInputControl)n).setText("");
        }
        CarregaTabela("");
    }
    private void EstadoEdicao()
    {
        cbAluno.setDisable(false);
        cbLivro.setDisable(false);
        cbLivro.getSelectionModel().selectFirst();
        cbAluno.getSelectionModel().selectFirst();
        txNomeResp.setDisable(false);
        txEscola.setDisable(false);
        txNivel.setDisable(false);
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
        txValor.setDisable(false);
        txDesconto.setDisable(false);
        btVerificar.setDisable(false);
        btGerar.setDisable(false);
    }
    
    @FXML
    private void evtInserir(ActionEvent event) 
    {
        EstadoEdicao();
        cbAluno.getSelectionModel().selectFirst();
        checkAtivo.setSelected(true);
    }

    @FXML
    private void evtAlterar(ActionEvent event) 
    {
        if(tabela.getSelectionModel().getSelectedItem() != null)
        {     
            EstadoEdicao();
            Matricula m = tabela.getSelectionModel().getSelectedItem();
            Double auxiliar;
            txCausa.setText(""+m.getInfoCancelamento());
            txDesconto.setText(""+m.getDesconto());           
            txEscola.setText(m.getInstuiEnsino());
            txNivel.setText(m.getNivel());
            txNomeResp.setText(m.getNomeResponsável());
            txNumMatricula.setText(""+m.getNummat());
            if(m.getAtivo()=='A')
            {
                txCausa.setDisable(true);
                checkAtivo.setSelected(true);
            }
            else
            {
                txCausa.setDisable(false);
                checkAtivo.setSelected(false);
            }
                
            auxiliar=m.getValor();
            int conversor= auxiliar.intValue();
            String auxiliardec;
            auxiliar=auxiliar-conversor;
            auxiliardec=auxiliar.toString();
            txValor.setText(""+m.getValor());
            if(auxiliardec.length()==3 && auxiliardec.charAt(2)!='0')
                txValor.setText(txValor.getText()+'0');
            if(auxiliar==0)
                txValor.setText(txValor.getText()+'0');
            
            cbAluno.getSelectionModel().select(m.getAluno());
            cbLivro.getSelectionModel().select(m.getLivro());
            txCPF.setText(m.getAluno().getCpf());
            txEmail.setText(m.getAluno().getEmail());
            cbParcelas.setDisable(false);
            
            cbTurma.getItems().clear();
            List<Turma> comboTurmas = new ArrayList();
            DALTurma Tur = new DALTurma();
            comboTurmas.add(Tur.get(m.getTurmaID().getTurmaID()));
            cbTurma.setDisable(false);
            ObservableList<Turma> modeloTur = FXCollections.observableArrayList(comboTurmas);               
            cbTurma.setItems(modeloTur);
            cbTurma.getSelectionModel().selectFirst();
            checkVerif.setSelected(true);
            cbParcelas.setDisable(true);
        }
    }

    @FXML
    private void evtExcluir(ActionEvent event) 
    {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar exclusão?", ButtonType.YES,ButtonType.NO);
            boolean ok = true;

            if(a.showAndWait().get() == ButtonType.YES)
            {           
                Matricula m = new Matricula();
                DALMatricula dalm = new DALMatricula();

                m = tabela.getSelectionModel().getSelectedItem();

                try{

                    Banco.getCon().getConnect().setAutoCommit(false);

                    ok = dalm.apagar(m.getNummat());

                    if(ok)
                    {                   
                         Banco.getCon().getConnect().commit();
                        a =  new Alert(Alert.AlertType.CONFIRMATION," Exclusão ocorrida com sucesso!!", ButtonType.OK);
                    }
                    else
                    {                  
                        Banco.getCon().getConnect().rollback();
                        a =  new Alert(Alert.AlertType.ERROR, "Erro ao deletar matrícula!", ButtonType.OK);
                    }

                    a.showAndWait();

                }catch(SQLException ex){System.out.println(ex.getMessage());}

                EstadoOriginal();    
                CarregaTabela("");
            
            }
    }
 private boolean validatxt(String nivel,String valor)
    {
        boolean ok=true;
        Alert a=null;
        if(nivel.isEmpty())
        {        
            ok = false;
            txNivel.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txNivel.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Nível obrigatório!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
            
        }
        else
        {
            txNivel.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
        }
        if(valor.isEmpty())
        {        
            ok = false;
            txValor.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            
            if(a==null)
            {
                txValor.requestFocus();
                a = new Alert(Alert.AlertType.WARNING, "Valor obrigatório!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
            
        }
        else
        {
            txValor.setStyle("-fx-border-width: 0;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
        }
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
        boolean ok = true,problema = false;
        
        try{
            
            preco_inserido = Double.parseDouble(auxiliar);
            
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
    private boolean verificacoes()
    {
        boolean ok=true;
        Alert a=null;
        if(!checkVerif.isSelected())
        {
           ok = false;
            
           a = new Alert(Alert.AlertType.WARNING, "Turma não selecionada", ButtonType.CLOSE);
        }
        if(cbParcelas.getSelectionModel().isEmpty())
        {
            ok=false;
            a= new Alert(Alert.AlertType.WARNING,"Parcelas não selecionadas",ButtonType.CLOSE);
        }
        if(cbAluno.getSelectionModel().isEmpty())
        {
            ok=false;
            a= new Alert(Alert.AlertType.WARNING,"Aluno não selecionado!!",ButtonType.CLOSE);
        }
        if(a != null)
            a.showAndWait();
        return ok;
    }
    @FXML
    private void evtConfirmar(ActionEvent event) 
    {
        if(validatxt(txNivel.getText(),txValor.getText()) && validaPreco(txValor.getText()) && verificacoes())
        {
            int cod,desconto;
            String semana="";
            boolean ok2=true;
            Double valor;
             String auxiliar="";
            for (int i = 0; i < txValor.getText().length(); i++)
            {
                if(txValor.getText().charAt(i)!='.')
                {
                    if(txValor.getText().charAt(i)==',')
                        auxiliar+='.';
                    else
                        auxiliar+=txValor.getText().charAt(i);
                }
            }
            try
            {
                cod = Integer.parseInt(txNumMatricula.getText());
                
            }
            catch (NumberFormatException ex) 
            {
                cod = 0;    
                
            }
            try{
                if(!txDesconto.getText().isEmpty())
                    desconto=Integer.parseInt(txDesconto.getText());
                else
                    desconto=0;
                valor=Double.parseDouble(auxiliar);
            }
            catch(NumberFormatException ex)
            {
                desconto=0;
                valor=0.0;
            }

                DALMatricula dalma = new DALMatricula();
                List<Matricula> comboMatri = new ArrayList();
                
                comboMatri=dalma.get("where alunoid="+cbAluno.getSelectionModel().getSelectedItem().getID());
                ObservableList<Matricula> modeloMatri = FXCollections.observableArrayList(comboMatri);
                
                for (int i = 0; i < modeloMatri.size(); i++)
                {
                    if(modeloMatri.get(i).getAtivo()=='A')
                        ok2=false;
                }
            
            if(ok2 || cod!=0)
            {
                DALMatricula dalm = new DALMatricula();
            Matricula m = new Matricula();
            m.setAluno(cbAluno.getSelectionModel().getSelectedItem());
            m.setDesconto(desconto);
            m.setInfoCancelamento(" "+txCausa.getText());
            m.setInstuiEnsino(" "+txEscola.getText());
            m.setLivro(cbLivro.getSelectionModel().getSelectedItem());
            m.setNivel(txNivel.getText());
            m.setNomeResponsável(" "+txNomeResp.getText());
            m.setTurmaID(cbTurma.getSelectionModel().getSelectedItem());
            m.setValor(valor);
            if(checkAtivo.isSelected())
                m.setAtivo('A');
            else
                m.setAtivo('F');
            m.setNummat(cod);
            Alert a = null;
            boolean ok,okp;
            try
                {
                    Banco.getCon().getConnect().setAutoCommit(false);

                    if (cod == 0)
                    {
                        ok = dalm.gravar(m);
                        /*
                        DALRecebimento dalr = new DALRecebimento();
                        String numparc="";
                        for (int i = 0; cbParcelas.getSelectionModel().getSelectedItem().charAt(i)!=' '; i++)
                        {
                            numparc=numparc+cbParcelas.getSelectionModel().getSelectedItem().charAt(i);
                        }   
                        Caixa caixa = new Caixa(1);
                        LocalDate d=LocalDate.now();
                        d=d.withDayOfMonth(cbDiaVenc.getSelectionModel().getSelectedItem());
                        if(cbDiaVenc.getSelectionModel().getSelectedItem()<LocalDate.now().getDayOfMonth())
                            d=d.withMonth(d.getMonthValue()+2);
                        else
                            d=d.withMonth(d.getMonthValue()+1);
                        
                        String auxiliarP;
                        if(cbParcelas.getSelectionModel().getSelectedItem().charAt(1)==' ')
                            auxiliarP=cbParcelas.getSelectionModel().getSelectedItem().substring(18);
                        else
                            auxiliarP=cbParcelas.getSelectionModel().getSelectedItem().substring(19);
                        for (int i = 0; i < auxiliarP.length(); i++)
                        {
                            if(cbParcelas.getSelectionModel().getSelectedItem().charAt(i)!='.')
                            {
                                if(cbParcelas.getSelectionModel().getSelectedItem().charAt(i)==',')
                                    auxiliarP+='.';
                                else
                                    auxiliarP+=cbParcelas.getSelectionModel().getSelectedItem().charAt(i);
                            }
                        }
                        Double valparc=Double.parseDouble(auxiliarP);

                        for (int i = 0; i < Integer.parseInt(numparc); i++)
                        {
                            Recebimentos r = new Recebimentos();
                            
                            r.setCaixa(caixa);
                            r.setDtemissoa(LocalDate.now());
                            r.setDtvencimento(d);
                            r.setMat(m);
                            r.setValor(valparc);
                            
                            dalr.inserir(r);
                        }*/

                        if (ok)
                        {
                            DALTurma daltu = new DALTurma();
                            daltu.subqtd(m.getTurmaID());
                            a = new Alert(Alert.AlertType.CONFIRMATION, "Matrícula inserida!!", ButtonType.OK);
                        } 
                        else 
                        {
                            a = new Alert(Alert.AlertType.ERROR, "Problemas ao inserir a Matrícula!!", ButtonType.OK);
                        }

                    } 
                    else
                    { 
                        Turma quanti = m.getTurmaID();
                        ok = dalm.atualizar(m);
                        if(ok)
                        {
                            DALTurma daltu = new DALTurma();
                            if(m.getAtivo()=='A')
                            {
                                daltu.subqtd(m.getTurmaID());
                                daltu.addqtd(quanti.getTurmaID(),quanti.getQtdvagas());
                            }                           
                            a = new Alert(Alert.AlertType.CONFIRMATION, "Matrícula atualizada!!", ButtonType.OK);
                        } 
                        else
                        {
                            a = new Alert(Alert.AlertType.ERROR, "Problemas ao atualizar a Matrícula!!", ButtonType.OK);
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
            else
            {
                Alert a = new Alert(Alert.AlertType.ERROR, "Aluno ainda possui matrícula em aberto!!", ButtonType.OK);
                a.showAndWait();
            }
            
        }
    }

    @FXML
    private void evtCancelar(ActionEvent event) 
    {
        if (!cbAluno.isDisabled())
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
    private void evtPesquisar(ActionEvent event) {
        
        String pesquisa = txPesquisa.getText();
        String sql;               
        
        if(filtro_atual.equals("Aluno"))
             sql = " inner join aluno on(aluno.alunoid = matricula.alunoid) where aluno.nome like  '%"+pesquisa+"%'";
        else if(filtro_atual.equals("CPF"))
          sql = " inner join aluno on (aluno.alunoid = matricula.alunoid) where aluno.cpf like '%"+pesquisa+"%'";
        else
           sql = " where matricula.nivel like '%"+pesquisa+"%'";
            
        if(checkAtivoPesq.isSelected())
            sql += " and matricula.ativo = 'A'";
        
        CarregaTabela(sql);
    }

    @FXML
    private void evtFiltroTxt(ActionEvent event) {
        
        filtro_atual = cbFiltro.getSelectionModel().getSelectedItem();
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
    private void notVerifica(ActionEvent event) 
    {
        checkVerif.setSelected(false);
        cbTurma.setDisable(true);
        cbTurma.getItems().clear();
    }

    private void notVerifica(InputMethodEvent event) 
    {
        checkVerif.setSelected(false);
        cbTurma.setDisable(true);
        cbTurma.getItems().clear();
    }

    @FXML
    private void evtPreenche(ActionEvent event)
    {
        if(!cbAluno.getSelectionModel().isEmpty())
        {
            txCPF.setText(cbAluno.getSelectionModel().getSelectedItem().getCpf());
            txEmail.setText(cbAluno.getSelectionModel().getSelectedItem().getEmail());
        }
        
    }
    
    @FXML
    private void evtGerar(ActionEvent event) 
    {
        String auxiliar=txValor.getText();
        if(validaPreco(auxiliar))
        {
            DecimalFormat df = new DecimalFormat("#.##");
            auxiliar=auxiliar.replace(',', '.');
                           
                int desc;
                double valor=Double.parseDouble(auxiliar);
                if(!txDesconto.getText().isEmpty())
                    desc=Integer.parseInt(txDesconto.getText());
                else
                    desc=0;
                if(desc>100)
                    desc=100;
                double total=valor-((valor*desc)/100);
                double par;
                List<String> cpar = new ArrayList();
                for (int i = 1; i <= 20; i++)
                {                    
                    par=total/i;
                    
                    cpar.add(i+" parcela(s) de R$"+df.format(par));  
                }
                ObservableList<String> modelopar = FXCollections.observableArrayList(cpar);
                cbParcelas.getItems().clear();
                cbParcelas.setItems(modelopar);
                cbParcelas.getSelectionModel().selectFirst();
                cbParcelas.setDisable(false);
        }
        else
        {
            txValor.setStyle("-fx-border-color: red; -fx-border-width: 2;"
                    + "-fx-background-color: #BEBEBE;"
                    + "-fx-font-weight: bold;");
            txValor.requestFocus();
               Alert a = new Alert(Alert.AlertType.WARNING, "Valor inválido!!", ButtonType.CLOSE);            
                a.showAndWait();
        }
    }

    @FXML
    private void evtCausa(ActionEvent event)
    {
        if(checkAtivo.isSelected())
            txCausa.setDisable(true);
        else
            txCausa.setDisable(false);
    }

    @FXML
    private void evtVerificarDisp(ActionEvent event) 
    {
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
            
            List<Turma> comboTurmas = new ArrayList();
            DALTurma Tur = new DALTurma();
            comboTurmas=Tur.get("");
            
            boolean retira;
            for (int i = 0; i < comboTurmas.size(); i++)
            {
                retira=false;
                Turma aux= comboTurmas.get(i);
                if(retira==false && aux.getSemana().charAt(0)=='1' && semana.charAt(0)=='0')
                    retira=true;
                else if(aux.getSemana().charAt(1)=='1' && semana.charAt(1)=='0')
                    retira=true;
                else if(aux.getSemana().charAt(2)=='1' && semana.charAt(2)=='0')
                    retira=true;
                else if(aux.getSemana().charAt(3)=='1' && semana.charAt(3)=='0')
                    retira=true;
                else if(aux.getSemana().charAt(4)=='1' && semana.charAt(4)=='0')
                    retira=true;
                else if(aux.getSemana().charAt(5)=='1' && semana.charAt(5)=='0')
                    retira=true;
                else if(aux.getSemana().charAt(6)=='1' && semana.charAt(6)=='0')
                    retira=true;
                if(aux.getQtdvagas()<=0)
                    retira=true;
                if(retira==true)
                {
                    comboTurmas.remove(i);
                    i--;
                }                                  
            }
            
            if(comboTurmas.size()>0)
            {               
                cbTurma.setDisable(false);
                ObservableList<Turma> modeloTur = FXCollections.observableArrayList(comboTurmas);
                cbTurma.getItems().clear();
                cbTurma.setItems(modeloTur);
                cbTurma.getSelectionModel().selectFirst();
                checkVerif.setSelected(true);
                
            }
            else
            {
                checkVerif.setSelected(false);          
               Alert a = new Alert(Alert.AlertType.WARNING, "Não existem turmas nas datas informadas!!", ButtonType.CLOSE);            
                a.showAndWait();
            }
                
            
    }
    
}
