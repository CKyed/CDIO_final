package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class TextController {

    public static String readFile(String file, String keyword){
        try {
            String filePath = "src/main/java/model/" + file;

            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(":", 2);
                if (parts.length >= 2)
                {
                    String key = parts[0];
                    String value = parts[1];
                    if(keyword.equals(key)){
                        return value;
                    }
                }
                else {
                    System.out.println("ignoring line: " + line);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
