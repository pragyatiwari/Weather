package com.pragya.weather.model;

/**
 * Day Weather
 * <p/>
 * Created by Pragya on 8/24/2016.
 */
public class DayWeather {

    public String weatherTitle;
    public String minTemperature;
    public String maxTemperature;
    public String date;
    public String imageUrl;

    public DayWeather(String weatherTitle, String minTemperature, String maxTemperature, String date, String imageUrl) {
        this.weatherTitle = weatherTitle;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.date = date;
        this.imageUrl = imageUrl;
    }
}
