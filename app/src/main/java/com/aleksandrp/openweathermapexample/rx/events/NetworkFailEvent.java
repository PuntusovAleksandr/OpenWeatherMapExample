package com.aleksandrp.openweathermapexample.rx.events;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class NetworkFailEvent {

    private int errorCode;
    private String message;

    public NetworkFailEvent() {
    }

    public NetworkFailEvent(String mData) {
        this.message = mData;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
