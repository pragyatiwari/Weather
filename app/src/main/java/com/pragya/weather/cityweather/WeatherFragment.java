package com.pragya.weather.cityweather;

import android.app.SearchManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pragya.weather.R;
import com.pragya.weather.model.CityWeather;
import com.pragya.weather.model.DayWeather;
import com.pragya.weather.network.VolleyRequest;
import com.pragya.weather.util.LocationContract;

import java.util.List;

/**
 * view class for weather presenter.
 * will show data and transfer events to presenter
 * <p>
 * Created by Pragya on 8/24/2016.
 */
public class WeatherFragment extends Fragment
        implements WeatherContract.View {


    private RecyclerView mRecyclerView;
    private WeatherContract.Presenter mPresenter;
    private ImageLoader mImageLoader;
    private TextView mTextViewCityName;
    private SearchView mSearchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_fragmnet, container, false);
        setHasOptionsMenu(true);
        initializeViews(rootView);
        return rootView;
    }

    private void initializeViews(View rootView) {
        mTextViewCityName = (TextView) rootView.findViewById(R.id.cityName);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mImageLoader = VolleyRequest.getInstance(getActivity()).getImageLoader();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.floatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.loadWeatherForCurrentLocation();
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mPresenter.start();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_weather, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (null != mSearchView) {
            mSearchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getActivity().getComponentName()));
        }

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String text) {
                // do nothing
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                mPresenter.loadWeather(query);
                return true;
            }
        };
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(queryTextListener);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        getActivity().findViewById(R.id.progressBar).setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showWeather(CityWeather cityWeather) {
        mTextViewCityName.setText(cityWeather.cityName);
        RecyclerviewAdapter listAdapter = new RecyclerviewAdapter(cityWeather);
        mRecyclerView.setAdapter(listAdapter);
        mSearchView.clearFocus();
    }

    @Override
    public void showLoadingWeatherError(int errorCode) {

        switch (errorCode) {
            case LocationContract.ERROR_LOCATION_PERMISSION:
                showMessage(getResources().getString(R.string.enable_permission));
                break;
            case LocationContract.ERROR_ENABLE_LOCATION:
                showMessage(getResources().getString(R.string.message_enable_location));
                break;
            case LocationContract.ERROR_GETTING_LOCATION:
                showMessage(getResources().getString(R.string.error_getting_location));
                break;
            case LocationContract.ERROR_PLAYSERVICE_CONNECTION_FAILED:
                showMessage(getResources().getString(R.string.play_services_error));
                break;
            case LocationContract.ERROR_PLAYSERVICE_CONNECTION_SUSPENDED:
                showMessage(getResources().getString(R.string.play_service_disconnected));
                break;
            case LocationContract.ERROR_DATA_INCORRECT:
                showMessage(getResources().getString(R.string.weather_data_error));
                break;
            case LocationContract.ERROR_NETWORK_PROBLEM:
                showMessage(getResources().getString(R.string.network_error));
                break;
            default:
                showMessage(getResources().getString(R.string.unknown_error));
                break;
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        mPresenter = presenter;
    }

    // recycler view adapter for weather list
    private class RecyclerviewAdapter extends
            RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
        private List<DayWeather> mDayWeatherList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            private NetworkImageView image;
            private TextView title;
            private TextView date;
            private TextView minTemp;
            private TextView maxTemp;

            public ViewHolder(View view) {
                super(view);
                image = (NetworkImageView) view.findViewById(R.id.image);
                title = (TextView) view.findViewById(R.id.weatherTitle);
                date = (TextView) view.findViewById(R.id.date);
                minTemp = (TextView) view.findViewById(R.id.minTemperature);
                maxTemp = (TextView) view.findViewById(R.id.maxTemperature);
            }
        }

        public RecyclerviewAdapter(CityWeather cityWeather) {
            mDayWeatherList = cityWeather.dayWeatherList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_dayweather, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            DayWeather dayWeather = mDayWeatherList.get(position);
            holder.title.setText(dayWeather.weatherTitle);
            holder.date.setText(dayWeather.date);
            holder.minTemp.setText(dayWeather.minTemperature);
            holder.maxTemp.setText(dayWeather.maxTemperature);
            holder.image.setImageUrl(dayWeather.imageUrl, mImageLoader);
        }

        @Override
        public int getItemCount() {
            return mDayWeatherList.size();
        }
    }
}