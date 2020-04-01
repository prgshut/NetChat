package ru.geekbrains.java.client.view.auth;

        import java.io.IOException;
        import java.net.URL;
        import java.util.ResourceBundle;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.control.PasswordField;
        import javafx.scene.control.TextField;
        import ru.geekbrains.java.client.controller.ClientController;

public class AuthControl {

    private ClientController controller;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginFild;

    @FXML
    private PasswordField passFild;

    @FXML
    private Button conectButton;
    @FXML
    void authBut(ActionEvent event) {
String login = loginFild.getText().trim();
String pass = passFild.getText().trim();
        try {
            controller.sendAuthMessage(login,pass);
        } catch (IOException e) {
            System.err.println("Ошибка подключения");
        }
    }

    @FXML
    private Button cancelButton;

    @FXML
    void initialize() {
        assert loginFild != null : "fx:id=\"LoginFild\" was not injected: check your FXML file 'AuthForm.fxml'.";
        assert passFild != null : "fx:id=\"PassFild\" was not injected: check your FXML file 'AuthForm.fxml'.";
        assert conectButton != null : "fx:id=\"ConectButton\" was not injected: check your FXML file 'AuthForm.fxml'.";
        assert cancelButton != null : "fx:id=\"CancelButton\" was not injected: check your FXML file 'AuthForm.fxml'.";

    }

    public void setController(ClientController controller) {
        this.controller = controller;
    }
}
