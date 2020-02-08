package com.github.hyota.asciiartboardreader.model.net;

import com.github.hyota.asciiartboardreader.model.common.CharsetDetectorStream;
import com.github.hyota.asciiartboardreader.model.entity.Setting;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

@Slf4j
public class SettingConverterFactory extends Converter.Factory {
    @Nonnull
    private Converter<ResponseBody, Setting> settingResponseConverter;

    @Inject
    public SettingConverterFactory(@Nonnull SettingResponseConverter settingResponseConverter) {
        this.settingResponseConverter = settingResponseConverter;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (Setting.class.equals(type)) {
            return settingResponseConverter;
        }
        return null;
    }

    public static class SettingResponseConverter implements Converter<ResponseBody, Setting> {

        private static final String SEPARATOR = "=";

        @Inject
        public SettingResponseConverter() {
        }

        @Nullable
        @Override
        public Setting convert(@Nonnull ResponseBody value) {
            CharsetDetectorStream stream = new CharsetDetectorStream(value.byteStream());
            int read;
            List<Byte> byteList = new ArrayList<>();
            try {
                while ((read = stream.read()) > 0 && !stream.isDone()) {
                    byteList.add((byte) read);
                }
                Charset charset;
                if (stream.isDone()) {
                    charset = stream.getCharset();
                } else {
                    charset = Charset.forName("SHIFT_JIS");
                }
                byte[] bytes = new byte[byteList.size()];
                for (int i = 0; i < byteList.size(); i++) {
                    bytes[i] = byteList.get(i);
                }
                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                     SequenceInputStream sequenceInputStream = new SequenceInputStream(byteArrayInputStream, stream);
                     Reader isr = new InputStreamReader(sequenceInputStream, charset);
                     BufferedReader br = new BufferedReader(isr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (!line.contains("=")) {
                            continue;
                        }
                        String[] items = line.split(SEPARATOR, 2);
                        if ("BBS_TITLE".equals(items[0])) {
                            return new Setting(items[1]);
                        }
                    }

                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }

}
