package screamofwoods.weatherme;

import android.os.Handler;

//Helper thread to execute the AsyncTask for updating just once
//This is needed in order to make internet calls out of the main thread
public class WeatherGetterOnce extends Thread {
    private Handler handler = new Handler();
    private CityInfo city;

    public WeatherGetterOnce(CityInfo city){
        this.city = city;
    }

    @Override
    public void run() {
        handler.post(new Runnable() {
            public void run() {
                new UpdateForecastAsync().execute(city);
            }
        });
    }
}
