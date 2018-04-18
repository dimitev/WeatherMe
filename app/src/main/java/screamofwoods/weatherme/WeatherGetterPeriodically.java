package screamofwoods.weatherme;

import android.os.Handler;
import android.util.Log;

//This is used in order to call the AsyncTask that updates all forecasts
//This thread executes every 15 minutes or if the primary city is changed
public class WeatherGetterPeriodically extends Thread {
    private final Handler handler = new Handler();
    private CityInfo city;

    public WeatherGetterPeriodically(CityInfo city){
        this.city = city;
    }
    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            //Used to start the AsyncTask and pass parameters to it
            handler.post(new Runnable() {
                public void run() {
                    Log.e("Updating periodically", city.getName());
                    new UpdateForecastAsync().execute(city);//starting the asynctask
                }
            });
            try {
                Thread.currentThread().sleep(1000 * 60 * 15); //sleep the thread for 15 minutes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();//This is executed when the primary city is changed
                break;
            }
        }
    }

}
