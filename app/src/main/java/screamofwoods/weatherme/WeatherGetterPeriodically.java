package screamofwoods.weatherme;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
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


//public class WeatherGetterPeriodically extends Thread {
//    private final Handler handler = new Handler();
//    private CityInfo city;
//
//    public WeatherGetterPeriodically(CityInfo city){
//        this.city = city;
//    }
//    @Override
//    public void run() {
//        while(!Thread.currentThread().isInterrupted()) {
//            //Used to start the AsyncTask and pass parameters to it
//            handler.post(new Runnable() {
//                public void run() {
//                    Log.e("Updating periodically", city.getName());
//                    new UpdateForecastAsync().execute(city);//starting the asynctask
//                }
//            });
//            try {
//                Thread.currentThread().sleep(1000 * 60 * 15); //sleep the thread for 15 minutes
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();//This is executed when the primary city is changed
//                break;
//            }
//        }
//    }
//
//}
