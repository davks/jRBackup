/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrbackup;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JRBackup extends Application {
    
    private static final String VERSION = "1.6.1";

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/jrbackup/main_window.fxml"));
        Parent root = loader.load();
        MainWindowController controller = loader.getController();
        controller.setVersion(VERSION);
        
        primaryStage.getIcons().add(new Image("/jrbackup/icons/jRBackup-48.png"));
        
        primaryStage.setTitle("jRBackup");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


}
