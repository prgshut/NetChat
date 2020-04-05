package ru.geekbrains.java.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.geekbrains.java.client.Command;

import ru.geekbrains.java.client.io.HistoriChat;
import ru.geekbrains.java.client.model.NetworkService;
import ru.geekbrains.java.client.view.auth.AuthControl;
import ru.geekbrains.java.client.view.chat.ChatControl;


import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static ru.geekbrains.java.client.Command.*;

public class ClientController {

    private final NetworkService networkService;
    private Stage primaryStage;
    private Stage chatStage;
    private Parent rootChat;
    private String nickname;
    private ChatControl clientChat;
    private HistoriChat historiChat;

    public ClientController(String serverHost, int serverPort, Stage primaryStage) {
        this.networkService = new NetworkService(serverHost, serverPort);
        this.primaryStage = primaryStage;
        this.chatStage = new Stage();
        historiChat = new HistoriChat();
    }

    public TimerTask tt = new TimerTask() {
        @Override
        public void run() {

            System.exit(0);
        }
    };
    public Timer timer = new Timer();

    public void runApplication() throws IOException {
//        timer.schedule(tt, 12000L);
        openAuth();
        openChat();
        connectToServer();
        runAuthProcess();
    }

    private void openAuth() throws IOException {

        FXMLLoader loaderAuth = new FXMLLoader();
        rootChat = loaderAuth.load(getClass().getResourceAsStream("../view/auth/AuthForm.fxml"));
        AuthControl authDialog = loaderAuth.getController();
        authDialog.setController(this);
        Scene scene = new Scene(rootChat, 500, 230);
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(scene);
        primaryStage.setIconified(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
    }

    private void runAuthProcess() {
        networkService.setSuccessfulAuthEvent(nickname -> {
            setUserName(nickname);
//            timer.cancel();

            Platform.runLater(() -> {
                runchat();
//                openChat();
            });
        });

    }

    private void runchat() {
        chatStage.setTitle("Супер чат :" + nickname);
        chatStage.show();
        clientChat.appendHistori(historiChat.readHistori(nickname));
        primaryStage.close();
    }

    private void openChat() {
        System.out.println("2 " + Thread.currentThread().getName());
        FXMLLoader loaderChat = new FXMLLoader();
        try {
            rootChat = loaderChat.load(getClass().getResourceAsStream("../view/chat/ChatForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientChat = loaderChat.getController();
        clientChat.setController(this);
        Scene scene = new Scene(rootChat, 600, 400);
        System.out.println(nickname);
//        chatStage.setTitle("Супер чат :" + nickname);
        chatStage.setScene(scene);
        chatStage.setIconified(false);
//        chatStage.show();
        chatStage.setOnCloseRequest(e -> {
            historiChat.writeHistori(clientChat.saveHistori(),nickname);
            System.exit(0);
        });

        networkService.setMessageHandler(new MessageHandler() {
            @Override
            public void handle(String message) {
                clientChat.appendMessage(message);

            }
        });
    }

    private void setUserName(String nickname) {
        this.nickname = nickname;
    }

    private void connectToServer() throws IOException {
        try {
            networkService.connect(this);
        } catch (IOException e) {
            System.err.println("Failed to establish server connection");
            throw e;
        }
    }

    public void sendAuthMessage(String login, String pass) throws IOException {
        networkService.sendCommand(authCommand(login, pass));
    }

    public void sendMessageToAllUsers(String message) {
        try {
            networkService.sendCommand(broadcastMessageCommand(message));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void renameUserName( String newUserName){
        try {
            networkService.sendCommand(renameCommand(this.nickname,newUserName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        networkService.close();
    }

    public String getUsername() {
        return nickname;
    }

    public void sendPrivateMessage(String username, String message) {
        try {
            networkService.sendCommand(privateMessageCommand(username, message));
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }

    public void showErrorMessage(String errorMessage) {
        System.err.println(errorMessage);
    }

    public void updateUsersList(List<String> users) {
        users.remove(nickname);
        users.add(0, "All");
        clientChat.updateUsers(users);
    }
}
