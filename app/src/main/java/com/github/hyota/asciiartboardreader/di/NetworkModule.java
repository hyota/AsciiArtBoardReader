package com.github.hyota.asciiartboardreader.di;

import android.content.Context;

import com.github.hyota.asciiartboardreader.model.net.converter.SettingConverterFactory;
import com.readystatesoftware.chuck.ChuckInterceptor;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

@Module
public class NetworkModule {

    @Provides
    OkHttpClient provideOkHttpClient(Context context) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new ChuckInterceptor(context))
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient client, SettingConverterFactory settingConverterFactory) {
        return new Retrofit.Builder()
                .addConverterFactory(settingConverterFactory)
                .baseUrl("http://jbbs.shitaraba.net")
                .client(client)
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .build();
    }

}
