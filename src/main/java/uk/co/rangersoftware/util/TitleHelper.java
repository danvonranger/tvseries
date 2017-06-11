package uk.co.rangersoftware.util;

import java.util.HashMap;
import java.util.Map;

public class TitleHelper {
    private Map<String, String> replacementValues;

    public TitleHelper(){
        replacementValues = new HashMap<String, String>();
        replacementValues.put("-", " ");
        replacementValues.put("\\+", " ");
        replacementValues.put(";", " ");
        replacementValues.put(",", " ");
        replacementValues.put("\\.", " ");
        replacementValues.put("\\)", " ");
        replacementValues.put("\\(", " ");
        replacementValues.put("\\[", " ");
        replacementValues.put("\\]", " ");
        replacementValues.put("_", " ");
        replacementValues.put("'", "");
    }

    public String prepareTitle(String title){
        return replaceValues(title);
    }

    private String replaceValues(String candidate){
        String output = candidate;
        for (Map.Entry<String, String> entry : replacementValues.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            output = output.replaceAll(key, value);
        }
        return removeAllDoubleSpaces(output);
    }

    private String removeAllDoubleSpaces(String input){
        String output = input;
        while(output.contains("  ")) {
            output = output.replaceAll("  ", " ");
        }
        return output.trim();
    }
}
