package uk.co.rangersoftware.util;

import uk.co.rangersoftware.model.DownloadSize;

public interface Sizing {
    DownloadSize downloadSize(String sizeData, int max);
}
