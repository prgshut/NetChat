package ru.geekbrains.java.base;

import java.sql.SQLException;

public class CreationBase {
    public static void main(String[] args) {
        ConectBase con = new ConectBase();
        String creatTabel = "CREATE TABLE `test_base`.`user` ( " +
                "`name` VARCHAR(10) NOT NULL," +
                "  `login` VARCHAR(10) NOT NULL," +
                "  `pass` VARCHAR(10) NOT NULL," +
                "  PRIMARY KEY (`name`));";
        String insert= "INSERT INTO `test_base`.`user` (`name`, `login`, `pass`) " +
                "VALUES ('lo', 'lo', 'pa');";


        try {
            con.conect();
//            con.update(creatTabel);

//            for (int i = 0; i < 100; i++) {
//                String name ="User"+i;
//                String login ="log"+i;
//                String pas ="pas"+1;
//                con.update("INSERT INTO `test_base`.`user` (`name`, `login`, `pass`) " +
//                        "VALUES ('"+name+"', '"+login+"', '"+pas+"');");
//
//            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            con.disconect();
        }

    }
}
