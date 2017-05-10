package com.aleksandrp.openweathermapexample.utils;

import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.aleksandrp.openweathermapexample.App;

/**
 * Created by AleksandrP on 10.05.2017.
 */

public class ShowToast {

    private static boolean isShowToast = true;

    public  static void showMessageError(String mMessage) {
        Toast toast = Toast.makeText(App.getContext(), mMessage, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        if (isShowToast) {
            isShowToast = false;
            toast.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isShowToast = true;
                }
            }, 3500);
        }
    }
}
