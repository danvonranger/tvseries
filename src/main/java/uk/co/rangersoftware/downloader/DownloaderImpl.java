package uk.co.rangersoftware.downloader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import uk.co.rangersoftware.config.GlobalConfig;
import uk.co.rangersoftware.log.Log;
import uk.co.rangersoftware.util.Constants;
import uk.co.rangersoftware.util.DurationTimer;

import java.io.*;

public class DownloaderImpl implements Downloader {
    private final Log logger;
    private final String DOWNLOAD_PREFX = "Download attempt took:";
    private final GlobalConfig config;

    public DownloaderImpl(Log logger, GlobalConfig globalConfig) {
        config = globalConfig;
        this.logger = logger;
    }

    public DownloadResult download(String urlStr) throws FileNotFoundException {
        DownloadResult result = null;
        for (String userAgent : config.userAgents()) {
            int attempts = 0;
            while(true) {
                attempts++;
                if(attempts > Constants.maxRetrieveAttemptsPerUserAgent) break;
                DurationTimer timer = new DurationTimer();
                timer.start();
                result = downloadAttempt(urlStr, userAgent);
                if (!result.isInError()) {
                    logger.debug(String.format("%s %s - Have data.", DOWNLOAD_PREFX, timer.stop()));
                    break;
                }
                logger.debug(String.format("%s %s - No data.", DOWNLOAD_PREFX, timer.stop()));
                sleep();
            }
            if (!result.isInError()) {
                break;
            }
        }
        return result;
    }

    private void sleep(){
        try {
            Thread.sleep(500);
        } catch (Exception ex) {

        }
    }

    private DownloadResult downloadAttempt(String urlStr, String useragent) {
        File file = new File(Constants.DOWNLOAD_DATA_FILE_NAME);
        file.delete();
        DownloadResult result = new DownloadResult();
        try {
            InputStream is = getConnectionStream(urlStr, useragent);

            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(is, "UTF-8");
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
               out.append(buffer, 0, rsz);
            }
            result.setRawData(out.toString());
            is.close();
            result.setInError(result.rawData().length() == 0);
        } catch (Exception ex) {
            result.setInError(true);
            result.setErrorMessage(ex.getMessage());
        }

        return result;
    }

    public InputStream getConnectionStream(String fullUrl, String agent) throws IOException{
        int CONNECTION_TIMEOUT_MS = Constants.connectionTimeoutInSeconds * 1000;

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT_MS);
        HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT_MS);

        HttpClient client = new DefaultHttpClient(httpParams);
        HttpGet request = new HttpGet(fullUrl);
        request.addHeader("User-Agent", agent);
        HttpResponse response = client.execute(request);
        InputStream is = response.getEntity().getContent();

        return is;
    }
}
