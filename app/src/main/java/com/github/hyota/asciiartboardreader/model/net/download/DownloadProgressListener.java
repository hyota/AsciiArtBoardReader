package com.github.hyota.asciiartboardreader.model.net.download;

@FunctionalInterface
public interface DownloadProgressListener {

    void update(long bytesRead, long contentLength, boolean done);

}