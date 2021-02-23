/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startenglish;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import startenglish.db.Entidades.Falta;
import startenglish.db.Entidades.Matricula;

/**
 * FXML Controller class
 *
 * @author azeve
 */
public class FXMLFaltasController implements Initializable {

    @FXML
    private AnchorPane pndados;
    @FXML
    private TableView<Falta> tabela;
    @FXML
    private TableColumn<Matricula, Integer> tabelaIDMatricula;
    @FXML
    private TableColumn<Matricula, String> tabelaAluno;
    @FXML
    private TableColumn<Falta, CheckBox> tabelaPresenca;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private JFXButton btConfirmar;
    @FXML
    private JFXButton btAbrir;
    
    ObservableList<Falta> list = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void evtClickTabela(MouseEvent event) {
    }

    @FXML
    private void evtAbrir(ActionEvent event) {
    }


    @FXML
    private void evtCancelar(ActionEvent event) {
    }

    @FXML
    private void evtConfirmar(ActionEvent event) {
    }
    
}
