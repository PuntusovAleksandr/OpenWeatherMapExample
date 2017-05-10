package com.aleksandrp.openweathermapexample.rx.events;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class UpdateUiEvent<T> extends BaseEvent {


    private T data;
    private boolean success;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
