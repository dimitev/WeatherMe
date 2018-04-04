package screamofwoods.weatherme;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.TextView;
        import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    TextView txtData;
    //WeatherApiCall weatherApiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtData = findViewById(R.id.txtData);
        CityInfo c = new CityInfo("Sofia", (float)42.68, (float)23.32, true, txtData);
        //weatherApiCall = new WeatherApiCall(txtData);
        //weatherApiCall.GetDailyForecast("Elena");
    }
}