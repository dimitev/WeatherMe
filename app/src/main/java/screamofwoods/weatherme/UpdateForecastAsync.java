package screamofwoods.weatherme;

import android.os.AsyncTask;
import android.util.Log;

//Async task to make internet calls away from the main thread
//Updates all forecasts - current and hourly
public class UpdateForecastAsync extends AsyncTask<CityInfo, Void, Void> {
    @Override
    protected Void doInBackground(CityInfo... city) {
        //Connects to the REST api and updates the fields
        if(city[0] != null){
            Log.e("AsyncTask", "Updating: " + city[0].getName());
            city[0].forecast.getMomentForecast(city[0]);
            city[0].forecast.getHourlyForecast(city[0]);
            city[0].forecast.getFiveDayForecast(city[0]);
        }
        return null;
    }
}
