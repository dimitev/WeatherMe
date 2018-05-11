package screamofwoods.weatherme;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

import static screamofwoods.weatherme.CityInfo.getWeatherConditionImage;

public class Hourly extends BaseObservable implements Serializable {
    private static final long serialVersionUID = 3;
    private float currentTemperature=0;
    private int rain=0;
    private String weather="";
    private String hour=" x";

    public Hourly() {

    }

    public void Copy(Hourly n)
    {
        this.currentTemperature=n.currentTemperature;
        this.rain=n.rain;
        this.weather=n.weather;
        this.hour=n.hour;
        notifyPropertyChanged(BR._all);
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
        return !hour.equals("")&&hour!=null ? hour.split(" ")[1]:"-";
    }

    @Bindable
    public float getRain() {
        return rain;
    }

    @Bindable
    public Drawable getWeather() {
        return getWeatherConditionImage(this.weather);
    }

    public void setCurrentTemperature(Float currentTemperature) {
        this.currentTemperature = currentTemperature;
        notifyPropertyChanged(BR._all);
    }

    public void setHour(String hour) {
        this.hour = hour;
        notifyPropertyChanged(BR._all);
    }

    public void setRain(int rain) {
        this.rain = rain;
        notifyPropertyChanged(BR._all);
    }

    public void setWeather(String weather) {
        this.weather = weather;
        notifyPropertyChanged(BR._all);
    }
}
