package uk.co.rangersoftware.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static uk.co.rangersoftware.util.Constants.ROOT_FOLDER;
import static uk.co.rangersoftware.util.Constants.TV_SHOWS_FILE_NAME;
import static uk.co.rangersoftware.util.Constants.TV_SHOW_EXT;

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
        try {
            String fileName = file.getName();
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
