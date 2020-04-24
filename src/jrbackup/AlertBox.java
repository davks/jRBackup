/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jrbackup;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 *
 * @author david
 */
public class AlertBox {

    private AlertBox() {
    }

    public static void displayOK(String title, String header, String content, ActionEvent event, Alert.AlertType alertType) {
        //alert.initOwner(((Node) event.getSource()).getScene().getWindow());
        Node node = (Node) event.getSource();
        AlertBox.displayOK(title, header, content, node, alertType);
    }

    public static void displayOK(String title, String header, String content, Node node, Alert.AlertType alertType) {
        if (alertType.equals(Alert.AlertType.CONFIRMATION)) {
            alertType = Alert.AlertType.INFORMATION;
        }
        Alert alert = new Alert(alertType);

        // fixnutí velikosti dialogu při jeho otevření
        alert.setResizable(true);
        //alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.setTitle(title);
        alert.setHeaderText(header);
        Label label = new Label(content);
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);

        //alert.setContentText(content);
        alert.initOwner((Stage) node.getScene().getWindow());

        /*Platform.runLater(() -> {
            //alert.getDialogPane().getScene().getWindow().sizeToScene();
            alert.setResizable(false);
        });*/
        alert.showAndWait();
    }

    public static boolean displayYN(String title, String header, String content, ActionEvent event) {
        Node node = (Node) event.getSource();
        return displayYN(title, header, content, node);
    }

    public static boolean displayYN(String title, String header, String content, Node node) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        // fixnutí velikosti dialogu při jeho otevření
        alert.setResizable(true);
        //alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.setTitle(title);
        alert.setHeaderText(header);
        Label label = new Label(content);
        label.setWrapText(true);
        alert.getDialogPane().setContent(label);

        //alert.setContentText(content);
        alert.initOwner((Stage) node.getScene().getWindow());

        ButtonType buttonYes = new ButtonType("Ano", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Ne", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(buttonNo, buttonYes);

        Optional<ButtonType> result = alert.showAndWait();

        return result.get() == buttonYes;
    }

    public static String displayInput(String title, String header, ActionEvent event) {
        Node node = (Node) event.getSource();
        return displayInput(title, header, node);
    }

    public static String displayInput(String title, String header, Node node) {
        TextInputDialog dialog = new TextInputDialog();

        // fixnutí velikosti dialogu při jeho otevření
        dialog.setResizable(true);
        //dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        dialog.setTitle("");
        dialog.setHeaderText("");
        dialog.setContentText("");

        dialog.initOwner((Stage) node.getScene().getWindow());

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            return result.get();
        }

        return "";
    }

    public static String displayPassword(String title, String header, ActionEvent event) {
        Node node = (Node) event.getSource();
        return displayPassword(title, header, node);

    }

    public static String displayPassword(String title, String header, Node node) {
        PasswordDialog dialog = new PasswordDialog();

        // fixnutí velikosti dialogu při jeho otevření
        dialog.setResizable(true);
        //dialog.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        dialog.setTitle(title);
        dialog.setHeaderText(header);

        dialog.initOwner((Stage) node.getScene().getWindow());

        ButtonType buttonOK = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType buttonCancel = new ButtonType("Zrušit", ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().setAll(buttonOK, buttonCancel);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.get() == buttonOK) {
            return dialog.getPasswordField().getText();
        }

        return null;
    }

}
