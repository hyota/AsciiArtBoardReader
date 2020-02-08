package com.github.hyota.asciiartboardreader.model.common;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CharsetDetectorStream extends InputStream {

    private InputStream in;
    @Nonnull
    private UniversalDetector detector;
    @Setter
    @Nullable
    private CharsetDetectorListener listener;
    @Getter
    @Nullable
    private Charset charset;

    public interface CharsetDetectorListener {
        void onDetectCharset(@Nonnull Charset charset);

        void onNotFoundCharset();
    }

    public CharsetDetectorStream(@Nonnull InputStream in) {
        this.in = in;
        this.detector = new UniversalDetector(charset -> {
            try {
                this.charset = Charset.forName(detector.getDetectedCharset());
                if (listener != null && this.charset != null) {
                    listener.onDetectCharset(this.charset);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public int read() throws IOException {
        int read = in.read();
        if (read > 0 && !detector.isDone()) {
            detector.handleData(new byte[]{(byte) read}, 0, 1);
            if (detector.isDone()) {
                log.debug("detector is done.");
                detector.dataEnd();
                String charset = detector.getDetectedCharset();
                if (charset != null) {
                    try {
                        this.charset = Charset.forName(charset);
                        if (listener != null) {
                            listener.onDetectCharset(this.charset);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        if (read <= 0 && !detector.isDone()) {
            log.debug("detector is not done.");
            detector.dataEnd();
            String charset = detector.getDetectedCharset();
            if (charset != null) {
                log.debug("detect charset. charset = {}", charset);
                try {
                    this.charset = Charset.forName(charset);
                    if (listener != null) {
                        listener.onDetectCharset(this.charset);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            } else {
                log.debug("detector not found charset.");
                if (listener != null) {
                    listener.onNotFoundCharset();
                }
            }
        }
        return read;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = in.read(b, off, len);
        if (read > 0 && !detector.isDone()) {
            log.debug("detector handle data.");
            detector.handleData(b, off, len);
            if (detector.isDone()) {
                log.debug("detector is done.");
                detector.dataEnd();
            }
        }
        if (read <= 0 && listener != null) {
            log.debug("detector not found charset.");
            listener.onNotFoundCharset();
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        super.close();
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public boolean isDone() {
        return detector.isDone();
    }
}
