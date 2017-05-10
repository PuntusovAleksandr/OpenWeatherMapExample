package com.aleksandrp.openweathermapexample.api.helpers;

import com.aleksandrp.openweathermapexample.App;
import com.aleksandrp.openweathermapexample.R;
import com.aleksandrp.openweathermapexample.api.RestAdapter;
import com.aleksandrp.openweathermapexample.api.constants.ApiConstants;
import com.aleksandrp.openweathermapexample.api.interfaces.ServiceWeather;
import com.aleksandrp.openweathermapexample.rx.BusProvider;
import com.aleksandrp.openweathermapexample.rx.events.NetworkResponseEvent;
import com.aleksandrp.openweathermapexample.rx.models.WeatherModel;
import com.aleksandrp.openweathermapexample.utils.SettingsApp;

import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.aleksandrp.openweathermapexample.api.RestAdapter.authToken;


/**
 * Created by AleksandrP on 10.05.2017.
 */

public class ApiHelper {

    protected RestAdapter restAdapter;

    public ApiHelper() {
        restAdapter = new RestAdapter();
    }

    private String handlerErrors(Response mResponse) {
        return App.getContext().getString(R.string.unknowb_error) + mResponse.code();
    }


    public void getWeather() {
        restAdapter.init(false, "getWeather");
        ServiceWeather serviceApi =
                restAdapter.getRetrofit().create(ServiceWeather.class);
        Observable<Response<WeatherModel>> allSources =
                serviceApi.getWeather(
                        SettingsApp.getInstance().getKeyCity(),
                        authToken,
                        "ru",
                        "metric");
        allSources.subscribeOn(Schedulers.newThread())
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribe(new Subscriber<Response<WeatherModel>>() {
                    private NetworkResponseEvent event;
                    private NetworkResponseEvent<String> eventError;
                    private WeatherModel body;
                    private String errorText = "";

                    @Override
                    public void onCompleted() {
                        if (event != null) {
                            event.setSucess(true);
                            event.setData(body);
                        } else {
                            event = new NetworkResponseEvent<>();
                            event.setId(ApiConstants.ERROR);
                            event.setData(errorText);
                            event.setSucess(false);
                        }
                        BusProvider.send(event);
                    }

                    @Override
                    public void onError(Throwable e) {
                        eventError = new NetworkResponseEvent<>();
                        eventError.setId(ApiConstants.ERROR);
                        eventError.setData("Error load Sources ::: " + e.getMessage());
                        eventError.setSucess(false);
                        BusProvider.send(eventError);
                    }

                    @Override
                    public void onNext(Response<WeatherModel> mResponse) {
                        if (mResponse.isSuccessful()) {
                            event = new NetworkResponseEvent<>();
                            event.setId(ApiConstants.GET_WEATHER);
                            body = mResponse.body();
                        } else {
                            errorText = handlerErrors(mResponse);
                        }
                    }
                });
    }

}
