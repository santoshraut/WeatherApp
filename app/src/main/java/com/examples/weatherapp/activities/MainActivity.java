package com.examples.weatherapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.weatherapp.R;
import com.examples.weatherapp.adapters.WeatherDataViewAdapter;
import com.examples.weatherapp.apputils.Hashdefine;
import com.examples.weatherapp.databases.DBHelper;
import com.examples.weatherapp.models.WeatherDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static ArrayList<String> responseList = new ArrayList<>();
    private static ArrayList<String> countries = new ArrayList<String>();
    private static ArrayList<String> metrics = new ArrayList<String>();
    private static ArrayList<String> countriesCombination = new ArrayList<>();
    private static ArrayList<String> metricsCombination = new ArrayList<>();

    private static ArrayList<Integer> yearsList = null;
    private static ArrayList<Integer> monthsList = null;
    private static ArrayList<Float> tminList = null;
    private static ArrayList<Float> tmaxList = null;
    private static ArrayList<Float> rainfallList = null;

    private static DBHelper dbHelper;
    private static ProgressDialog progressDialog;
    private static RecyclerView recyclerView;
    private static WeatherDataViewAdapter weatherDataViewAdapter;

    private Button yearBtn, countryBtn;
    static Dialog d ;
    int year = Calendar.getInstance().get(Calendar.YEAR);
    AlertDialog levelDialog;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(MainActivity.this);
        context = MainActivity.this;

        yearBtn = findViewById(R.id.year);
        countryBtn = findViewById(R.id.country);
        recyclerView = findViewById(R.id.weatherRecycler);
        RecyclerView.LayoutManager categoriesLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(categoriesLayout);

        countries.add("UK");
        countries.add("England");
        countries.add("Scotland");
        countries.add("Wales");

        metrics.add("Tmin");
        metrics.add("Tmax");
        metrics.add("Rainfall");

        if (dbHelper.checkWeatherTableIsEmpty()) {
            // Load All Data At a First Time
            if (Hashdefine.isNetworkAvailable(MainActivity.this)) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Loading Data Please Wait");
                progressDialog.show();
                for (int i = 0; i < countries.size(); i++) {
                    for (int j = 0; j < metrics.size(); j++) {
                        String BASEURL = "https://s3.eu-west-2.amazonaws.com/interview-question-data/metoffice/" + metrics.get(j) + "-" + countries.get(i) + ".json";
                        String country = countries.get(i);
                        String metric = metrics.get(j);
                        countriesCombination.add(country);
                        metricsCombination.add(metric);
                        new HttpGetRequest().execute(BASEURL);
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();
            }
        }else{
            ArrayList<WeatherDataModel> weatherDataList = dbHelper.selectWeatherData();
//            for (int i = 0; i < weatherDataList.size(); i++) {
//                        Log.e("weatherDataList", weatherDataList.get(i).getCountry() + "-" + weatherDataList.get(i).getYear()
//                                + "-" + weatherDataList.get(i).getMonth() + "-" + weatherDataList.get(i).getTmin() + "-" +
//                                weatherDataList.get(i).getTmax() + "-" + weatherDataList.get(i).getRainfall());
//            }

            // SetAdapter
            weatherDataViewAdapter = new WeatherDataViewAdapter(MainActivity.this, weatherDataList);
            recyclerView.setAdapter(weatherDataViewAdapter);
        }

        yearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearDialog();
            }
        });

        countryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Strings to Show In Dialog with Radio Buttons
                final CharSequence[] items = {"UK","England","Scotland","Wales"};

                // Creating and Building the Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select Country");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        switch(item)
                        {
                            case 0:
                                // Your code when first option seletced
                                countryBtn.setText("UK");

                                if(yearBtn.getText().toString().equalsIgnoreCase("Select Year")){
                                    Toast.makeText(getApplicationContext(), "Please Select Year", Toast.LENGTH_LONG).show();
                                }else {
                                    ArrayList<WeatherDataModel> weatherDataList = dbHelper.selectWeatherDataWithCondition(countryBtn.getText().toString(),
                                            yearBtn.getText().toString());
                                    weatherDataViewAdapter = new WeatherDataViewAdapter(MainActivity.this, weatherDataList);
                                    recyclerView.setAdapter(weatherDataViewAdapter);
                                    weatherDataViewAdapter.notifyDataSetChanged();
                                }

                                break;
                            case 1:
                                // Your code when 2nd  option seletced
                                countryBtn.setText("England");

                                if(yearBtn.getText().toString().equalsIgnoreCase("Select Year")){
                                    Toast.makeText(getApplicationContext(), "Please Select Year", Toast.LENGTH_LONG).show();
                                }else {
                                    ArrayList<WeatherDataModel> weatherDataList = dbHelper.selectWeatherDataWithCondition(countryBtn.getText().toString(),
                                            yearBtn.getText().toString());
                                    weatherDataViewAdapter = new WeatherDataViewAdapter(MainActivity.this, weatherDataList);
                                    recyclerView.setAdapter(weatherDataViewAdapter);
                                    weatherDataViewAdapter.notifyDataSetChanged();
                                }

                                break;
                            case 2:
                                // Your code when 3rd option seletced
                                countryBtn.setText("Scotland");

                                if(yearBtn.getText().toString().equalsIgnoreCase("Select Year")){
                                    Toast.makeText(getApplicationContext(), "Please Select Year", Toast.LENGTH_LONG).show();
                                }else {
                                    ArrayList<WeatherDataModel> weatherDataList = dbHelper.selectWeatherDataWithCondition(countryBtn.getText().toString(),
                                            yearBtn.getText().toString());
                                    weatherDataViewAdapter = new WeatherDataViewAdapter(MainActivity.this, weatherDataList);
                                    recyclerView.setAdapter(weatherDataViewAdapter);
                                    weatherDataViewAdapter.notifyDataSetChanged();
                                }

                                break;
                            case 3:
                                // Your code when 4th  option seletced
                                countryBtn.setText("Wales");

                                if(yearBtn.getText().toString().equalsIgnoreCase("Select Year")){
                                    Toast.makeText(getApplicationContext(), "Please Select Year", Toast.LENGTH_LONG).show();
                                }else {
                                    ArrayList<WeatherDataModel> weatherDataList = dbHelper.selectWeatherDataWithCondition(countryBtn.getText().toString(),
                                            yearBtn.getText().toString());
                                    weatherDataViewAdapter = new WeatherDataViewAdapter(MainActivity.this, weatherDataList);
                                    recyclerView.setAdapter(weatherDataViewAdapter);
                                    weatherDataViewAdapter.notifyDataSetChanged();
                                }

                                break;

                            default:
                                break;

                        }
                        levelDialog.dismiss();
                    }
                });
                levelDialog = builder.create();
                levelDialog.show();
            }
        });

    }

    public void showYearDialog()
    {

        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("Year Picker");
        d.setContentView(R.layout.year_dialog);
        Button set = (Button) d.findViewById(R.id.button1);
        Button cancel = (Button) d.findViewById(R.id.button2);
        TextView year_text=(TextView)d.findViewById(R.id.year_text);
        year_text.setText(""+year);
        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        nopicker.setMaxValue(year+0);
        nopicker.setMinValue(year-109);
        nopicker.setWrapSelectorWheel(false);
        nopicker.setValue(year);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                yearBtn.setText(String.valueOf(nopicker.getValue()));
                d.dismiss();

                if(countryBtn.getText().toString().equalsIgnoreCase("Select Country")){
                       Toast.makeText(getApplicationContext(), "Please Select Country", Toast.LENGTH_LONG).show();
                }else {
                    ArrayList<WeatherDataModel> weatherDataList = dbHelper.selectWeatherDataWithCondition(countryBtn.getText().toString(),
                            yearBtn.getText().toString());

                    // SetAdapter
                    weatherDataViewAdapter = new WeatherDataViewAdapter(MainActivity.this, weatherDataList);
                    recyclerView.setAdapter(weatherDataViewAdapter);
                    weatherDataViewAdapter.notifyDataSetChanged();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    public static class HttpGetRequest extends AsyncTask<String, Void, String> {

        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];

            String result;
            String inputLine;

            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);

                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();

                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();

                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());

                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();

                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }

                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();

                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }

            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
