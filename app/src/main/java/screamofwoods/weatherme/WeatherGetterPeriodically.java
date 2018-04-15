package screamofwoods.weatherme;

import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class WeatherGetterPeriodically extends Thread {
    private final Handler handler = new Handler();
    private Timer timer = new Timer();
    private CityInfo city;

    public WeatherGetterPeriodically(CityInfo city){
        this.city = city;
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            Log.e("This is called", "every minute");
            handler.post(new Runnable() {
                public void run() {
                    new UpdateForecastAsync().execute(city);
                }
            });
            try {
                Thread.currentThread().sleep(1000 * 60 * 15); //sleep the thread for 15 minutes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

}
