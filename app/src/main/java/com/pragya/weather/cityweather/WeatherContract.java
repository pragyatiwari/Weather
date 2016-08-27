package com.pragya.weather.cityweather;

import com.pragya.weather.model.CityWeather;
import com.pragya.weather.util.BasePresenter;
import com.pragya.weather.util.BaseView;

/**
 * Weather contract between view and presenter
 * <p>
 * Created by Pragya on 8/22/2016.
 */
public interface WeatherContract {
    interface View extends BaseView<Presenter> {

        /**
         * set loading indicator
         *
         * @param active if true show indicator, if false hide indicator
         */
        void setLoadingIndicator(boolean active);

        /**
         * show weather with provided data
         *
         * @param cityWeather
         */
        void showWeather(CityWeather cityWeather);

        /**
         * show error
         *
         * @param errorCode
         */
        void showLoadingWeatherError(int errorCode);

        /**
         * check if view is active
         *
         * @return
         */
        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        /**
         * get weather data for provided city
         *
         * @param city
         */
        void loadWeather(String city);

        /**
         * get weather data for current location
         */
        void loadWeatherForCurrentLocation();
    }
}

