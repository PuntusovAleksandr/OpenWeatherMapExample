package com.aleksandrp.openweathermapexample.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aleksandrp.openweathermapexample.R;
import com.aleksandrp.openweathermapexample.api.services.ServiceApi;
import com.aleksandrp.openweathermapexample.interfaces.MvpView;
import com.aleksandrp.openweathermapexample.presenter.MainPresenter;
import com.aleksandrp.openweathermapexample.rx.events.NetworkRequestEvent;
import com.aleksandrp.openweathermapexample.rx.models.WeatherModel;
import com.aleksandrp.openweathermapexample.utils.SettingsApp;
import com.aleksandrp.openweathermapexample.utils.ShowToast;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.aleksandrp.openweathermapexample.api.RestAdapter.API_ICON_URL;
import static com.aleksandrp.openweathermapexample.api.constants.Exatras.SERVICE_JOB_ID_TITLE;
import static com.aleksandrp.openweathermapexample.utils.InternetUtils.checkInternetConnection;
import static com.aleksandrp.openweathermapexample.utils.StaticParams.DNEPR;
import static com.aleksandrp.openweathermapexample.utils.StaticParams.KHARKOV;
import static com.aleksandrp.openweathermapexample.utils.StaticParams.KIEV;

public class MainActivity extends AppCompatActivity implements MvpView {

    /**
     * DECLARE VIEWS
     */
    @Bind(R.id.progressBar_registration)
    RelativeLayout progressBar_registration;

    @Bind(R.id.tv_city)
    TextView tv_city;
    @Bind(R.id.tv_value)
    TextView tv_value;
    @Bind(R.id.tv_max_value)
    TextView tv_max_value;
    @Bind(R.id.tv_min_value)
    TextView tv_min_value;
    @Bind(R.id.tv_summer)
    TextView tv_summer;

    @Bind(R.id.iv_icon_weather)
    ImageView iv_icon_weather;

    /**
     * DECLARE SERVICE
     */
    private MainPresenter mPresenter;
    private Intent serviceIntent;

    private Timer timer;
    private TimerTask task;
    private final long TIME_PERIOD = 1000 * 60 * 30;        // period 30 min

    /**
     * DECLARE PARAMS
     */
    private String name;
    private String temp;
    private String temp_min;
    private String temp_max;
    private String weatherMain;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initPresenter();

        initToolBar();

        mPresenter.registerSubscriber();

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getWeather();
                    }
                });
            }
        };
        timer.schedule(task, 0, TIME_PERIOD);      // period
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWeather();
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        mPresenter.unRegisterSubscriber();
        if (mPresenter != null)
            mPresenter.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", tv_city.getText().toString());
        outState.putString("temp", tv_value.getText().toString());
        outState.putString("temp_min", tv_min_value.getText().toString());
        outState.putString("temp_max", tv_max_value.getText().toString());
        outState.putString("weatherMain", tv_summer.getText().toString());
        outState.putString("path", tv_city.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        name = savedInstanceState.getString("name");
        temp = savedInstanceState.getString("temp");
        temp_min = savedInstanceState.getString("temp_min");
        temp_max = savedInstanceState.getString("temp_max");
        weatherMain = savedInstanceState.getString("weatherMain");
        path = savedInstanceState.getString("path");
        setDataUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_kiev:
                SettingsApp.getInstance().setKeyCity(KIEV);
                getWeather();
                return true;

            case R.id.action_kharkov:
                SettingsApp.getInstance().setKeyCity(KHARKOV);
                getWeather();
                return true;

            case R.id.action_dnepr:
                SettingsApp.getInstance().setKeyCity(DNEPR);
                getWeather();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Get from server
     */
    private void getWeather() {
        if (checkInternetConnection()) {
            showProgress(true);
            mPresenter.getWeather();
        } else {
            showMessageError(getString(R.string.faild_internet));
        }
    }

    /**
     * Init  presenter
     */
    private void initPresenter() {
        serviceIntent = new Intent(this, ServiceApi.class);
        mPresenter = new MainPresenter();
        mPresenter.setMvpView(this);
        mPresenter.init();
    }

    /**
     * Stop timer
     */
    private void stopTimer() {
        if (timer != null)
            timer.cancel();
    }

    /**
     * Init tool bar views
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //    ================================
    // from presenter
    private void showProgress(boolean mShowPhone) {
        if (mShowPhone) {
            progressBar_registration.setVisibility(View.VISIBLE);
        } else {
            progressBar_registration.setVisibility(View.GONE);
        }
    }

    public void makeRequest(NetworkRequestEvent<Bundle> mEvent) {
        serviceIntent.putExtra(SERVICE_JOB_ID_TITLE, mEvent.getId());
        startService(serviceIntent);
    }

    public void showMessageError(String mMessage) {
        showProgress(false);
        ShowToast.showMessageError(mMessage);
    }

    public void showWeather(WeatherModel mData) {
        showProgress(false);
        this.name = mData.name;
        this.temp = mData.main.temp;
        this.temp_min = mData.main.temp_min;
        this.temp_max = mData.main.temp_max;
        this.weatherMain = mData.weather.get(0).main;
        this.path = API_ICON_URL + mData.weather.get(0).icon + ".png";
        if (!temp.startsWith("-")) temp = "+" + temp;
        if (!temp_min.startsWith("-")) temp_min = "+" + temp_min;
        if (!temp_max.startsWith("-")) temp_max = "+" + temp_max;

        setDataUi();
    }

    private void setDataUi() {
        tv_city.setText(name);
        tv_value.setText(temp);
        tv_min_value.setText(temp_min);
        tv_max_value.setText(temp_max);
        tv_summer.setText(weatherMain);

        if (path.isEmpty()) path = String.valueOf(getResources().getDrawable(R.mipmap.ic_launcher));
        Picasso.with(this)
                .load(path)
                .fit()
                .centerInside()
                .into(iv_icon_weather);
    }
}
