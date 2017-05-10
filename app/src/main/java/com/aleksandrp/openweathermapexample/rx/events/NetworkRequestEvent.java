package com.aleksandrp.openweathermapexample.rx.events;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class NetworkRequestEvent <T> extends BaseEvent {


    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
