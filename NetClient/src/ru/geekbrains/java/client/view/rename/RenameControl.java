package ru.geekbrains.java.client.view.rename;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.geekbrains.java.client.controller.ClientController;

public class RenameControl {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label oldUserName;

    @FXML
    private TextField newUserName;

    @FXML
    private Button renameButton;

    private ClientController controller;

    @FXML
    void renameOn(ActionEvent event) {
        String name = newUserName.getText().trim();
        if (!name.isEmpty()) {
            controller.renameUserName(name);
        }
    }

    @FXML
    void initialize() {
        assert oldUserName != null : "fx:id=\"oldUserName\" was not injected: check your FXML file 'RenameForm.fxml'.";
        assert newUserName != null : "fx:id=\"newUserName\" was not injected: check your FXML file 'RenameForm.fxml'.";
        assert renameButton != null : "fx:id=\"renameButton\" was not injected: check your FXML file 'RenameForm.fxml'.";
    }

    public void setController(ClientController controller) {
        this.controller = controller;
        oldUserName.setText(controller.getUsername());

    }

}
