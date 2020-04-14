package ru.geekbrains.java.server.client;

import org.apache.log4j.Logger;
import ru.geekbrains.java.client.Command;
import ru.geekbrains.java.client.CommandType;
import ru.geekbrains.java.client.command.AuthCommand;
import ru.geekbrains.java.client.command.BroadcastMessageCommand;
import ru.geekbrains.java.client.command.PrivateMessageCommand;
import ru.geekbrains.java.client.command.RenameCommand;
import ru.geekbrains.java.server.NetworkServer;
import ru.geekbrains.java.server.filter.CensorshipFilter;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler {
    final static Logger admin =Logger.getLogger("admin");
    final static Logger info =Logger.getLogger("info");
    private final NetworkServer networkServer;
    private final Socket clientSocket;
    private CensorshipFilter filter;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String nickname;

    public ClientHandler(NetworkServer networkServer, Socket socket) {
        this.networkServer = networkServer;
        this.clientSocket = socket;
        filter= new CensorshipFilter();
    }

    public void run() {
        filter.creatMatList();
        doHandle(clientSocket);
    }

    private void doHandle(Socket socket) {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            executorService.execute(() -> {
                try {
                    authentication();
                    readMessages();
                } catch (IOException e) {
//                    System.out.println("Соединение с клиентом " + nickname + " было закрыто!");
                    admin.error("Соединение с клиентом " + nickname + " было закрыто!");
                } finally {
                    closeConnection();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            networkServer.unsubscribe(this);
            clientSocket.close();
            info.info("Завершил работу "+nickname);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case END:
                    System.out.println("Received 'END' command");
                    return;
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommand commandData = (PrivateMessageCommand) command.getData();
                    String receiver = commandData.getReceiver();
                    String message = commandData.getMessage();
                    message=filter.matFilter(message);
                    info.info("Пользователь "+nickname+ " отправил сообщение "+receiver);
                    networkServer.sendMessage(receiver, Command.messageCommand(nickname, message));
                    break;
                }
                case RENAME:{
                    RenameCommand comandData = (RenameCommand) command.getData();
                    String oldUserName = comandData.getOldUserName();
                    String newUserName = comandData.getNewUserName();
                    admin.info("Пользователь переименовался был "+oldUserName+" Стал "+newUserName);
                    networkServer.getAuthService().rename(oldUserName,newUserName);
                    break;
                }
                case BROADCAST_MESSAGE: {
                    BroadcastMessageCommand commandData = (BroadcastMessageCommand) command.getData();
                    String message = commandData.getMessage();
                    message=filter.matFilter(message);
                    info.info("Пользователь "+nickname+"отправил общее сообщение");
                    networkServer.broadcastMessage(Command.messageCommand(nickname, message), this);
                    break;
                }
                default:
                    admin.warn("Unknown type of command : " + command.getType());
//                    System.err.println("Unknown type of command : " + command.getType());
            }
        }
    }

    private Command readCommand() throws IOException {
        try {
             return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from client!";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Command.errorCommand(errorMessage));
            return null;
        }
    }

    private void authentication() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                boolean successfulAuth = processAuthCommand(command);
                if (successfulAuth){
                    return;
                }
            } else {
                System.err.println("Unknown type of command for auth process: " + command.getType());
            }
        }
    }

    private boolean processAuthCommand(Command command) throws IOException {
        AuthCommand commandData = (AuthCommand) command.getData();
        String login = commandData.getLogin();
        String password = commandData.getPassword();
        String username = networkServer.getAuthService().getUsernameByLoginAndPassword(login, password);
        if (username == null) {
            Command authErrorCommand = Command.authErrorCommand("Отсутствует учетная запись по данному логину и паролю!");
            sendMessage(authErrorCommand);
            return false;
        }
        else if (networkServer.isNicknameBusy(username)) {
            Command authErrorCommand = Command.authErrorCommand("Данный пользователь уже авторизован!");
            sendMessage(authErrorCommand);
            return false;
        }
        else {
            nickname = username;
            String message = nickname + " зашел в чат!";
            networkServer.broadcastMessage(Command.messageCommand(null, message), this);
            commandData.setUsername(nickname);
            sendMessage(command);
            networkServer.subscribe(this);
            return true;
        }
    }

    public void sendMessage(Command command) throws IOException {
        out.writeObject(command);
    }

    public String getNickname() {
        return nickname;
    }
}