//                Log.e("Response -- ", result);

                responseList.add(result);

                if (responseList.size() == 12) {
                    for (int k = 0; k < responseList.size(); k++) {
                        JSONArray jsonArray = new JSONArray(responseList.get(k));
//                        Log.e("jsonArray -", jsonArray.toString());
                        String country = countriesCombination.get(k);
                        String metric = metricsCombination.get(k);

                        if (country.equalsIgnoreCase("UK")) {
                            if (metric.equalsIgnoreCase("Tmin")) {
                                yearsList = new ArrayList<>();
                                monthsList = new ArrayList<>();
                                tminList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = (jsonObject.optInt("value"));
                                    yearsList.add(year);
                                    monthsList.add(month);
                                    tminList.add(value);
                                }
                            } else if (metric.equalsIgnoreCase("Tmax")) {
                                tmaxList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    tmaxList.add(value);
                                }
                            } else if (metric.equalsIgnoreCase("Rainfall")) {
                                rainfallList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    rainfallList.add(value);
                                }
                                for (int i = 0; i < yearsList.size(); i++) {
//                                    Log.e("UK Weather Data - ", "Year - " + yearsList.get(i) + "\nMonth - " +
//                                            monthsList.get(i) + "\nTmin - " + tminList.get(i) + "\nTmax - " +
//                                            tmaxList.get(i) + "\nRainfall - " + rainfallList.get(i));

                                     dbHelper.saveWeatherDetails(country, yearsList.get(i), monthsList.get(i),
                                             tminList.get(i), tmaxList.get(i) , rainfallList.get(i));
                                }
                            }
                        } else if (country.equalsIgnoreCase("England")) {
                            if (metric.equalsIgnoreCase("Tmin")) {
                                yearsList = new ArrayList<>();
                                monthsList = new ArrayList<>();
                                tminList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    yearsList.add(year);
                                    monthsList.add(month);
                                    tminList.add(value);
                                }
                            } else if (metric.equalsIgnoreCase("Tmax")) {
                                tmaxList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    tmaxList.add(value);
                                }
                            } else if (metric.equalsIgnoreCase("Rainfall")) {
                                rainfallList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    rainfallList.add(value);
                                }
                                for (int i = 0; i < yearsList.size(); i++) {
//                                    Log.e("England Weather Data3- ", "Year - " + yearsList.get(i) + "\nMonth - " +
//                                            monthsList.get(i) + "\nTmin - " + tminList.get(i) + "\nTmax - " +
//                                            tmaxList.get(i) + "\nRainfall - " + rainfallList.get(i));

                                    dbHelper.saveWeatherDetails(country, yearsList.get(i), monthsList.get(i),
                                            tminList.get(i), tmaxList.get(i) , rainfallList.get(i));
                                }
                            }
                        } else if (country.equalsIgnoreCase("Scotland")) {
                            if (metric.equalsIgnoreCase("Tmin")) {
                                yearsList = new ArrayList<>();
                                monthsList = new ArrayList<>();
                                tminList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    yearsList.add(year);
                                    monthsList.add(month);
                                    tminList.add(value);
                                }
                            } else if (metric.equalsIgnoreCase("Tmax")) {
                                tmaxList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    tmaxList.add(value);
                                }
                            } else if (metric.equalsIgnoreCase("Rainfall")) {
                                rainfallList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    rainfallList.add(value);
                                }
                                for (int i = 0; i < yearsList.size(); i++) {
//                                    Log.e("Scot Weather Data - ", "Year - " + yearsList.get(i) + "\nMonth - " +
//                                            monthsList.get(i) + "\nTmin - " + tminList.get(i) + "\nTmax - " +
//                                            tmaxList.get(i) + "\nRainfall - " + rainfallList.get(i));

                                    dbHelper.saveWeatherDetails(country, yearsList.get(i), monthsList.get(i),
                                            tminList.get(i), tmaxList.get(i) , rainfallList.get(i));
                                }
                            }
                        }
                        if (country.equalsIgnoreCase("Wales")) {
                            if (metric.equalsIgnoreCase("Tmin")) {
                                yearsList = new ArrayList<>();
                                monthsList = new ArrayList<>();
                                tminList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    yearsList.add(year);
                                    monthsList.add(month);
                                    tminList.add(value);
                                }
                            } else if (metric.equalsIgnoreCase("Tmax")) {
                                tmaxList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    tmaxList.add(value);
                                }
                            } else if (metric.equalsIgnoreCase("Rainfall")) {
                                rainfallList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int year = jsonObject.optInt("year");
                                    int month = jsonObject.optInt("month");
                                    float value = jsonObject.optInt("value");
                                    rainfallList.add(value);
                                }
                                for (int i = 0; i < yearsList.size(); i++) {
//                                    Log.e("Wales Weather Data - ", "Year - " + yearsList.get(i) + "\nMonth - " +
//                                            monthsList.get(i) + "\nTmin - " + tminList.get(i) + "\nTmax - " +
//                                            tmaxList.get(i) + "\nRainfall - " + rainfallList.get(i));

                                    dbHelper.saveWeatherDetails(country, yearsList.get(i), monthsList.get(i),
                                            tminList.get(i), tmaxList.get(i) , rainfallList.get(i));
                                }
                            }
                        }
                    }
                }

                if (responseList.size() >= 12) {
                    progressDialog.dismiss();

                    // Select Weather Data
                    ArrayList<WeatherDataModel> weatherDataList = dbHelper.selectWeatherData();
//                    for (int i = 0; i < weatherDataList.size(); i++) {
//                        Log.e("weatherDataList", weatherDataList.get(i).getCountry() + "-" + weatherDataList.get(i).getYear()
//                                + "-" + weatherDataList.get(i).getMonth() + "-" + weatherDataList.get(i).getTmin() + "-" +
//                                weatherDataList.get(i).getTmax() + "-" + weatherDataList.get(i).getRainfall());
//                    }
                    // SetAdapter
                    weatherDataViewAdapter = new WeatherDataViewAdapter(context, weatherDataList);
                    recyclerView.setAdapter(weatherDataViewAdapter);
                }

            } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }

    }

}
