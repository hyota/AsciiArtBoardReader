package com.github.hyota.asciiartboardreader.model.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CharsetDetectorReader extends Reader {

    private CharsetDetectorStream charsetDetectorStream;
    private SequenceInputStream sequenceInputStream;
    private InputStreamReader inputStreamReader;
    private BufferedReader br;

    public CharsetDetectorReader(@Nonnull InputStream in, @Nonnull Charset defaultCharset) throws IOException {
        charsetDetectorStream = new CharsetDetectorStream(in);
        List<Byte> byteList = new ArrayList<>();
        int read;
        while ((read = charsetDetectorStream.read()) > 0 && !charsetDetectorStream.isDone()) {
            byteList.add((byte) read);
        }
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            bytes[i] = byteList.get(i);
        }
        sequenceInputStream = new SequenceInputStream(new ByteArrayInputStream(bytes), charsetDetectorStream);
        Charset charset = charsetDetectorStream.getCharset();
        log.debug("detect charset = {}", charset);
        if (charset == null) {
            charset = defaultCharset;
            log.debug("use default charset = {}", charset);
        }
        inputStreamReader = new InputStreamReader(sequenceInputStream, charset);
        br = new BufferedReader(inputStreamReader);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return br.read();
    }

    @Nullable
    public String readLine() throws IOException {
        return br.readLine();
    }

    @Override
    public void close() throws IOException {
        IOException exception = null;
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            exception = e;
        }
        try {
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            exception = exception != null ? exception : e;
        }
        try {
            if (sequenceInputStream != null) {
                sequenceInputStream.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            exception = exception != null ? exception : e;
        }
        try {
            if (charsetDetectorStream != null) {
                charsetDetectorStream.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        if (exception != null) {
            throw exception;
        }
    }

}
