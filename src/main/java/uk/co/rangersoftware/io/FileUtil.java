package uk.co.rangersoftware.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static String readContent(File file){
        String rawData = null;

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuffer fileContents = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                fileContents.append(line);
                line = br.readLine();
            }

            br.close();
            rawData = fileContents.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rawData;
    }

    public static List<String> readContentIntoList(File file){
        List<String> list = new ArrayList<String>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuffer fileContents = new StringBuffer();
            String line = br.readLine();
            while (line != null) {
                if(!list.contains(line)) {
                    list.add(line);
                }
                line = br.readLine();
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void appendToFile(File file, String data){
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            bw.append(data + System.getProperty("line.separator"));

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
