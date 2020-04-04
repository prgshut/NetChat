package ru.geekbrains.java.client.view.chat;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.geekbrains.java.client.controller.ClientController;
import ru.geekbrains.java.client.view.rename.RenameControl;

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
    private Button renameButton;

    @FXML
    private Button sendButton;
    private ClientController controller;
    private Parent rootRename;
    @FXML
    void send(ActionEvent event) {
        sendMessage();
    }

    @FXML
    void renameOn(ActionEvent event) {
        Stage renameStage = new Stage();
        FXMLLoader loaderRename = new FXMLLoader();

        try {
            rootRename = loaderRename.load(getClass().getResourceAsStream("../rename/RenameForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        RenameControl renameModel = loaderRename.getController();
        renameModel.setController(this.controller);
        Scene scene = new Scene(rootRename, 300, 200);
        renameStage.setTitle("Переименовка");
        renameStage.setScene(scene);
        renameStage.initModality(Modality.WINDOW_MODAL);
        renameStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        renameStage.setIconified(false);
        renameStage.show();

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
    public List<String> saveHistori(){

        return  chatFild.getItems();
    }
    public void appendHistori(List<String> histori){
        if (histori!=null) {
            chatFild.getItems().addAll(histori);
        }
    }
    public void updateUsers(List<String> users) {
        clientList.getItems().clear();
        System.out.println(users);
        for (String user : users) {
            clientList.getItems().add(user);
        }


    }
}
