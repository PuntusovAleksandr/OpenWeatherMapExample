package com.aleksandrp.openweathermapexample.api.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.aleksandrp.openweathermapexample.App;
import com.aleksandrp.openweathermapexample.R;
import com.aleksandrp.openweathermapexample.api.constants.ApiConstants;
import com.aleksandrp.openweathermapexample.api.helpers.ApiHelper;
import com.aleksandrp.openweathermapexample.rx.BusProvider;
import com.aleksandrp.openweathermapexample.rx.events.NetworkFailEvent;
import com.aleksandrp.openweathermapexample.rx.events.NetworkResponseEvent;
import com.aleksandrp.openweathermapexample.rx.events.UpdateUiEvent;

import rx.Subscriber;

import static com.aleksandrp.openweathermapexample.api.constants.Exatras.SERVICE_JOB_ID_TITLE;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class ServiceApi extends Service {

    private Subscriber subscriber;
    private static final String LOGGER_TAG = "ServiceWeather";
    private int startId;

    private ApiHelper mApiHelper;

    public ServiceApi() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        subscriber = new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                if (o instanceof NetworkResponseEvent) {
                    NetworkResponseEvent event = (NetworkResponseEvent) o;
                    if (!event.isSucess()) {
                        requestFailed(event);
                    }
                    requestCallBack(event);
                }
            }
        };
        BusProvider.observe().subscribe(subscriber);
        Log.d(LOGGER_TAG, "subscribe");
        mApiHelper = new ApiHelper();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriber.unsubscribe();
        subscriber = null;
        mApiHelper = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        int jobId = intent.getIntExtra(SERVICE_JOB_ID_TITLE, -1);

        switch (jobId) {
            case ApiConstants.GET_WEATHER:
                mApiHelper.getWeather();
                break;
        }
        return START_NOT_STICKY;
    }


    private void requestCallBack(NetworkResponseEvent event) {
        UpdateUiEvent updateUiEvent = new UpdateUiEvent();
        updateUiEvent.setSuccess(event.isSucess());
        updateUiEvent.setData(event.getData());
        switch (event.getId()) {
            case ApiConstants.GET_WEATHER:
                updateUiEvent.setId(ApiConstants.RESPONSE_GET_WEATHER);
                break;

        }
        if (updateUiEvent != null) {
            BusProvider.send(updateUiEvent);
        }
        stopSelf(startId);
    }

    private void requestFailed(NetworkResponseEvent event) {

        NetworkFailEvent networkFailEvent = new NetworkFailEvent();
        if (event.getId() == ApiConstants.ERROR) {
            String data = (String) event.getData();
            if (data.isEmpty()) data = App.getContext().getString(R.string.bad_internet);
            networkFailEvent.setMessage(data);
        } else {
            networkFailEvent.setMessage("Error request");
        }
        BusProvider.send(networkFailEvent);
        stopSelf(startId);
    }
}
