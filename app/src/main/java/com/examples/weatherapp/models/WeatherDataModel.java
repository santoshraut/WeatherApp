package com.examples.weatherapp.models;

public class WeatherDataModel {

    String country;
    int year, month;
    float Tmin, Tmax, Rainfall;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getTmin() {
        return Tmin;
    }

    public void setTmin(float tmin) {
        Tmin = tmin;
    }

    public float getTmax() {
        return Tmax;
    }

    public void setTmax(float tmax) {
        Tmax = tmax;
    }

    public float getRainfall() {
        return Rainfall;
    }

    public void setRainfall(float rainfall) {
        Rainfall = rainfall;
    }
}
