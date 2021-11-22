package com.example.cole.termproject;

/**
 * Created by Cole on 5/4/2019.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.cole.termproject.data.DatabaseDescription.Weather;


public class AddEditFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>  {

    public interface AddEditFragmentListener {
        void onAddEditCompleted(Uri weatherUri);
    }

    private static final int WEATHER_LOADER = 0;

    private AddEditFragmentListener listener;
    private Uri weatherUri;
    private boolean addingNewWeather = true;

    private TextInputLayout dateTextInputLayout;
    private TextInputLayout categoryTextInputLayout;
    private TextInputLayout cityTextInputLayout;
    private TextInputLayout stateTextInputLayout;
    private TextInputLayout tempTextInputLayout;
    private TextInputLayout humidTextInputLayout;

    private FloatingActionButton saveWeatherFAB;
    private CoordinatorLayout coordinatorLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddEditFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        dateTextInputLayout = (TextInputLayout) view.findViewById(R.id.dateTextInputLayout);

        dateTextInputLayout.getEditText().addTextChangedListener(nameChangedListener);

        categoryTextInputLayout = (TextInputLayout) view.findViewById(R.id.categoryTextInputLayout);
        cityTextInputLayout = (TextInputLayout) view.findViewById(R.id.cityTextInputLayout);
        stateTextInputLayout = (TextInputLayout) view.findViewById(R.id.stateTextInputLayout);
        tempTextInputLayout = (TextInputLayout) view.findViewById(R.id.tempTextInputLayout);
        humidTextInputLayout = (TextInputLayout) view.findViewById(R.id.humidTextInputLayout);


        saveWeatherFAB = (FloatingActionButton) view.findViewById(R.id.saveFloatingActionButton);
        saveWeatherFAB.setOnClickListener(saveWeatherButtonClicked);
        updateSaveButtonFAB();

        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinatorLayout);

        Bundle arguments = getArguments();

        if (arguments != null) {
            addingNewWeather = false;
            weatherUri = arguments.getParcelable(MainActivity.WEATHER_URI);
        }

        if (weatherUri != null)
            getLoaderManager().initLoader(WEATHER_LOADER, null, this);

        return view;
    }

    private final TextWatcher nameChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            updateSaveButtonFAB();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private void updateSaveButtonFAB() {
        String input = dateTextInputLayout.getEditText().getText().toString();

        if (input.trim().length() != 0)
            saveWeatherFAB.show();
        else
            saveWeatherFAB.hide();
    }

    private final View.OnClickListener saveWeatherButtonClicked =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getView().getWindowToken(), 0);
                    saveWeather();
                }
            };

    private void saveWeather() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(Weather.COLUMN_DATE, dateTextInputLayout.getEditText().getText().toString());
        contentValues.put(Weather.COLUMN_CATEGORY, categoryTextInputLayout.getEditText().getText().toString());
        contentValues.put(Weather.COLUMN_CITY, cityTextInputLayout.getEditText().getText().toString());
        contentValues.put(Weather.COLUMN_STATE, stateTextInputLayout.getEditText().getText().toString());
        contentValues.put(Weather.COLUMN_TEMP, tempTextInputLayout.getEditText().getText().toString());
        contentValues.put(Weather.COLUMN_HUMID, humidTextInputLayout.getEditText().getText().toString());

        if (addingNewWeather) {

            Uri newWeatherUri = getActivity().getContentResolver().insert(Weather.CONTENT_URI, contentValues);

            if (newWeatherUri != null)
            {
                Snackbar.make(coordinatorLayout, R.string.weather_added, Snackbar.LENGTH_LONG).show();
                listener.onAddEditCompleted(newWeatherUri);
            }
            else {
                Snackbar.make(coordinatorLayout,
                        R.string.weather_not_added, Snackbar.LENGTH_LONG).show();
            }
        }
        else {

            int updatedRows = getActivity().getContentResolver().update(
                    weatherUri, contentValues, null, null);

            if (updatedRows > 0)
            {
                listener.onAddEditCompleted(weatherUri);
                Snackbar.make(coordinatorLayout,
                        R.string.weather_updated, Snackbar.LENGTH_LONG).show();
            }
            else {
                Snackbar.make(coordinatorLayout,
                        R.string.weather_not_updated, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case WEATHER_LOADER:
                return new CursorLoader(getActivity(),
                        weatherUri,
                        null,
                        null,
                        null,
                        null);
            default:
                return null;
        }
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

            dateTextInputLayout.getEditText().setText(data.getString(dateIndex));
            categoryTextInputLayout.getEditText().setText(data.getString(categoryIndex));
            cityTextInputLayout.getEditText().setText(data.getString(cityIndex));
            stateTextInputLayout.getEditText().setText(data.getString(stateIndex));
            tempTextInputLayout.getEditText().setText(data.getString(tempIndex));
            humidTextInputLayout.getEditText().setText(data.getString(humidIndex));

            updateSaveButtonFAB();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }

}
