package ru.geekbrains.java.server.filter;

import ru.geekbrains.java.base.ConectBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

public class CensorshipFilter {
    private static ConectBase con;
    private static Set<String> matList;

    public void creatMatList() {
        matList = new LinkedHashSet<String>();
        con = new ConectBase();
        try {
            final String str = "select * from test_base.curse_words;";
            ResultSet rez = con.select(str);
            while (rez.next()) {
                matList.add(rez.getString("word"));
            }
            System.out.println("Создали лист");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public String matFilter(String message){
        System.out.println("Проверяем на мат");
        final String[] str = message.split(" ");
        String outMessag = message;
        for (int i = 0; i < str.length; i++) {
            if (matList.contains(str[i])){
                StringBuffer stars = new StringBuffer();
                for (int j = 0; j < str[i].length(); j++) {
                    stars.append("*");
                }
              outMessag= outMessag.replaceAll("(?i)"+str[i],stars.toString());
            }

        }
        return outMessag;
    }




}
