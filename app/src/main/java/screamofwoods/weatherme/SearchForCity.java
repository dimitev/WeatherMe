package screamofwoods.weatherme;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchForCity {
    public static List<CityInfo> citiesFound = new ArrayList<CityInfo>();
    private static final String BASE_URL = "http://api.apixu.com/v1/search.json";
    private static final String API_KEY = "9a86c637d9e14547890131921181005";
    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private RequestParams requestParams = new RequestParams();
    private String name = "";
    private float lat;
    private float lon;

    public SearchForCity(String name) {
        this.name = name;
        requestParams.add("key", API_KEY);
    }

    public SearchForCity(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    //Searches city either by name or provided coordinates.
    //Fills the public list with all the found cities
    //!!!The list MUST BE cleared upon user's choice of city!!!
    public void findCity() {

        if (!name.isEmpty()) {
            requestParams.add("q", name);
            Log.d("Add a city ", "has name");
        } else {
            requestParams.add("q", lat + "," + lon);
            Log.d("Add a city ", "searching by loc");
        }

        asyncHttpClient.get(BASE_URL, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                CityInfo c;
                try {
                    //JSONArray allCities = response.getJSONArray();
                    JSONObject current;
                    for (int i = 0; i < response.length(); i++) {
                        current=response.getJSONObject(i);
                        c = new CityInfo();
                        c.setName(current.getString("name"));
                        c.setRegion(current.getString("region"));
                        c.setCountry(current.getString("country"));
                        citiesFound.add(c);
                    }
                    AddCitiesActivity.mAdapter.notifyDataSetChanged();//updates the drawer
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
