package screamofwoods.weatherme;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;

//TODO Serialize Forecast and SearchForCity classes -> done but is it working
//TODO @Override toString() method in both of the above classes -> done but is it working
public class CityInfo extends BaseObservable implements Serializable {
    private static final long serialVersionUID = 1;
    //transient vars are not due to serialization
    transient private SearchForCity searchForCity;
    public Forecast forecast;
    private String lastUpdated;
    private String[] weatherConditionHourly;
    private float[] temperatureHourly;
    private int[] chanceOfRainHourly;
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
    private boolean isMetric;
    private boolean isDay;

    public CityInfo() {

    }

    public CityInfo(String name, float lat, float lon, boolean isMetric) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        weatherConditionHourly = new String[24];
        temperatureHourly = new float[24];
        chanceOfRainHourly = new int[24];
        this.isMetric = isMetric;
        forecast = new Forecast();
        //forecast.getHourlyForecast();
        notifyPropertyChanged(BR._all);
    }

    public CityInfo(float lat, float lon, boolean isMetric) {
        this.lat = lat;
        this.lon = lon;
        weatherConditionHourly = new String[24];
        temperatureHourly = new float[24];
        chanceOfRainHourly = new int[24];
        this.isMetric = isMetric;
        forecast = new Forecast();
        //forecast.getHourlyForecast();
        notifyPropertyChanged(BR._all);
    }

    //In order to serialize the object properly
    @Override
    public String toString() {
        return "CityInfo[forecast=" + forecast.toString() + "lastUpdated=" + lastUpdated + ", weatherConditionHourly=" + weatherConditionHourly.toString() +
                ", temperatureHourly=" + temperatureHourly.toString() + ", chanceOfRainHourly=" + chanceOfRainHourly.toString() +
                ", name=" + name + ", region=" + region + ", country=" + country +
                ", weatherCondition=" + weatherCondition + ", windDirection=" + windDirection +
                ", cloudCoverage=" + cloudCoverage + ", currentTemperature=" + currentTemperature +
                ", minimumTemperature=" + minimumTemperature + ", maximumTemperature=" + maximumTemperature +
                ", windSpeed=" + windSpeed + ", atmPressure=" + atmPressure +
                ", lat=" + lat + ", lon=" + lon + ", uvIndex=" + uvIndex +
                ", humidity=" + humidity + ", chanceOfRain=" + chanceOfRain + ", isMetric=" + isMetric + ", isDay=" + isDay +
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

    public void setWeatherConditionHourly(String[] weatherConditionHourly) {
        this.weatherConditionHourly = weatherConditionHourly;
        notifyPropertyChanged(BR._all);
    }

    public void setTemperatureHourly(float[] temperatureHourly) {
        this.temperatureHourly = temperatureHourly;
        notifyPropertyChanged(BR._all);
    }

    public void setChanceOfRainHourly(int[] chanceOfRainHourly) {
        this.chanceOfRainHourly = chanceOfRainHourly;
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

    @Bindable
    public boolean getIsDay() {
        return isDay;
    }

    @Bindable
    public String getLastUpdated() {
        return lastUpdated;
    }

    @Bindable
    public String[] getWeatherConditionHourly() {
        return weatherConditionHourly;
    }

    @Bindable
    public float[] getTemperatureHourly() {
        return temperatureHourly;
    }

    @Bindable
    public int[] getChanceOfRainHourly() {
        return chanceOfRainHourly;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public Boolean getIsMetric() {
        return isMetric;
    }

    @Bindable
    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherConditionImage(String v) {
    }

    @Bindable
    public Drawable getWeatherConditionImage() {
        //return MainActivity.getAppContext().getResources().getDrawable(R.drawable.cloud);
        String w = getWeatherCondition();
        if (w == null) return null;
        //return "" + R.drawable.fog;/*
        Log.d("GetImage", w);
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

                else//clear
                    return null;
            }
        }

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
