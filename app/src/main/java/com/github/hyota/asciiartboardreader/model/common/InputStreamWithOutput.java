package com.github.hyota.asciiartboardreader.model.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InputStreamWithOutput extends InputStream {

    @Nonnull
    private InputStream in;
    @Nonnull
    private BufferedOutputStream bos;
    @Nonnull
    private FileOutputStream fos;

    public InputStreamWithOutput(@Nonnull InputStream in, @Nonnull File outputFile) {
        log.debug("constructor in {}, file {}", in, outputFile);
        try {
            this.in = in;
            fos = new FileOutputStream(outputFile);
            bos = new BufferedOutputStream(fos);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public int read() throws IOException {
        int read = in.read();
        bos.write(read);
        return read;
    }

    @Override
    public void close() throws IOException {
        try {
            bos.close();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        try {
            fos.close();
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        super.close();
    }

}
