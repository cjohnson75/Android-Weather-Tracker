package com.example.cole.termproject;

/**
 * Created by Cole on 5/4/2019.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.cole.termproject.data.DatabaseDescription.Weather;


public class WeatherFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface WeatherFragmentListener {

        void onWeatherSelected(Uri weatherUri);
        void onAddWeather();
    }

    private static final int WEATHER_LOADER = 0;
    private WeatherFragmentListener listener;
    private WeatherAdapter weatherAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);


        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        weatherAdapter = new WeatherAdapter(
                new WeatherAdapter.WeatherClickListener() {
                    @Override
                    public void onClick(Uri WeatherUri) {
                        listener.onWeatherSelected(WeatherUri);
                    }
                }
        );
        recyclerView.setAdapter(weatherAdapter);

        recyclerView.addItemDecoration(new ItemDivider(getContext()));

        recyclerView.setHasFixedSize(true);

        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View view) {
                                             listener.onAddWeather();
                                         }
                                     }
        );

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (WeatherFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(WEATHER_LOADER, null, this);
    }

    public void updateWeatherList() {
        weatherAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case WEATHER_LOADER:
                return new CursorLoader(getActivity(),
                        Weather.CONTENT_URI,
                        null,
                        null,
                        null,
                        Weather.COLUMN_DATE + " COLLATE NOCASE ASC");
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        weatherAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        weatherAdapter.swapCursor(null);
    }


}