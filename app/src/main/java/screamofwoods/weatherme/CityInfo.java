package screamofwoods.weatherme;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class CityInfo extends BaseObservable {
    //private SearchForCity searchForCity;
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
        forecast = new Forecast(this);
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
        forecast = new Forecast(this);
        //forecast.getHourlyForecast();
        notifyPropertyChanged(BR._all);
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
        MainActivity.mAdapter.notifyDataSetChanged();//updates the drawer to deal with the async
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
