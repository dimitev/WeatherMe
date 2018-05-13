package screamofwoods.weatherme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

//Helper thread to execute the AsyncTask for updating just once
//This is needed in order to make internet calls out of the main thread
public class WeatherGetterOnce extends Thread {
    private Handler handler = new Handler();
    private CityInfo city;
    private Context appContext;

    public WeatherGetterOnce(CityInfo city, Context appContext){
        this.appContext = appContext;
        this.city = city;
    }

    @Override
    public void run() {
        //check for internet connection
        handler.post(new Runnable() {
            public void run() {
                if(isOnline()) {
                    new UpdateForecastAsync().execute(city);
                } else {
                    Toast.makeText(appContext, "You're offline!", Toast.LENGTH_LONG).show();
                    MainActivity.binding.currentContent.swiperefresh.setRefreshing(false);
                    MainActivity.binding.hourlyContent.swiperefresh.setRefreshing(false);
                    MainActivity.binding.fiveDayContent.swiperefresh.setRefreshing(false);
                }
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
