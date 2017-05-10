package com.aleksandrp.openweathermapexample.rx.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksandrP on 10.05.2017.
 */

class Clouds {

    @SerializedName("all")
    @Expose
    public int all;

    public Clouds() {
    }
}
