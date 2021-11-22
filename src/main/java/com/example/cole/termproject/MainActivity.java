package com.example.cole.termproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity implements WeatherFragment.WeatherFragmentListener,
        DetailFragment.DetailFragmentListener,
        AddEditFragment.AddEditFragmentListener {

    public static final String WEATHER_URI = "weather_uri";

    private WeatherFragment weatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null && findViewById(R.id.fragmentContainer) != null) {

            weatherFragment = new WeatherFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, weatherFragment);
            transaction.commit();
        }
    }

    @Override
    public void onWeatherSelected(Uri weatherUri) {
        if (findViewById(R.id.fragmentContainer) != null)
            displayWeather(weatherUri, R.id.fragmentContainer);
    }

    @Override
    public void onAddWeather() {
        if (findViewById(R.id.fragmentContainer) != null)
            displayAddEditFragment(R.id.fragmentContainer, null);
    }

    private void displayWeather(Uri weatherUri, int viewID) {

        DetailFragment detailFragment = new DetailFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(WEATHER_URI, weatherUri);
        detailFragment.setArguments(arguments);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, detailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayAddEditFragment(int viewID, Uri weatherUri) {

        AddEditFragment addEditFragment = new AddEditFragment();

        if (weatherUri != null)
        {
            Bundle arguments = new Bundle();
            arguments.putParcelable(WEATHER_URI, weatherUri);
            addEditFragment.setArguments(arguments);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(viewID, addEditFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onWeatherDeleted() {
        getSupportFragmentManager().popBackStack();
        weatherFragment.updateWeatherList();
    }

    @Override
    public void onEditWeather(Uri weatherUri) {
        if (findViewById(R.id.fragmentContainer) != null)
            displayAddEditFragment(R.id.fragmentContainer, weatherUri);
    }

    @Override
    public void onAddEditCompleted(Uri weatherUri) {
        getSupportFragmentManager().popBackStack();
        weatherFragment.updateWeatherList();
    }

    float x1, x2, y1, y2;

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1 < x2)
                {
                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(i);
                }
                break;

        }
        return false;
    }
}