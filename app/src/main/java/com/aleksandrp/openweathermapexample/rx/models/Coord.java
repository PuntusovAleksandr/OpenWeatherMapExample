package com.aleksandrp.openweathermapexample.rx.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class Coord {
    @SerializedName("lon")
    @Expose
    public String lon;
    @SerializedName("lat")
    @Expose
    public String lat;

    public Coord() {
    }
}
