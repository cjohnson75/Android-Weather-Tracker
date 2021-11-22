package com.example.cole.termproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Main2Activity extends AppCompatActivity {

    TextView temperature, description, humidity;

    JSONObject jsonObj;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        temperature = (TextView) findViewById(R.id.temperature);
        description = (TextView) findViewById(R.id.description);
        humidity = (TextView) findViewById(R.id.humidity);

        Button fab = (Button) findViewById(R.id.btnSubmit);

        final EditText locationEditText = (EditText) findViewById(R.id.locationEditText);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                URL url = createURL(locationEditText.getText().toString());

                if (url != null) {

                    getWeather getLocalWeatherTask = new getWeather();
                    getLocalWeatherTask.execute(url);
                }
                else {

                    Toast.makeText(getApplicationContext(),R.string.invalid_url,Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private URL createURL(String city) {
        String apiKey = getString(R.string.api_key);
        String baseUrl = getString(R.string.web_service_url);

        try {

            String urlString = baseUrl + URLEncoder.encode(city, "UTF-8") + "&units=imperial&APPID=" + apiKey;
            return new URL(urlString);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class getWeather extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... params) {

            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) params[0].openConnection();
                int response = connection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    }
                    catch (IOException e) {
                        Toast.makeText(getApplicationContext(),R.string.read_error,Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    return new JSONObject(builder.toString());
                }
                else {

                    Toast.makeText(getApplicationContext(),R.string.connect_error,Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(),R.string.connect_error,Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject weather) {
            convertJSONtoArrayList(weather);
        }
    }

    private void convertJSONtoArrayList(JSONObject response) {
        description.setText("");
        temperature.setText("");
        humidity.setText("");

        try {
            jsonObj = (JSONObject) response.getJSONArray("weather").get(0);

            description.setText(jsonObj.getString("description"));
            temperature.setText(response.getJSONObject("main").getString("temp"));
            humidity.setText(response.getJSONObject("main").getString("humidity"));

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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
                if(x1 > x2)
                {
                    Intent i = new Intent(Main2Activity.this, MainActivity.class);
                    startActivity(i);
                }
                break;

        }
        return false;
    }
}
