package screamofwoods.weatherme;

import android.os.AsyncTask;
import android.util.Log;

public class UpdateForecastAsync extends AsyncTask<CityInfo, Void, Void> {
    @Override
    protected Void doInBackground(CityInfo... city) {
        Log.e("Executing update on", city[0].getName());
        city[0].forecast.getMomentForecast();
        city[0].forecast.getHourlyForecast();
        return null;
    }
}
