package uk.co.rangersoftware.util;

import uk.co.rangersoftware.model.DownloadSize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadSizer implements Sizing {
    public DownloadSize downloadSize(String sizeData, int max) {
        DownloadSize downloadSize = new DownloadSize();

        String regEx = "(Size\\s\\d+)";
        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sizeData);
        if(!matcher.find()) {
            downloadSize.setSizeIsValid(true);
            return downloadSize;
        }

        boolean isInGigaBytes = sizeData.contains("GiB");
        String data = sizeData.substring(sizeData.indexOf("Size"), sizeData.lastIndexOf(","));
        data = data.replace("Size ", "");
        String size = "";
        for(char c : data.toCharArray()){
            if(c == '.') {
                size += c;
                continue;
            }
            if(!Character.isDigit(c))break;
            size += c;
        }
        if(!size.contains(".")){
            size += ".00";
        }
        String firstPart = size.split("\\.")[0];
        String decimalPart = size.split("\\.")[1];
        int multiplyBy = 1;
        if(isInGigaBytes){
            switch (decimalPart.length()){
                case 1:
                    multiplyBy = 100;
                    break;
                case 2:
                    multiplyBy = 10;
                    break;
                default:
                    multiplyBy = 1;
                    break;
            }
            size = size.replace(".", "");
        }else{
            size = firstPart;
        }

        int sizeAsInt = Integer.parseInt(size);
        sizeAsInt = sizeAsInt * multiplyBy;
        downloadSize.setSizeInMB(sizeAsInt);
        downloadSize.setSizeIsValid(sizeAsInt <= max);
        if(!downloadSize.isValid()){
            downloadSize.setFailureMessage(String.format("File size %s MB is too large", sizeAsInt, max));
        }
        return downloadSize;
    }
}
