package com.aleksandrp.openweathermapexample.rx.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksandrP on 10.05.2017.
 */

class Wind {

    @SerializedName("speed")
    @Expose
    public String speed;
    @SerializedName("deg")
    @Expose
    public String deg;

    public Wind() {
    }
}
