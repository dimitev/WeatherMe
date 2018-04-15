package screamofwoods.weatherme;

import android.os.AsyncTask;
import android.util.Log;

//Async task to make internet calls away from the main thread
//Updates all forecasts - current and hourly
public class UpdateForecastAsync extends AsyncTask<CityInfo, Void, Void> {
    @Override
    protected Void doInBackground(CityInfo... city) {
        //Connects to the REST api and updates the fields
        city[0].forecast.getMomentForecast();
        city[0].forecast.getHourlyForecast();
        return null;
    }
}
