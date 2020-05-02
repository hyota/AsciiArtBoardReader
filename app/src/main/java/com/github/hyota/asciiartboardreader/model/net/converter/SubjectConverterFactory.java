package com.github.hyota.asciiartboardreader.model.net.converter;

import com.github.hyota.asciiartboardreader.model.entity.Setting;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.entity.ThreadInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

@Slf4j
public class SubjectConverterFactory extends Converter.Factory {

    @Nonnull
    private final Converter<ResponseBody, Subject> subjectResponseConverter;

    public interface InputStreamWrapperBuilder {
        InputStream build(@Nonnull InputStream is);
    }

    public SubjectConverterFactory() {
        this.subjectResponseConverter = new SubjectResponseConverter(Charset.forName("SHIFT_JIS"), null);
    }

    public SubjectConverterFactory(@Nonnull Charset charset) {
        this.subjectResponseConverter = new SubjectResponseConverter(charset, null);
    }

    public SubjectConverterFactory(@Nullable InputStreamWrapperBuilder inputStreamWrapperBuilder) {
        this.subjectResponseConverter = new SubjectResponseConverter(Charset.forName("SHIFT_JIS"), inputStreamWrapperBuilder);
    }

    public SubjectConverterFactory(@Nonnull Charset charset, @Nullable InputStreamWrapperBuilder inputStreamWrapperBuilder) {
        this.subjectResponseConverter = new SubjectResponseConverter(charset, inputStreamWrapperBuilder);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        if (Setting.class.equals(type)) {
            return subjectResponseConverter;
        }
        return null;
    }

    public static class SubjectResponseConverter implements Converter<ResponseBody, Subject> {

        @Nonnull
        private SubjectSteamConverter converter;

        public SubjectResponseConverter(@Nonnull Charset charset, @Nullable InputStreamWrapperBuilder inputStreamWrapperBuilder) {
            this.converter = new SubjectSteamConverter(charset, inputStreamWrapperBuilder);
        }

        @Nullable
        @Override
        public Subject convert(@Nonnull ResponseBody value) {
            log.debug("convert start");
            return converter.convert(value.byteStream());
        }
    }

    public static class SubjectSteamConverter {
        private static Pattern pattern = Pattern.compile("^(\\d+)\\.(?:(?:dat<>)|(?:cgi,))(.+)\\((\\d+)\\)$");

        @Nonnull
        private final Charset charset;
        @Nullable
        private final InputStreamWrapperBuilder inputStreamWrapperBuilder;

        public SubjectSteamConverter(@Nonnull Charset charset) {
            this.charset = charset;
            this.inputStreamWrapperBuilder = null;
        }

        public SubjectSteamConverter(@Nonnull Charset charset, @Nullable InputStreamWrapperBuilder inputStreamWrapperBuilder) {
            this.charset = charset;
            this.inputStreamWrapperBuilder = inputStreamWrapperBuilder;
        }

        @Nullable
        public Subject convert(@Nonnull InputStream in) {
            log.debug("convert.");
            InputStream wrapper = null;
            if (inputStreamWrapperBuilder != null) {
                wrapper = inputStreamWrapperBuilder.build(in);
            }
            try (InputStreamReader isr = new InputStreamReader(wrapper != null ? wrapper : in, charset);
                 BufferedReader reader = new BufferedReader(isr)) {
                List<ThreadInfo> threadInfoList = new ArrayList<>();
                String line;
                long id = 0;
                while ((line = reader.readLine()) != null) {
                    Matcher m = pattern.matcher(line);
                    if (!m.find()) {
                        log.warn("not pattern match line. {}", line);
                        continue;
                    }
                    threadInfoList.add(new ThreadInfo(++id, Long.parseLong(m.group(1)), m.group(2), Long.parseLong(m.group(3))));
                }
                return new Subject(threadInfoList);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                if (wrapper != null) {
                    try {
                        wrapper.close();
                    } catch (IOException e) {
                        log.warn(e.getMessage(), e);
                    }
                }
            }
            return null;
        }
    }

}
