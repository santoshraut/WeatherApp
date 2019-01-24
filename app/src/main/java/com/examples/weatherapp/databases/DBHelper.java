package com.examples.weatherapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.examples.weatherapp.models.WeatherDataModel;

import java.util.ArrayList;


/**
 * Created by Saantosh on 22/01/2019.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME= "WEATHER_DB";
    private static final int DB_VERSION = 1;

    /* TABLE NAME*/
    public static final String TABLE_WEATHER_DATA = "WEATHER_DETAILS";

    /* TABLE COLUMNS */
    public static final String KEY_WEATHER_ID = "WeatherID";
    public static final String KEY_COUNTRY = "County";
    public static final String KEY_YEAR = "Year";
    public static final String KEY_MONTH = "Month";
    public static final String KEY_TMIN = "Tmin";
    public static final String KEY_TMAX = "Tmax";
    public static final String KEY_RAINFALL = "Rainfall";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*PENDING TABLE MASTER*/
        String Create_Table_Weather_Data_Master = "CREATE TABLE IF NOT EXISTS " + TABLE_WEATHER_DATA + "(" +
                KEY_WEATHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_COUNTRY + " TEXT, " +
                KEY_YEAR + " INTEGER, " +
                KEY_MONTH + " INTEGER, " +
                KEY_TMIN + " REAL, " +
                KEY_TMAX + " REAL, " +
                KEY_RAINFALL + " REAL)";
        db.execSQL(Create_Table_Weather_Data_Master);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER_DATA);
        onCreate(db);
    }

    public void saveWeatherDetails(String country, int year, int month, float Tmin, float Tmax, float Rainfall){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COUNTRY, country);
        values.put(KEY_YEAR, year);
        values.put(KEY_MONTH, month);
        values.put(KEY_TMIN, Tmin);
        values.put(KEY_TMAX, Tmax);
        values.put(KEY_RAINFALL, Rainfall);

        try {
            db.insert(TABLE_WEATHER_DATA, null, values);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkWeatherTableIsEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isEmpty = true;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_WEATHER_DATA, null);
            if (cursor.getCount() < 1){
                isEmpty = true;
            }else {
                isEmpty = false;
            }
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isEmpty;
    }

    public ArrayList<WeatherDataModel> selectWeatherDataWithCondition(String country, String year){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<WeatherDataModel> weatherList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM WEATHER_DETAILS WHERE "+ KEY_COUNTRY + "=? AND "+ KEY_YEAR + "=?", new String[] {country, year}, null);
            cursor.moveToFirst();
            do {
                WeatherDataModel weather_model = new WeatherDataModel();
                Log.e(cursor.getColumnName(0), cursor.getString(0));
                Log.e(cursor.getColumnName(1), cursor.getString(1));
                Log.e(cursor.getColumnName(2), cursor.getString(2));
                Log.e(cursor.getColumnName(3), cursor.getString(3));
                Log.e(cursor.getColumnName(3), cursor.getString(4));
                Log.e(cursor.getColumnName(3), cursor.getString(5));
                Log.e(cursor.getColumnName(3), cursor.getString(6));
                weather_model.setCountry(cursor.getString(1));
                weather_model.setYear(cursor.getInt(2));
                weather_model.setMonth(cursor.getInt(3));
                weather_model.setTmin(cursor.getFloat(4));
                weather_model.setTmax(cursor.getFloat(5));
                weather_model.setRainfall(cursor.getFloat(6));
                weatherList.add(weather_model);
            } while (cursor.moveToNext());
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();

        return weatherList;
    }

    public ArrayList<WeatherDataModel> selectWeatherData(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<WeatherDataModel> weatherList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM "+ TABLE_WEATHER_DATA, null );
            cursor.moveToFirst();
            do {
                WeatherDataModel weather_model = new WeatherDataModel();
//                Log.e(cursor.getColumnName(0), cursor.getString(0));
//                Log.e(cursor.getColumnName(1), cursor.getString(1));
//                Log.e(cursor.getColumnName(2), cursor.getString(2));
//                Log.e(cursor.getColumnName(3), cursor.getString(3));
//                Log.e(cursor.getColumnName(3), cursor.getString(4));
//                Log.e(cursor.getColumnName(3), cursor.getString(5));
//                Log.e(cursor.getColumnName(3), cursor.getString(6));

                weather_model.setCountry(cursor.getString(1));
                weather_model.setYear(cursor.getInt(2));
                weather_model.setMonth(cursor.getInt(3));
                weather_model.setTmin(cursor.getFloat(4));
                weather_model.setTmax(cursor.getFloat(5));
                weather_model.setRainfall(cursor.getFloat(6));
                weatherList.add(weather_model);
            } while (cursor.moveToNext());
        }catch (Exception e){
            e.printStackTrace();
        }
        db.close();

        return weatherList;
    }

    public boolean deleteWeather(int Contact_Id, String weather){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_WEATHER_DATA, KEY_WEATHER_ID + "=" + weather, null) > 0;
    }

    public boolean deleteDataFromUserEmergencyContact(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_WEATHER_DATA, null, null) > 0;
    }

}
