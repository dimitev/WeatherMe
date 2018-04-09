package screamofwoods.weatherme;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchForCity {
    public static List<CityInfo> citiesFound = null;
    private static final String BASE_URL = "http://api.apixu.com/v1/search.json";
    private static final String API_KEY = "9593a63a2df64b31bfe183434180204";
    private AsyncHttpClient asyncHttpClient;
    private RequestParams requestParams;
    private String name = "";
    private float lat;
    private float lon;

    public SearchForCity(String name){
        this.name = name;
    }

    public SearchForCity(float lat, float lon){
        this.lat = lat;
        this.lon = lon;
    }

    //Searches city either by name or provided coordinates.
    //Fills the public list with all the found cities
    //!!!The list MUST BE cleared upon user's choice of city!!!
    public void findCity(){
        if(name.isEmpty()){
            requestParams.add("q", name);
        } else {
            requestParams.add("q", lat + "," + lon);
        }

        asyncHttpClient.get(BASE_URL, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                CityInfo c = new CityInfo();
                try {
                    JSONArray allCities = response.getJSONArray(0);
                    for(int i = 0; i < allCities.length(); i++){
                        c.setName(allCities.optJSONObject(i).getString("name"));
                        c.setRegion(allCities.optJSONObject(i).getString("region"));
                        c.setCountry(allCities.optJSONObject(i).getString("country"));
                        citiesFound.add(c);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
