package screamofwoods.weatherme;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

//This is used in order to call the AsyncTask that updates all forecasts
//This thread executes every 15 minutes or if the primary city is changed

public class WeatherGetterPeriodically extends JobService {
    UpdateForecastAsync updateForecastAsync;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("JobService", "Executing Periodic job");
        updateForecastAsync = new UpdateForecastAsync();
        updateForecastAsync.execute(MainActivity.c);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("UpdateService", "System calling to stop the job here");
        if (updateForecastAsync != null)
            updateForecastAsync.cancel(true);
        return false;
    }
}