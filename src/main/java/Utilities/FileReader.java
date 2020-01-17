package Utilities;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static Utilities.PathExpert.*;


public class FileReader {

    public static String readFile(String file, String keyword){
        try {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(file), StandardCharsets.UTF_8));
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int getFieldData(String state, String attribute){
        String line = readFile(fieldAttributesPath,state);
        String[] stringData = line.split(",");
        int[] intData = new int[stringData.length];
        for (int i=0;i<stringData.length;i++){
            intData[i] = Integer.parseInt(stringData[i]);
        }
        String[] choices = {"id","price","rent","houseprice","house1","house2","house3","house4","house5","series"};
        int number = 0;
        for (int i = 0; i < intData.length; i++) {
            if (choices[i].equals(attribute)){
                number = intData[i];
            }
        }
        return number;
    }
}
