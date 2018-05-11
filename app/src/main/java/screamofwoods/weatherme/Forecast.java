package screamofwoods.weatherme;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cz.msebera.android.httpclient.Header;

public class Forecast implements Serializable{
    private static final long serialVersionUID = 2;
    private static final String BASE_URL = "http://api.apixu.com/v1/forecast.json";
    private static final String API_KEY = "9a86c637d9e14547890131921181005";
    transient private SyncHttpClient syncHttpClient;
    transient private RequestParams requestParams;

    @Override
    public String toString(){
        return "Forecast[BASE_URL=" + BASE_URL + ", API_KEY=" + API_KEY + "]";
    }

    private int getCurrentHour(){
        DateTime date = new DateTime();
        int currentHour = date.getHourOfDay();
        if(date.getMinuteOfHour() >= 30){
            if(currentHour == 23) {
                currentHour = 0;
            }else {
                ++currentHour;
            }
        }
        return currentHour;
    }
    public void getForecast(final CityInfo city){
        syncHttpClient = new SyncHttpClient();
        requestParams = new RequestParams();
        requestParams.add("key", API_KEY);
        requestParams.add("days", "5");
        if(!(city.getName().isEmpty())) {
            requestParams.add("q", city.getName());
        } else {
            requestParams.add("q", Float.toString(city.getLat()) + "," + Float.toString(city.getLon()));
        }
        syncHttpClient.get(BASE_URL, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                getMomentForecast(city, response);
                getHourlyForecast(city, response);
                getFiveDayForecast(city, response);
            }
        });
    }

    //Update the current weather for a city
    private void getMomentForecast(CityInfo city, JSONObject response)
    {
        int currentHour = getCurrentHour();
        try {
            if(city.getName().isEmpty()){
                city.setName(response.getJSONObject("location").getString("name"));
            }
            city.setLon((float) response.getJSONObject("location").getDouble("lat"));
            city.setLat((float) response.getJSONObject("location").getDouble("lon"));
            city.setRegion(response.getJSONObject("location").getString("region"));
            city.setCountry(response.getJSONObject("location").getString("country"));
            city.setLastUpdated(response.getJSONObject("current").getString("last_updated"));
            city.setAtmPressure((float) response.getJSONObject("current").getDouble("pressure_mb"));
            city.setChanceOfRain(response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONArray("hour").optJSONObject(currentHour).getInt("chance_of_rain"));
            city.setChanceOfSnow(response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONArray("hour").optJSONObject(currentHour).getInt("chance_of_snow"));
            city.setHumidity(response.getJSONObject("current").getInt("humidity"));
            city.setUvIndex((float) response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONObject("day").getDouble("uv"));
            city.setWeatherCondition(response.getJSONObject("current").getJSONObject("condition").getString("text"));
            city.setWindDirection(response.getJSONObject("current").getString("wind_dir"));
            city.setCloudCoverage(response.getJSONObject("current").getInt("cloud"));
            int isDay = response.getJSONObject("current").getInt("is_day");
            if(1 == isDay){
                city.setIsDay(true);
            } else {
                city.setIsDay(false);
            }
            if(city.getIsMetric()){
                city.setCurrentTemperature((float) response.getJSONObject("current").getDouble("temp_c"));
                city.setWindSpeed((float) response.getJSONObject("current").getDouble("wind_kph"));
                city.setMaximumTemperature((float) response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONObject("day").getDouble("maxtemp_c"));
                city.setMinimumTemperature((float) response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONObject("day").getDouble("mintemp_c"));
            } else {
                city.setCurrentTemperature((float) response.getJSONObject("current").getDouble("temp_f"));
                city.setWindSpeed((float) response.getJSONObject("current").getDouble("wind_mph"));
                city.setMaximumTemperature((float) response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONObject("day").getDouble("maxtemp_f"));
                city.setMinimumTemperature((float) response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONObject("day").getDouble("mintemp_f"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getHourlyForecast(CityInfo city, JSONObject response){
        JSONArray dayOneJSON, dayTwoJSON;
        try {
            dayOneJSON = response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONArray("hour");
            dayTwoJSON = response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(1).getJSONArray("hour");
            int count;
            int i;
            if(city.getIsMetric()){
                for(i = getCurrentHour(), count = 0; i < 24; i++, count++){
                    city.hourly[count].setCurrentTemperature((float) dayOneJSON.optJSONObject(i).getDouble("temp_c"));
                    city.hourly[count].setRain(dayOneJSON.optJSONObject(i).getInt("chance_of_rain"));
                    city.hourly[count].setWeather(dayOneJSON.optJSONObject(i).getJSONObject("condition").getString("text"));
                    city.hourly[count].setHour(dayOneJSON.optJSONObject(i).getString("time"));
                    Log.e("Temp",  "Hour " + i + " " + city.hourly[count].getCurrentTemperature());
                }
                if(count < 24){
                    //for(i = 0; i < 24 - count; i++, count++){
                    for(i = 0; count<24; i++, count++){
                        city.hourly[count].setCurrentTemperature((float) dayTwoJSON.optJSONObject(i).getDouble("temp_c"));
                        city.hourly[count].setRain(dayTwoJSON.optJSONObject(i).getInt("chance_of_rain"));
                        city.hourly[count].setWeather(dayTwoJSON.optJSONObject(i).getJSONObject("condition").getString("text"));
                        city.hourly[count].setHour(dayTwoJSON.optJSONObject(i).getString("time"));
                        //Log.e("Temp",  "Hour " + count + " " + city.hourly[count].getCurrentTemperature());
                    }
                }
            } else {
                for(i = getCurrentHour(), count = 0; i < 24; i++, count++){
                    city.hourly[count].setCurrentTemperature((float) dayOneJSON.optJSONObject(i).getDouble("temp_f"));
                    city.hourly[count].setRain(dayOneJSON.optJSONObject(i).getInt("chance_of_rain"));
                    city.hourly[count].setWeather(dayOneJSON.optJSONObject(i).getJSONObject("condition").getString("text"));
                    city.hourly[count].setHour(dayOneJSON.optJSONObject(i).getString("time"));
                }
                if(count < 24){
                    for(i = 0; i < 24 - count; i++, count++){
                        city.hourly[count].setCurrentTemperature((float) dayTwoJSON.optJSONObject(i).getDouble("temp_f"));
                        city.hourly[count].setRain(dayTwoJSON.optJSONObject(i).getInt("chance_of_rain"));
                        city.hourly[count].setWeather(dayTwoJSON.optJSONObject(i).getJSONObject("condition").getString("text"));
                        city.hourly[count].setHour(dayTwoJSON.optJSONObject(i).getString("time"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getFiveDayForecast(final CityInfo city, JSONObject response){
        JSONArray dayJSON;
        try {
            dayJSON = response.getJSONObject("forecast").getJSONArray("forecastday");
            int i;
            if(city.getIsMetric()) {
                for (i = 0; i < 5; i++) {
                    city.fiveDay[i].setCondition(dayJSON.optJSONObject(i).getJSONObject("day").getJSONObject("condition").getString("text"));
                    city.fiveDay[i].setDate(dayJSON.optJSONObject(i).getString("date"));
                    if(city.getIsMetric()){
                        city.fiveDay[i].setMinTemp((float) dayJSON.optJSONObject(i).getJSONObject("day").getDouble("mintemp_c"));
                        city.fiveDay[i].setMaxTemp((float) dayJSON.optJSONObject(i).getJSONObject("day").getDouble("maxtemp_c"));
                    } else {
                        city.fiveDay[i].setMinTemp((float) dayJSON.optJSONObject(i).getJSONObject("day").getDouble("mintemp_f"));
                        city.fiveDay[i].setMaxTemp((float) dayJSON.optJSONObject(i).getJSONObject("day").getDouble("maxtemp_f"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}