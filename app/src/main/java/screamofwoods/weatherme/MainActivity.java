package screamofwoods.weatherme;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txtData;
    //WeatherApiCall weatherApiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtData = findViewById(R.id.txtData);
        CityInfo c = new CityInfo("Sofia", (float)25.25, (float)55.28, true, txtData);
        //weatherApiCall = new WeatherApiCall(txtData);
        //weatherApiCall.GetDailyForecast("Elena");
    }
}