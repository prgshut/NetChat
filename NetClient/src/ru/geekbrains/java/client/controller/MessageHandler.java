package ru.geekbrains.java.client.controller;

@FunctionalInterface
public interface MessageHandler {
    void handle(String message);
}
