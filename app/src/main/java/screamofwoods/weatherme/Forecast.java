package screamofwoods.weatherme;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

    public Forecast(CityInfo city) {
        this.city = city;
        asyncHttpClient = new AsyncHttpClient();
        requestParams = new RequestParams();
        requestParams.add("key", API_KEY);
    }

    public void getMomentForecast()
    {
        if(city.getName() != null)
        {
            requestParams.add("q", city.getName());
            DateFormat df = new SimpleDateFormat("HH");
            Date d = new Date();
            requestParams.add("q", df.format(d));
            asyncHttpClient.get(BASE_URL, requestParams, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    super.onSuccess(statusCode, headers, response);
                    try {
                        city.setLastUpdated((Date) response.getJSONObject("current").get("last_updated"));
                        city.setAtmPressure((float) response.getJSONObject("current").getDouble("pressure_mb"));
                        city.setChanceOfRain(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONArray(0).getJSONArray());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            })
        }
    }

}
