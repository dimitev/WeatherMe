package screamofwoods.weatherme;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.joda.time.DateTime;


public class MainActivity extends AppCompatActivity {

    TextView txtData;
    //WeatherApiCall weatherApiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtData = findViewById(R.id.txtData);
        CityInfo c = new CityInfo((float)25.25, (float)55.28, true, txtData);
        DateTime dt = new DateTime();
        String hour = Integer.toString(dt.getHourOfDay());
        Toast.makeText(this, "Current hour: " + hour, Toast.LENGTH_LONG).show();
        //weatherApiCall = new WeatherApiCall(txtData);
        //weatherApiCall.GetDailyForecast("Elena");
    }
}