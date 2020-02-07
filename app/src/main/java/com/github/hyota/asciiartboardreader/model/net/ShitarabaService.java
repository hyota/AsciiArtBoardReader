package com.github.hyota.asciiartboardreader.model.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ShitarabaService {

    @GET("/bbs/api/setting.cgi/{category}/{address}/")
    Call<String> getSettings(@Path("category") String category, @Path("address") long address);

}
