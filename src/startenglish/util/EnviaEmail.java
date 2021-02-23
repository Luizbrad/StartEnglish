/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startenglish.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author azeve
 */
public class EnviaEmail {

    public EnviaEmail(String emailLogin, String senhaEsquecida) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        String meuEmail = "gsilvadeazevedo@gmail.com";
        String minhaSenha = "484225684";
        
        SimpleEmail email = new SimpleEmail();
        email.setHostName("smtp.gmail.com");
        email.setAuthenticator(new DefaultAuthenticator(meuEmail, minhaSenha));
        email.setSmtpPort(465);
        
        email.setSSLOnConnect(true);
        
        try {
            email.setFrom(meuEmail);
            email.setSubject("Esqueceu sua senha?");
            email.setMsg("Sua senha é: "+senhaEsquecida);
            
            email.addTo(emailLogin);
            email.send();
            a = new Alert(Alert.AlertType.ERROR,"Sua senha foi enviada para seu E-mail cadastrado!!", ButtonType.OK);
            a.showAndWait();
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    
    
    
}
