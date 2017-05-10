package com.aleksandrp.openweathermapexample.rx.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksandrP on 10.05.2017.
 */

class Sys {

    @SerializedName("type")
    @Expose
    public int type;
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("sunrise")
    @Expose
    public String sunrise;
    @SerializedName("sunset")
    @Expose
    public String sunset;

    public Sys() {
    }
}
