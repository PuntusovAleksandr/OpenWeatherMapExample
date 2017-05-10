package com.aleksandrp.openweathermapexample.rx.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class Main {

    @SerializedName("temp")
    @Expose
    public String temp;
    @SerializedName("pressure")
    @Expose
    public String pressure;
    @SerializedName("humidity")
    @Expose
    public String humidity;
    @SerializedName("temp_min")
    @Expose
    public String temp_min;
    @SerializedName("temp_max")
    @Expose
    public String temp_max;

    public Main() {
    }
}
