package screamofwoods.weatherme;

import android.os.Handler;

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
