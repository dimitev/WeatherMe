package screamofwoods.weatherme;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

public class Hourly extends BaseObservable implements Serializable {
    private float minimumTemperature;
    private float maximumTemperature;
    private float rain;
    private String weather;

    public Hourly() {
    }

    public Hourly(float min, float max, String weather, float rain) {
        this.setMaximumTemperature(max);
        this.setMinimumTemperature(min);
        this.setRain(rain);
        this.setWeather(weather);
    }

    //In order to serialize the object properly
    @Override
    public String toString() {
        return "Hourly[minimumTemperature=" + minimumTemperature + ", maximumTemperature=" + maximumTemperature + ", weatherCondition=" + weather +
                ", rain=" + rain + "]";
    }

    @Bindable
    public float getMinimumTemperature() {
        return minimumTemperature;
    }

    @Bindable
    public float getMaximumTemperature() {
        return maximumTemperature;
    }

    @Bindable
    public float getRain() {
        return rain;
    }

    @Bindable
    public String getWeather() {
        return weather;
    }

    public void setMinimumTemperature(Float minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public void setMaximumTemperature(Float maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    public void setRain(Float rain) {
        this.rain = rain;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
