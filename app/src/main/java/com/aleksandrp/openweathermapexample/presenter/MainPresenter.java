package com.aleksandrp.openweathermapexample.presenter;

import android.os.Bundle;

import com.aleksandrp.openweathermapexample.activity.MainActivity;
import com.aleksandrp.openweathermapexample.api.constants.ApiConstants;
import com.aleksandrp.openweathermapexample.interfaces.PresenterEventListener;
import com.aleksandrp.openweathermapexample.rx.BusProvider;
import com.aleksandrp.openweathermapexample.rx.events.NetworkFailEvent;
import com.aleksandrp.openweathermapexample.rx.events.NetworkRequestEvent;
import com.aleksandrp.openweathermapexample.rx.events.UpdateUiEvent;
import com.aleksandrp.openweathermapexample.rx.models.WeatherModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static com.aleksandrp.openweathermapexample.api.constants.ApiConstants.RESPONSE_GET_WEATHER;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class MainPresenter extends BasePresenter implements PresenterEventListener {


    private Subscriber subscriber;


    @Override
    public void init() {

    }

    @Override
    public void registerSubscriber() {
        subscriber = new Subscriber() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Object mO) {
                if (mO instanceof UpdateUiEvent) {
                    UpdateUiEvent event = (UpdateUiEvent) mO;
                    Object data = event.getData();
                    if (event.getId() == RESPONSE_GET_WEATHER) {
                        showWeather((WeatherModel) data);
                    }
                } else if (mO instanceof NetworkFailEvent) {
                    NetworkFailEvent event = (NetworkFailEvent) mO;
                    showMessageError(event.getMessage());
                }
            }
        };
        BusProvider.observe().observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    //================================================
    @Override
    public void unRegisterSubscriber() {
        if (subscriber != null && !subscriber.isUnsubscribed()) {
            subscriber.unsubscribe();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        subscriber = null;
    }

    //================================================

    private void showWeather(WeatherModel mData) {
        ((MainActivity) mvpView).showWeather(mData);
    }

    public void showMessageError(String mMessage) {
        ((MainActivity) mvpView).showMessageError(mMessage);
    }


    public void getWeather() {
        NetworkRequestEvent<Bundle> networkRequestEvent = new NetworkRequestEvent();
        networkRequestEvent.setId(ApiConstants.GET_WEATHER);
        ((MainActivity) mvpView).makeRequest(networkRequestEvent);
    }
}
