package ru.geekbrains.java.client;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.geekbrains.java.client.controller.ClientController;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class NetworkClient extends Application {
    private final String DEFAULT_ADDR ="localhost";
    private final int DEFAULT_PORT = 8289;

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            ClientController clientController = new ClientController(DEFAULT_ADDR, DEFAULT_PORT, primaryStage);
            clientController.runApplication();
        } catch (IOException e) {
            System.err.println("Failed to connect to server! Please, check you network settings");
        }
    }
}
