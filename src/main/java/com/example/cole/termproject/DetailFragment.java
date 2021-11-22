package com.example.cole.termproject;

/**
 * Created by Cole on 5/4/2019.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cole.termproject.data.DatabaseDescription.Weather;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface DetailFragmentListener {
        void onWeatherDeleted();

        void onEditWeather(Uri weatherUri);
    }

    private static final int WEATHER_LOADER = 0;

    private DetailFragmentListener listener;
    private Uri weatherUri;

    private TextView dateTextView;
    private TextView categoryTextView;
    private TextView cityTextView;
    private TextView stateTextView;
    private TextView tempTextView;
    private TextView humidTextView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DetailFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        Bundle arguments = getArguments();

        if (arguments != null)
            weatherUri = arguments.getParcelable(MainActivity.WEATHER_URI);


        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        categoryTextView = (TextView) view.findViewById(R.id.categoryTextView);
        cityTextView = (TextView) view.findViewById(R.id.cityTextView);
        stateTextView = (TextView) view.findViewById(R.id.stateTextView);
        tempTextView = (TextView) view.findViewById(R.id.tempTextView);
        humidTextView = (TextView) view.findViewById(R.id.humidTextView);

        getLoaderManager().initLoader(WEATHER_LOADER, null, this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                listener.onEditWeather(weatherUri);
                return true;
            case R.id.action_delete:
                deleteWeather();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteWeather() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Are you sure you want to delete?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(getActivity(),"You clicked yes button",Toast.LENGTH_LONG).show();
                        getActivity().getContentResolver().delete(
                                weatherUri, null, null);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader;

        switch (id) {
            case WEATHER_LOADER:
                cursorLoader = new CursorLoader(getActivity(),
                        weatherUri,
                        null,
                        null,
                        null,
                        null);
                break;

            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            int dateIndex = data.getColumnIndex(Weather.COLUMN_DATE);
            int categoryIndex = data.getColumnIndex(Weather.COLUMN_CATEGORY);
            int cityIndex = data.getColumnIndex(Weather.COLUMN_CITY);
            int stateIndex = data.getColumnIndex(Weather.COLUMN_STATE);
            int tempIndex = data.getColumnIndex(Weather.COLUMN_TEMP);
            int humidIndex = data.getColumnIndex(Weather.COLUMN_HUMID);

            dateTextView.setText(data.getString(dateIndex));
            categoryTextView.setText(data.getString(categoryIndex));
            cityTextView.setText(data.getString(cityIndex));
            stateTextView.setText(data.getString(stateIndex));
            tempTextView.setText(data.getString(tempIndex));
            humidTextView.setText(data.getString(humidIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}