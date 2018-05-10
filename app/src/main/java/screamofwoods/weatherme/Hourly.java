package screamofwoods.weatherme;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

public class Hourly extends BaseObservable implements Serializable {
    private float currentTemperature;
    private int rain;
    private String weather;
    private String hour;

    public Hourly() {

    }

    public Hourly(float cur, int rain, String hour, String weather) {
        this.setCurrentTemperature(cur);
        this.setRain(rain);
        this.setHour(hour);
        this.setWeather(weather);
    }

    //In order to serialize the object properly
    @Override
    public String toString() {
        return "Hourly[currentTemperature=" + currentTemperature + ", hour=" + hour + ", weatherCondition=" + weather +
                ", rain=" + rain + "]";
    }

    @Bindable
    public float getCurrentTemperature() {
        return currentTemperature;
    }

    @Bindable
    public String getHour() {
        return hour;
    }

    @Bindable
    public float getRain() {
        return rain;
    }

    @Bindable
    public String getWeather() {
        return weather;
    }

    public void setCurrentTemperature(Float currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setRain(int rain) {
        this.rain = rain;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
