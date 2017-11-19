package uk.co.rangersoftware.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpGetExample {
    public static StringBuilder get(String url) throws IOException {
        int timeoutInMilleSeconds = 10000;
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder = requestBuilder.setConnectTimeout(timeoutInMilleSeconds);
        requestBuilder = requestBuilder.setConnectionRequestTimeout(timeoutInMilleSeconds);

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(requestBuilder.build());
        HttpClient client = builder.build();



        //HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        StringBuilder sb = new StringBuilder();

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        InputStream stream = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line;
        boolean firstLine = true;
        while (true) {
            line = reader.readLine();
            if(line == null) {
                break;
            }
            if(line.length() == 0) continue;
            if(firstLine){
                firstLine = false;
                if(!line.startsWith("<!DOCTYPE html>")){
                    sb = new StringBuilder();
                    break;
                }
            }
            sb.append(line + System.getProperty("line.separator"));
        }
        return sb;
    }
}
