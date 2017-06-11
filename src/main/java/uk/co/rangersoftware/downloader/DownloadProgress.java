package uk.co.rangersoftware.downloader;

import java.util.ArrayList;
import java.util.List;

public class DownloadProgress {
    List<String> downloadDetails;
    List<String> additionalDetails;

    public DownloadProgress(){
        additionalDetails = new ArrayList<String>();
        downloadDetails = new ArrayList<String>();
    }

    public void addInformationMessage(String details){
        if(additionalDetails.contains(details))return;
        additionalDetails.add(details);
    }

    public void addDownload(String details){
        if(downloadDetails.contains(details))return;
        downloadDetails.add(details);
    }

    public List<String> getAdditionalDetails() {
        return additionalDetails;
    }

    public List<String> getDownloadDetails() {
        return downloadDetails;
    }
}
