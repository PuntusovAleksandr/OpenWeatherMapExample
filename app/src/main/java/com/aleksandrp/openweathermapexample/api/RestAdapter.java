package com.aleksandrp.openweathermapexample.api;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class RestAdapter {


    public static final String API_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String API_ICON_URL = "http://openweathermap.org/img/w/";
    public static final String authToken = "9f17396ab86e28540f75a5b244394d7e";

    private Retrofit retrofit;

    public RestAdapter() {
    }

    public void init(final boolean token, String method) {
        Log.d("INIT", "method " + method);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)      // for log request
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                                Request request;
                                if (!token) {
                                    request = chain.request().newBuilder()
                                            .build();
                                } else {
                                    request = chain.request().newBuilder()
                                            .addHeader("Authorization", "bearer " + authToken)
                                            .build();
                                }


                                return chain.proceed(request);
                            }
                        })
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        HttpUrl httpUrl = HttpUrl.parse(API_BASE_URL);

        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(defaultHttpClient)
                .build();

    }


    public Retrofit getRetrofit() {
        return retrofit;
    }

}
