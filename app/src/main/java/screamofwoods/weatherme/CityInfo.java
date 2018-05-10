package screamofwoods.weatherme;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class CityInfo extends BaseObservable implements Serializable {
    private static final long serialVersionUID = 1;
    //transient vars are not due to serialization
    transient private SearchForCity searchForCity;
    public Forecast forecast;
    public Hourly[] hourly = new Hourly[24];
    private String lastUpdated;
    private String name = "";
    private String region = "";
    private String country = "";
    private String weatherCondition;
    private String windDirection; //Compass N, E, S, W, etc...
    private int cloudCoverage;
    private float currentTemperature;
    private float minimumTemperature;
    private float maximumTemperature;
    private float windSpeed;
    private float atmPressure;
    private float lat;
    private float lon;
    private float uvIndex;
    private int humidity;
    private int chanceOfRain;
    private int chanceOfSnow;
    private boolean isMetric;
    private boolean isDay;

    public CityInfo() {
        for (int i = 0; i < 24; i++)
            hourly[i] = new Hourly();
    }

    public void Copy(CityInfo n) {
        this.forecast = new Forecast();
        for (int i = 0; i < 24; i++)
            this.hourly[i].Copy(n.hourly[i]);
        this.lastUpdated = n.lastUpdated;
        this.name = n.name;
        this.region = n.region;
        this.country = n.country;
        this.weatherCondition = n.weatherCondition;
        this.windDirection = n.windDirection; //Compass N, E, S, W, etc...
        this.cloudCoverage = n.cloudCoverage;
        this.currentTemperature = n.currentTemperature;
        this.minimumTemperature = n.minimumTemperature;
        this.maximumTemperature = n.maximumTemperature;
        this.windSpeed = n.windSpeed;
        this.atmPressure = n.atmPressure;
        this.lat = n.lat;
        this.lon = n.lon;
        this.uvIndex = n.uvIndex;
        this.humidity = n.humidity;
        this.chanceOfRain = n.chanceOfRain;
        this.chanceOfSnow = n.chanceOfSnow;
        this.isMetric = n.isMetric;
        this.isDay = n.isDay;
        notifyPropertyChanged(BR._all);
    }

    public CityInfo(String name, float lat, float lon, boolean isMetric) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.isMetric = isMetric;
        forecast = new Forecast();
        //forecast.getHourlyForecast();
        notifyPropertyChanged(BR._all);
        for (int i = 0; i < 24; i++)
            hourly[i] = new Hourly();
    }

    public CityInfo(float lat, float lon, boolean isMetric) {
        this.lat = lat;
        this.lon = lon;
        this.isMetric = isMetric;
        forecast = new Forecast();
        //forecast.getHourlyForecast();
        for (int i = 0; i < 24; i++)
            hourly[i] = new Hourly();
        notifyPropertyChanged(BR._all);
    }

    //In order to serialize the object properly
    @Override
    public String toString() {
        return "CityInfo[forecast=" + forecast.toString() + "lastUpdated=" + lastUpdated + ", hourly=" + hourly.toString() +
                ", name=" + name + ", region=" + region + ", country=" + country +
                ", weatherCondition=" + weatherCondition + ", windDirection=" + windDirection +
                ", cloudCoverage=" + cloudCoverage + ", currentTemperature=" + currentTemperature +
                ", minimumTemperature=" + minimumTemperature + ", maximumTemperature=" + maximumTemperature +
                ", windSpeed=" + windSpeed + ", atmPressure=" + atmPressure +
                ", lat=" + lat + ", lon=" + lon + ", uvIndex=" + uvIndex +
                ", humidity=" + humidity + ", chanceOfRain=" + chanceOfRain + ", chanceOfSnow=" + chanceOfSnow + ", isMetric=" + isMetric + ", isDay=" + isDay +
                "]";
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
        notifyPropertyChanged(BR._all);
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR._all);
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
        notifyPropertyChanged(BR._all);
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
        notifyPropertyChanged(BR._all);
    }

    public void setCloudCoverage(int cloudCoverage) {
        this.cloudCoverage = cloudCoverage;
        notifyPropertyChanged(BR._all);
    }

    public void setCurrentTemperature(float currentTemperature) {
        this.currentTemperature = currentTemperature;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public float getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(float minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
        notifyPropertyChanged(BR._all);
    }

    @Bindable
    public float getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(float maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
        notifyPropertyChanged(BR._all);
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
        notifyPropertyChanged(BR._all);
    }

    public void setAtmPressure(float atmPressure) {
        this.atmPressure = atmPressure;
        notifyPropertyChanged(BR._all);
    }

    public void setLat(float lat) {
        this.lat = lat;
        notifyPropertyChanged(BR._all);
    }

    public void setLon(float lon) {
        this.lon = lon;
        notifyPropertyChanged(BR._all);
    }

    public void setUvIndex(float uvIndex) {
        this.uvIndex = uvIndex;
        notifyPropertyChanged(BR._all);
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
        notifyPropertyChanged(BR._all);
    }

    public void setChanceOfRain(int chanceOfRain) {
        this.chanceOfRain = chanceOfRain;
        notifyPropertyChanged(BR._all);
    }

    public void setIsMetric(boolean isMetric) {
        this.isMetric = isMetric;
        notifyPropertyChanged(BR._all);
    }

    public void setHourly(Hourly[] hourly) {
        this.hourly = hourly;
        notifyPropertyChanged(BR._all);
    }

    public void setRegion(String region) {
        this.region = region;
        notifyPropertyChanged(BR._all);
    }

    public void setCountry(String country) {
        this.country = country;
        notifyPropertyChanged(BR._all);
    }

    public void setIsDay(boolean day) {
        isDay = day;
        notifyPropertyChanged(BR._all);
    }

    public int getChanceOfSnow() {
        return chanceOfSnow;
    }

    public void setChanceOfSnow(int chanceOfSnow) {
        this.chanceOfSnow = chanceOfSnow;
    }

    @Bindable
    public boolean getIsDay() {
        return isDay;
    }

    @Bindable
    public String getLastUpdated() {
        return lastUpdated;
    }

    @Bindable
    public Hourly[] getHourly() {
        return hourly;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public boolean getIsMetric() {
        return isMetric;
    }

    @Bindable
    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherConditionImage(String v) {
    }

    public static Drawable getWeatherConditionImage(String w) {
        if (w == null) return null;
        //return "" + R.drawable.fog;/*
        //Log.d("GetImage", w);
        switch (w) {
            //cloud
            case "Cloudy":
            case "Overcast":
                return MainActivity.getAppContext().getResources().getDrawable(R.drawable.cloud);
            case "Partly cloudy":
                return MainActivity.getAppContext().getResources().getDrawable(R.drawable.cloudpart);
            //fog
            case "Mist":
            case "Fog":
            case "Freezing fog":
                return MainActivity.getAppContext().getResources().getDrawable(R.drawable.fog);
            case "Sunny":

                return MainActivity.getAppContext().getResources().getDrawable(R.drawable.sunny);
            case "Clear":

                return MainActivity.getAppContext().getResources().getDrawable(R.drawable.clear);
            //rain
            default: {
                //storm
                if (w.contains("storm") || w.contains("thunder"))
                    return MainActivity.getAppContext().getResources().getDrawable(R.drawable.storm);
                else if (w.contains("rain") || w.contains("pellets") || w.contains("sleet"))
                    return MainActivity.getAppContext().getResources().getDrawable(R.drawable.rain);
                    //snow
                else if (w.contains("snow"))
                    return MainActivity.getAppContext().getResources().getDrawable(R.drawable.snow);
            }
        }

        //clear
        return null;
    }


    @Bindable
    public Drawable getWeatherConditionImage() {
        return getWeatherConditionImage(getWeatherCondition());
    }

    @Bindable
    public String getWindDirection() {
        return windDirection;
    }

    @Bindable
    public int getCloudCoverage() {
        return cloudCoverage;
    }

    @Bindable
    public float getCurrentTemperature() {
        return currentTemperature;
    }

    @Bindable
    public float getWindSpeed() {
        return windSpeed;
    }

    @Bindable
    public float getAtmPressure() {
        return atmPressure;
    }

    @Bindable
    public float getLat() {
        return lat;
    }

    @Bindable
    public float getLon() {
        return lon;
    }

    @Bindable
    public float getUvIndex() {
        return uvIndex;
    }

    @Bindable
    public int getHumidity() {
        return humidity;
    }

    @Bindable
    public int getChanceOfRain() {
        return chanceOfRain;
    }

    @Bindable
    public String getRegion() {
        return region;
    }

    @Bindable
    public String getCountry() {
        return this.country;
    }
}
