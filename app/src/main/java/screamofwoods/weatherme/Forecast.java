package screamofwoods.weatherme;

import android.support.v4.widget.SwipeRefreshLayout;

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
    private static final String API_KEY = "9593a63a2df64b31bfe183434180204";
    transient private SyncHttpClient syncHttpClient;
    transient private RequestParams requestParams;

    @Override
    public String toString(){
        return "Forecast[BASE_URL=" + BASE_URL + ", API_KEY=" + API_KEY + "]";
    }

    //Update the current weather for a city
    public void getMomentForecast(final CityInfo city)
    {
        syncHttpClient = new SyncHttpClient();
        requestParams = new RequestParams();
        requestParams.add("key", API_KEY);
        if(!(city.getName().isEmpty())) {
            requestParams.add("q", city.getName());
        } else {
            requestParams.add("q", Float.toString(city.getLat()) + "," + Float.toString(city.getLon()));
        }
        DateTime date = new DateTime();
        int currentHour = date.getHourOfDay();
        if(date.getMinuteOfHour() >= 30){
            if(currentHour == 23) {
                currentHour = 0;
            }else {
                currentHour++;
            }
        }
        requestParams.add("hour", Integer.toString(currentHour));
        syncHttpClient.get(BASE_URL, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                super.onSuccess(statusCode, headers, response);
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
                    city.setChanceOfRain(response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONArray("hour").optJSONObject(0).getInt("chance_of_rain"));
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
        });
        //SwipeRefreshLayout sr=findViewById(R.id.swiperefresh);
    }

    public void getHourlyForecast(final CityInfo city){
        syncHttpClient = new SyncHttpClient();
        requestParams = new RequestParams();
        requestParams.add("key", API_KEY);
        if(requestParams.has("hour")){
            requestParams.remove("hour");
        }
        if(!(city.getName().isEmpty())) {
            requestParams.add("q", city.getName());
        } else {
            requestParams.add("q", Float.toString(city.getLat()) + "," + Float.toString(city.getLon()));
        }
        syncHttpClient.get(BASE_URL, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray hourlyForecastJSON = null;
                String[] weatherConditionHourly = new String[24];
                float[] temperatureHourly = new float[24];
                int[] chanceOfRainHourly = new int[24];
                try {
                    hourlyForecastJSON = response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONArray("hour");
                    if(city.getIsMetric())
                    {
                        for(int i = 0; i < 24; i++){
                            temperatureHourly[i] = (float) hourlyForecastJSON.optJSONObject(i).getDouble("temp_c");
                            chanceOfRainHourly[i] = hourlyForecastJSON.optJSONObject(i).getInt("chance_of_rain");
                            weatherConditionHourly[i] = hourlyForecastJSON.optJSONObject(i).getJSONObject("condition").getString("text");
                        }
                    } else {
                        for(int i = 0; i < 24; i++){
                            temperatureHourly[i] = (float) hourlyForecastJSON.optJSONObject(i).getDouble("temp_f");
                            chanceOfRainHourly[i] = hourlyForecastJSON.optJSONObject(i).getInt("chance_of_rain");
                            weatherConditionHourly[i] = hourlyForecastJSON.optJSONObject(i).getJSONObject("condition").getString("text");
                        }
                    }
                    city.setTemperatureHourly(temperatureHourly);
                    city.setChanceOfRainHourly(chanceOfRainHourly);
                    city.setWeatherConditionHourly(weatherConditionHourly);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
