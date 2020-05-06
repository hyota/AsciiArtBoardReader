package com.github.hyota.asciiartboardreader.model.net;

import com.github.hyota.asciiartboardreader.model.entity.Setting;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ShitarabaService {

    @GET("/bbs/api/setting.cgi/{category}/{address}/")
    Call<Setting> getSetting(@Path("category") String category, @Path("address") long address);

    @GET("/{category}/{address}/subject.txt")
    @Streaming
    Call<ResponseBody> getSubject(@Path("category") String category, @Path("address") long address);

}
