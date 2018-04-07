package screamofwoods.weatherme;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchForCity {
    private static final String BASE_URL = "https://api.apixu.com/v1/search.json";
    private static final String API_KEY = "9593a63a2df64b31bfe183434180204";
    private AsyncHttpClient asyncHttpClient;
    private RequestParams requestParams;
    private String cityName;
    private float lat;
    private float lon;

    public SearchForCity(){

    }

    public void findCityByName(, String name){
        requestParams.add("q", name);
        asyncHttpClient.get(BASE_URL, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                cityInfo.setName();
            }
        });
    }
}
