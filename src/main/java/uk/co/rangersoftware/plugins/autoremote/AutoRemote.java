package uk.co.rangersoftware.plugins.autoremote;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class AutoRemote {
    public static String notifcationUrl(List<String> episodes) {
        String baseUrl = "https://autoremotejoaomgcd.appspot.com/sendmessage?key=APA91bF_YHHB73bktCUBs1ASxo3h_g1Tt9lYV8NFEitj4-Pw-yYfk20HbnCeAhUouXOtCNwdWbrdOSRkuZtPlaHXyF0fZ5464jfSCfoM9fkwp2O9vDyLhbFA4dv7FqPxC8fsZIN6W8hr&message=";
        String prefix = "tvpb=:=";
        String summary = "";
        String message = "";
        if(episodes.size() == 0){
            message = "No new episodes.";
        }else {
            summary = "" + episodes.size();
            summary += " Download";
            if (episodes.size() > 1) {
                summary += "s";
            }
            summary += "\n\n";
            for (String download : episodes) {
                message += download;
                message += "\n";
            }
        }

        String url = baseUrl;
        String qs = prefix + summary + message;
        try {
            qs = URLEncoder.encode(qs, "UTF-8");
            url += qs;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
