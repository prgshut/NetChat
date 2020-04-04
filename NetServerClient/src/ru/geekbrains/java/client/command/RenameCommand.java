package ru.geekbrains.java.client.command;

import java.io.Serializable;

public class RenameCommand implements Serializable {
    private final String oldUserName;
    private final String newUserName;

    public RenameCommand(String oldUserName, String newUserName) {
        this.oldUserName = oldUserName;
        this.newUserName = newUserName;
    }

    public String getOldUserName() {
        return oldUserName;
    }

    public String getNewUserName() {
        return newUserName;
    }
}
