package com.github.hyota.asciiartboardreader.model.repository;

import com.github.hyota.asciiartboardreader.di.annotation.Local;
import com.github.hyota.asciiartboardreader.model.common.InputStreamWithOutput;
import com.github.hyota.asciiartboardreader.model.entity.Bbs;
import com.github.hyota.asciiartboardreader.model.entity.ShitarabaBbs;
import com.github.hyota.asciiartboardreader.model.entity.Subject;
import com.github.hyota.asciiartboardreader.model.net.ShitarabaService;
import com.github.hyota.asciiartboardreader.model.net.converter.SubjectConverterFactory;

import java.io.File;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

@Slf4j
public class SubjectShitarabaRepositoryImpl implements SubjectRepository {

    @Nonnull
    private Retrofit retrofit;
    @Nonnull
    private File storage;

    @Inject
    public SubjectShitarabaRepositoryImpl(@Nonnull Retrofit retrofit, @Local @Nonnull File storage) {
        this.retrofit = retrofit;
        this.storage = storage;
    }

    @Override
    public void load(@Nonnull Bbs bbs, @Nonnull Callback callback) {
        if (!(bbs instanceof ShitarabaBbs)) {
            log.warn("bbs {} is not ShitarabaBbs.", bbs.getClass());
            callback.onFail();
            return;
        }

        ShitarabaBbs shitarabaBbs = (ShitarabaBbs) bbs;
        Retrofit retrofit = this.retrofit.newBuilder()
//                .addConverterFactory(new SubjectConverterFactory(bbs.getCharset(), is -> new InputStreamWithOutput(is, getSubjectFile(storage, bbs))))
                .build();
        SubjectConverterFactory.SubjectSteamConverter converter = new SubjectConverterFactory.SubjectSteamConverter(bbs.getCharset(), is -> new InputStreamWithOutput(is, getSubjectFile(storage, bbs)));
        ShitarabaService api = retrofit.create(ShitarabaService.class);
        log.debug("load start.");
        api.getSubject(shitarabaBbs.getCategory(), shitarabaBbs.getAddress())
                .enqueue(new retrofit2.Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@Nonnull retrofit2.Call<ResponseBody> call, @Nonnull retrofit2.Response<ResponseBody> response) {
                        log.debug("onResponse.");
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Subject subject = converter.convert(response.body().byteStream());
                                if (subject != null) {
                                    callback.onSuccess(subject);
                                    return;
                                }
                            }
                        } else {
                            callback.onNotFound();
                        }
                        callback.onFail();
                    }

                    @Override
                    public void onFailure(@Nonnull retrofit2.Call<ResponseBody> call, @Nonnull Throwable t) {
                        log.info(t.getMessage(), t);
                        callback.onFail();
                    }
                });
    }
}
