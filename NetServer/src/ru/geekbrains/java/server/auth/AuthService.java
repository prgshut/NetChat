package ru.geekbrains.java.server.auth;

public interface AuthService {

    String getUsernameByLoginAndPassword(String login, String password);

    void start();
    void stop();

}
