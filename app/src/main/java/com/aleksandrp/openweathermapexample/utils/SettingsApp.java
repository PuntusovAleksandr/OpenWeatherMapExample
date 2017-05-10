package com.aleksandrp.openweathermapexample.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.aleksandrp.openweathermapexample.App;

import static com.aleksandrp.openweathermapexample.utils.StaticParams.KIEV;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class SettingsApp {

    private static final String TAG = SettingsApp.class.getSimpleName();

    /**
     * Instance of SharedPreferences object
     */
    private SharedPreferences sPref;
    /**
     * Editor of SharedPreferences object
     */
    private SharedPreferences.Editor editor;

    private static SettingsApp ourInstance = new SettingsApp();

    /**
     * The constant FILE_NAME.
     */
// Settings xml file name
    public static final String FILE_NAME = "settings";

    /**
     * get instance settingsApp
     *
     * @return
     */
    public static SettingsApp getInstance() {
        return ourInstance;
    }


    /**
     * Construct the instance of the object
     */
    public SettingsApp() {
        sPref = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        editor = sPref.edit();
    }


    // Keys for opening settings from xml file
    private static final String KEY_CITY = "KEY_CITY";


    /**
     * GET key city
     *
     * @return
     */
    public String getKeyCity() {
        Log.d(TAG, "getKeyCity");
        return sPref.getString(KEY_CITY, KIEV);
    }

    /**
     * SET FOR key city
     *
     * @param ip
     */
    public void setKeyCity(String ip) {
        Log.d(TAG, "setKeyCity");
        editor.putString(KEY_CITY, ip).commit();
    }

}
