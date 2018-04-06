package screamofwoods.weatherme;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class Forecast {
    private static final String BASE_URL = "http://api.apixu.com/v1/forecast.json";
    private static final String API_KEY = "9593a63a2df64b31bfe183434180204";
    private CityInfo city;
    private AsyncHttpClient asyncHttpClient;
    private RequestParams requestParams;
    private TextView tv;

    public Forecast(CityInfo city, TextView tv) {
        this.city = city;
        asyncHttpClient = new AsyncHttpClient();
        requestParams = new RequestParams();
        requestParams.add("key", API_KEY);
        this.tv = tv;
    }

    //Update the current weather for a city
    public void getMomentForecast()
    {
        if(!(city.getName().isEmpty())) {
            requestParams.add("q", city.getName());
        } else {
            requestParams.add("q", Float.toString(city.getLat()) + "," + Float.toString(city.getLon()));
        }
        DateTime date = new DateTime();
        String currentHour = Integer.toString(date.getHourOfDay());
        requestParams.add("hour", currentHour);
        asyncHttpClient.get(BASE_URL, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                super.onSuccess(statusCode, headers, response);
                try {
                    if((city.getName() != null) || !(city.getName().isEmpty())){
                        city.setName(response.getJSONObject("location").getString("name"));
                    }
                    city.setLastUpdated(response.getJSONObject("current").getString("last_updated"));
                    city.setAtmPressure((float) response.getJSONObject("current").getDouble("pressure_mb"));
                    city.setChanceOfRain(response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONArray("hour").optJSONObject(0).getInt("chance_of_rain"));
                    city.setHumidity(response.getJSONObject("current").getInt("humidity"));
                    city.setUvIndex((float) response.getJSONObject("forecast").getJSONArray("forecastday").optJSONObject(0).getJSONObject("day").getDouble("uv"));
                    city.setWeatherCondition(response.getJSONObject("current").getJSONObject("condition").getString("text"));
                    city.setWindDirection(response.getJSONObject("current").getString("wind_dir"));
                    city.setCloudCoverage(response.getJSONObject("current").getInt("cloud"));
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

                    tv.setText(city.getName()); //Used for debugging
                    //tv.setText(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONArray(0).optJSONArray(0).getJSONObject(0).getString("chance_of_rain"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
