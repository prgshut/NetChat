package ru.geekbrains.java.client.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HistoriChat {
    private static String path = "C:\\Temp";
    private static List<String> chatHistori;
    private static final int LAST_MESSAG_COUN = 100;

    public List<String> readHistori(String nikname) {
        chatHistori= new ArrayList<>();

        final String fileName = String.format("%s\\%s.txt", path, nikname);
        ArrayList<String> temp = new ArrayList<>();
        String s;
        if (new File(fileName).exists()) {
            try (BufferedReader inRead = new BufferedReader(new FileReader(fileName))) {
                while ((s = inRead.readLine()) != null) {
                    temp.add(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (temp.size() > LAST_MESSAG_COUN) {
                chatHistori.addAll((temp.size() - LAST_MESSAG_COUN), temp);
            } else {
                chatHistori.addAll(temp);
            }
            return chatHistori;
        }
        return null;
    }

    public void writeHistori(List<String> histori, String nikname) {
        final String fileName = String.format("%s\\%s.txt", path, nikname);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String s : histori) {
                writer.write(s+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
