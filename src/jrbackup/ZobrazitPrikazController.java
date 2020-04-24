package jrbackup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ZobrazitPrikazController implements Initializable {

    @FXML
    private TextArea taPrikaz;

    @FXML
    void onZavrit(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void zobrazitText(String text) {
        taPrikaz.setText(text);
    }

    public void zobrazitText(List<String> parametry) {
        StringBuilder sb = new StringBuilder();

        parametry.forEach(s -> {
            sb.append(s).append(" ");
        });

        taPrikaz.setText(sb.toString());
    }
}
