package ru.geekbrains.java.server.auth;

import ru.geekbrains.java.base.ConectBase;

import java.sql.ResultSet;
import java.sql.SQLException;


public class BaseAuthService implements AuthService {
    private static ConectBase con;
    private static ResultSet res;

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        final String str =String.format("SELECT user.name FROM test_base.user where user.login='%s' and user.pass='%s';", login, password);
        res=con.select(str);

            try {
                  while (res.next()){
                      System.out.println(res.getString("name"));
                      return res.getString("name");

                  }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        return null;
    }
    @Override
    public boolean rename(String oldUserName, String newUserName){
        final String str = String.format("update test_base.user set user.name = '%s' where name = '%s'", newUserName,oldUserName);
        int res = con.update(str);
        if(res ==1){
            return true;
        }
        return false;
    }

    @Override
    public void start() {
         con = new ConectBase();
        try {
            con.conect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации оставлен");
        con.disconect();
    }
}
