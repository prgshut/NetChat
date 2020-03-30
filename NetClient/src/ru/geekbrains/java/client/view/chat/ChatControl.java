package ru.geekbrains.java.client.view.chat;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import ru.geekbrains.java.client.controller.ClientController;

public class ChatControl {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> clientList;

    @FXML
    private ListView<String> chatFild;

    @FXML
    private TextField sendFild;

    @FXML
    private Button sendButton;
    private ClientController controller;

    @FXML
    void send(ActionEvent event) {
        sendMessage();
    }

    @FXML
    void initialize() {
        assert clientList != null : "fx:id=\"ClientList\" was not injected: check your FXML file 'ChatForm.fxml'.";
        assert chatFild != null : "fx:id=\"ChatFild\" was not injected: check your FXML file 'ChatForm.fxml'.";
        assert sendFild != null : "fx:id=\"SendFild\" was not injected: check your FXML file 'ChatForm.fxml'.";
        assert sendButton != null : "fx:id=\"SendButton\" was not injected: check your FXML file 'ChatForm.fxml'.";

    }

    public void setController(ClientController controller) {
        this.controller = controller;
    }

    public void appendMessage(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatFild.getItems().add(message);

            }
        });
    }

    private void sendMessage() {
        String message = sendFild.getText().trim();
        if (message.isEmpty()) {
            return;
        }

        appendOwnMessage(message);

        if (clientList.getSelectionModel().getSelectedIndex() < 1) {
            controller.sendMessageToAllUsers(message);
        } else {
            String username = clientList.getSelectionModel().getSelectedItem();
            controller.sendPrivateMessage(username, message);
        }

        sendFild.setText(null);
    }

    private void appendOwnMessage(String message) {
        appendMessage("Я: " + message);
    }

    public void updateUsers(List<String> users) {
        clientList.getItems().clear();
        System.out.println(users);
        for (String user : users) {
            clientList.getItems().add(user);
        }


    }
}
