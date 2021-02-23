package startenglish;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javax.swing.plaf.ColorUIResource;

/**
 *
 * @author azeve
 */
public class FXMLLoginController implements Initializable {
    
    public static BorderPane BPprincipal;
    
    @FXML 
    private VBox vbox;
    private Parent fxml;
    @FXML
    public static AnchorPane APLogin;
    @FXML
    private BorderPane telaPrincipal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        BPprincipal = telaPrincipal;
        
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        
        
        try {
            fxml = FXMLLoader.load(getClass().getResource("view/SignIn.fxml"));
            vbox.getChildren().removeAll();
            vbox.getChildren().setAll(fxml);
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
               
        t.setToX(vbox.getLayoutX() * 20);
        t.play();
        
    } 
    
    @FXML
    private void open_signin(ActionEvent event)
    {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        
        t.setToX(vbox.getLayoutX() * 20);
        t.play();
        t.setOnFinished((e)-> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("view/SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            } catch(IOException ex) {
            }
        });
    }
    
    @FXML
    private void open_signup(ActionEvent event)
    {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        
        t.setToX(0);
        t.play();
        t.setOnFinished((e)-> {
            try {
                fxml = FXMLLoader.load(getClass().getResource("view/SignUp.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            } catch(IOException ex) {
            }
        });
    }
    
    private void close_login(ActionEvent event)
    {
        System.exit(0);
    }

   
}
