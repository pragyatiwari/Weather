package com.pragya.weather.util;

/**
 * contact to get location and perform actions
 * <p>
 * Created by Pragya on 8/27/2016.
 */
public interface LocationContract {

    int ERROR_LOCATION_PERMISSION = 1;
    int ERROR_ENABLE_LOCATION = 2;
    int ERROR_GETTING_LOCATION = 3;
    int ERROR_PLAYSERVICE_CONNECTION_FAILED = 4;
    int ERROR_PLAYSERVICE_CONNECTION_SUSPENDED = 5;
    int ERROR_DATA_INCORRECT = 6;
    int ERROR_NETWORK_PROBLEM = 7;

    interface WeatherLocationProvider {

        /**
         * will provide current location in callback
         *
         * @param locationProviderCallback callback to provide result
         */
        void getCurrentLocation(LocationProviderCallback locationProviderCallback);
    }

    interface LocationProviderCallback {
        /**
         * return location result
         *
         * @param latitude
         * @param longitude
         */
        void onGetLocation(String latitude, String longitude);

        /**
         * return error while getting location
         *
         * @param errorCode errorCodes from LocationContract
         */
        void onLocationError(int errorCode);
    }
}
