/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import startenglish.db.DAL.DALLogin;
import startenglish.db.Entidades.Login;
import startenglish.util.EnviaEmail;

/**
 * FXML Controller class
 *
 * @author azeve
 */
public class SignUpController implements Initializable {

    @FXML
    private JFXTextField txtUser;
    @FXML
    private HBox txUser;
    @FXML
    private JFXButton btRecuperar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUser.setStyle("-fx-text-fill: white;");
    }    

    @FXML
    private void evtRecuperar(MouseEvent event) {
        DALLogin dale = new DALLogin();
           Login log = new Login();
           Alert a = new Alert(Alert.AlertType.CONFIRMATION);
           if(!txtUser.getText().isEmpty())
           {
               log = dale.get(txtUser.getText());
           
                if(log == null)
                {
                    a = new Alert(Alert.AlertType.ERROR,"Usuário incorreto ou inexistente!!", ButtonType.OK);
                    txtUser.requestFocus();
                    a.showAndWait();
                }
                else{
                    startenglish.util.EnviaEmail enviar = new EnviaEmail(log.getFunc().getEmail(), log.getSenha());
//                      startenglish.util.EnviaEmail enviar = new EnviaEmail("azv.g14@gmail.com", log.getSenha());
                }
           }
           else
           {
               a = new Alert(Alert.AlertType.ERROR,"Digite o usuário para recuperar a senha!", ButtonType.OK);
               txtUser.requestFocus();
               a.showAndWait();
           }
        
    }
    
}
