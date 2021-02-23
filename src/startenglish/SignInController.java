/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startenglish;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import startenglish.db.DAL.DALLogin;
import startenglish.db.Entidades.Login;

/**
 * FXML Controller class
 *
 * @author azeve
 */
public class SignInController implements Initializable {
    
    @FXML
    private JFXButton btLogin;
    @FXML
    private JFXTextField txtUsuario;
    @FXML
    private JFXPasswordField txtPassword;
    
    public static Login loginConectado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtUsuario.setStyle("-fx-text-fill: white;");
        txtPassword.setStyle("-fx-text-fill: white;");
    }    

    @FXML
    private void evtLogin(MouseEvent event) {
        
           DALLogin dale = new DALLogin();
           Login log = new Login();
           
           Alert a = new Alert(Alert.AlertType.CONFIRMATION);
           if(!txtUsuario.getText().isEmpty() && !txtPassword.getText().isEmpty())
           {
               log = dale.get(txtUsuario.getText());
           
                if(log == null)
                {
                    a = new Alert(Alert.AlertType.ERROR,"Usuário incorreto ou inexistente!!", ButtonType.OK);
                    txtUsuario.requestFocus();
                    a.showAndWait();
                }
                else{
                    if(txtPassword.getText().equals(log.getSenha()))
                    {
                        loginConectado = log;
                        try {
                             Parent aux = FXMLLoader.load(getClass().getResource("view/FXMLPrincipal.fxml"));
                             FXMLLoginController.BPprincipal.setCenter(aux);


                         } catch (Exception e) {
                             //System.out.println("Jorge");
                             System.out.println(e.getMessage());
                         }
                    }
                    else
                    {
                        a = new Alert(Alert.AlertType.ERROR,"Senha incorreta!!", ButtonType.OK);
                        txtPassword.requestFocus();
                        a.showAndWait();
                    }
                }
           }
           else
           {
               a = new Alert(Alert.AlertType.ERROR,"Digite o usuário e senha!", ButtonType.OK);
               txtUsuario.requestFocus();
               a.showAndWait();
           }
           
        
        
        
    }
    
}
