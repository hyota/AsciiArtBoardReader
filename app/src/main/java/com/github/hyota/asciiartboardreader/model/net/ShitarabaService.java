package com.github.hyota.asciiartboardreader.model.net;

import com.github.hyota.asciiartboardreader.model.entity.Setting;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ShitarabaService {

    @GET("/bbs/api/setting.cgi/{category}/{address}/")
    Call<Setting> getSetting(@Path("category") String category, @Path("address") long address);

}
