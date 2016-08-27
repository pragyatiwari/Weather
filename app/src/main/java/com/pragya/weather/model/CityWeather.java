package com.pragya.weather.model;

import java.util.List;

/**
 * City Weather
 * <p>
 * Created by Pragya on 8/24/2016.
 */
public class CityWeather {
    public String cityName;
    public List<DayWeather> dayWeatherList;

    public CityWeather(String cityName, List<DayWeather> dayWeatherList) {
        this.cityName = cityName;
        this.dayWeatherList = dayWeatherList;
    }
}
