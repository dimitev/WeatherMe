package screamofwoods.weatherme;

import android.widget.TextView;

import java.util.Date;

public class CityInfo {
    //private SearchForCity searchForCity;
    private Forecast forecast;
    //private UVRays uvRays;
    //private Pollution pollution;
    private Date lastUpdated;
    private String[] weatherConditionHourly;
    private float[] tempetureHourly;
    private int[] chanceOfRainHourly;
    private String name;
    private String weatherCondition;
    private String windDirection; //Compass
    private double pollutionIndex;
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

    public CityInfo(){

    }
    public CityInfo(String name, float lat, float lon, boolean isMetric, TextView tv){
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        weatherConditionHourly = new String[24];
        tempetureHourly = new float[24];
        chanceOfRainHourly = new int[24];
        this.isMetric = isMetric;
        forecast = new Forecast(this, tv);
        forecast.getMomentForecast();
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public void setPollutionIndex(double pollutionIndex) {
        this.pollutionIndex = pollutionIndex;
    }

    public void setCurrentTemperatureTemperature(float currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public float getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(float minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public float getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(float maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setAtmPressure(float atmPressure) {
        this.atmPressure = atmPressure;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setUvIndex(float uvIndex) {
        this.uvIndex = uvIndex;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setChanceOfRain(int chanceOfRain) {
        this.chanceOfRain = chanceOfRain;
    }

    public void setIsMetric(boolean isMetric){
        this.isMetric = isMetric;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public String[] getWeatherConditionHourly() {
        return weatherConditionHourly;
    }

    public float[] getTempetureHourly() {
        return tempetureHourly;
    }

    public int[] getChanceOfRainHourly() {
        return chanceOfRainHourly;
    }

    public String getName() {
        return name;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public double getPollutionIndex() {
        return pollutionIndex;
    }

    public float getCurrentTemperature() {
        return currentTemperature;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public float getAtmPressure() {
        return atmPressure;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public float getUvIndex() {
        return uvIndex;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getChanceOfRain() {
        return chanceOfRain;
    }

}
