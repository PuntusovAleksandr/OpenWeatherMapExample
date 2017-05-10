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
import com.aleksandrp.openweathermapexample.utils.ShowToasr;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.aleksandrp.openweathermapexample.api.RestAdapter.API_ICON_URL;
import static com.aleksandrp.openweathermapexample.api.constants.Exatras.SERVICE_JOB_ID_TITLE;
import static com.aleksandrp.openweathermapexample.utils.StaticParams.DNEPR;
import static com.aleksandrp.openweathermapexample.utils.StaticParams.KHARKOV;
import static com.aleksandrp.openweathermapexample.utils.StaticParams.KIEV;

public class MainActivity extends AppCompatActivity implements MvpView {


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

    private MainPresenter mPresenter;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initPresenter();

        initToolBar();

        mPresenter.registerSubscriber();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.getWeather();
    }

    @Override
    public void onDetachedFromWindow() {
        mPresenter.unRegisterSubscriber();
        if (mPresenter != null)
            mPresenter.destroy();
        super.onDetachedFromWindow();
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
                mPresenter.getWeather();
                return true;

            case R.id.action_kharkov:
                SettingsApp.getInstance().setKeyCity(KHARKOV);
                mPresenter.getWeather();
                return true;

            case R.id.action_dnepr:
                SettingsApp.getInstance().setKeyCity(DNEPR);
                mPresenter.getWeather();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initPresenter() {
        serviceIntent = new Intent(this, ServiceApi.class);
        mPresenter = new MainPresenter();
        mPresenter.setMvpView(this);
        mPresenter.init();
    }

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
        ShowToasr.showMessageError(mMessage);
    }

    public void showWeather(WeatherModel mData) {
        String temp = mData.main.temp;
        String temp_min = mData.main.temp_min;
        String temp_max = mData.main.temp_max;
        if (!temp.startsWith("-")) temp = "+" + temp;
        if (!temp_min.startsWith("-")) temp_min = "+" + temp_min;
        if (!temp_max.startsWith("-")) temp_max = "+" + temp_max;
        tv_city.setText(mData.name);
        tv_value.setText(temp);
        tv_min_value.setText(temp_min);
        tv_max_value.setText(temp_max);
        tv_summer.setText(mData.weather.get(0).main);

        Picasso.with(this)
                .load(API_ICON_URL + mData.weather.get(0).icon + ".png")
                .fit()
                .centerInside()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(iv_icon_weather);

    }
}
