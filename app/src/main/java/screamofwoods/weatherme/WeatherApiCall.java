package screamofwoods.weatherme;

import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class WeatherApiCall {
    private final String API_TOKEN = "4a3baf957b67cd5cef2b945db0f21bc1";
    private final String BASE_ADDRESS = "http://api.openweathermap.org/data/2.5/";
    private AsyncHttpClient asyncHttpClient;
    private RequestParams requestParams;
    private TextView txtWeatherData;

    public WeatherApiCall(TextView txtWeatherData) {
        asyncHttpClient = new AsyncHttpClient();
        requestParams = new RequestParams();
        this.txtWeatherData = txtWeatherData;
        requestParams.add("appid", API_TOKEN);
    }

    //Gets the current weather in json manner uses TEXT VIEW to post data - should be changed later

    //Get daily forecast about a city
    public void GetDailyForecast(String city) {
        requestParams.add("q", city);
        asyncHttpClient.get(makeUrl("weather"), requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                txtWeatherData.setText(response.toString());
            }
        });
    }
    //Daily forecast based on gps coordinates
    public void GetDailyForecast(double lat, double lon) {
        requestParams.add("lat", Double.toString(lat));
        requestParams.add("lon", Double.toString(lon));
        asyncHttpClient.get(makeUrl("weather"), requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                txtWeatherData.setText(response.toString());
            }
        });
    }
    //Daily forecast based on cityID provided by the weather api -> city.list.json
    public void GetDailyForecast(int cityID) {
        requestParams.add("id", Integer.toString(cityID));
        asyncHttpClient.get(makeUrl("weather"), requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                txtWeatherData.setText(response.toString());
            }
        });
    }

    //In case all calls to the api widgets are made in this class -> sticks the widget name to the url
    private String makeUrl(String weatherApiWidget) {
        return BASE_ADDRESS + weatherApiWidget;
    }
}