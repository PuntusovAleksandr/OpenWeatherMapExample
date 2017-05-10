package com.aleksandrp.openweathermapexample.api.interfaces;

import com.aleksandrp.openweathermapexample.rx.models.WeatherModel;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public interface ServiceWeather {

    @GET("weather")
    Observable<Response<WeatherModel>> getWeather(
            @Query("q") String name,
            @Query("appid") String token,
            @Query("lang") String lang,
            @Query("units") String units);
}
