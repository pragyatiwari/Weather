package com.pragya.weather.cityweather;

import android.support.annotation.NonNull;

import com.pragya.weather.model.CityWeather;
import com.pragya.weather.network.WeatherDataProvider;
import com.pragya.weather.util.LocationContract;
import com.pragya.weather.util.LocationContract.WeatherLocationProvider;

/**
 * listens to UI events and retrieves the data and updates UI
 * <p/>
 * Created by Pragya on 8/24/2016.
 */
public class WeatherPresenter implements WeatherContract.Presenter,
        WeatherDataProvider.WeatherDataCallback, LocationContract.LocationProviderCallback {

    private WeatherDataProvider mWeatherDataProvider;
    private WeatherContract.View mView;
    private WeatherLocationProvider mWeatherLocationProvider;

    public WeatherPresenter(@NonNull WeatherDataProvider weatherDataProvider,
                            WeatherContract.View weatherView, WeatherLocationProvider weatherLocationProvider) {
        mWeatherDataProvider = weatherDataProvider;
        mView = weatherView;
        mView.setPresenter(this);
        mWeatherLocationProvider = weatherLocationProvider;
    }


    @Override
    public void loadWeather(String city) {
        if (mView.isActive()) {
            mView.setLoadingIndicator(true);
            mWeatherDataProvider.getWeatherDataForCity(city, this);
        }
    }

    @Override
    public void loadWeatherForCurrentLocation() {
        mView.setLoadingIndicator(true);
        mWeatherLocationProvider.getCurrentLocation(this);

    }

    @Override
    public void start() {
        loadWeatherForCurrentLocation();
    }

    @Override
    public void onWeatherDataReceived(CityWeather cityWeather) {
        if (mView.isActive()) {
            mView.setLoadingIndicator(false);
            if (cityWeather != null) {
                mView.showWeather(cityWeather);
            } else {
                mView.showLoadingWeatherError(LocationContract.ERROR_DATA_INCORRECT);
            }
        }
    }

    @Override
    public void onWeatherDataError(String error) {
        if (mView.isActive()) {
            mView.setLoadingIndicator(false);
            mView.showLoadingWeatherError(LocationContract.ERROR_NETWORK_PROBLEM);
        }
    }

    @Override
    public void onGetLocation(String latitude, String longitude) {
        if (mView.isActive()) {
            mWeatherDataProvider.getWeatherDataForLocation(latitude, longitude, this);
        }
    }

    @Override
    public void onLocationError(int errorCode) {
        if (mView.isActive()) {
            mView.setLoadingIndicator(false);
            mView.showLoadingWeatherError(errorCode);
        }
    }
}
