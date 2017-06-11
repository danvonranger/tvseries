package uk.co.rangersoftware.downloader;

import uk.co.rangersoftware.config.GlobalConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AvailabilityChecker implements AvailabilityCheck {
    private GlobalConfig config;

    public AvailabilityChecker(GlobalConfig config) {
        this.config = config;
    }

    public boolean siteIsAvailable() {
        String domain = extractDomain();
        String pingRequest = String.format("ping -n 1 %s", domain);
        String pingResult = "";

        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(pingRequest);

            BufferedReader in = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                pingResult += inputLine;
            }
            in.close();

        } catch (IOException e) {
            return false;
        }
        return !pingResult.contains("Request timed out");
    }

    private String extractDomain(){
        String url = config.getBaseUrl();
        url = url.replace("https://", "");
        return url;
    }
}
