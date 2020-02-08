package com.github.hyota.asciiartboardreader.model.common;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CharsetDetectorStream extends BufferedInputStream {

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
        super(in);
        this.detector = new UniversalDetector(charset -> {
            try {
                this.charset = Charset.forName(detector.getDetectedCharset());
                if (listener != null) {
                    listener.onDetectCharset(this.charset);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int read = super.read(b, off, len);
        if (read > 0 && !detector.isDone()) {
            detector.handleData(b, off, len);
            if (detector.isDone()) {
                detector.dataEnd();
            }
        }
        if (read <= 0 && listener != null) {
            listener.onNotFoundCharset();
        }
        return read;
    }

    public boolean isDone() {
        return detector.isDone();
    }
}
