package screamofwoods.weatherme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class AddCitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//shows the toolbar arrow and hamburger menu
        getSupportActionBar().setHomeButtonEnabled(true);//shows the toolbar arrow and hamburger menu
        CityInfo d =new CityInfo("Varna",0,0,true);
        new WeatherGetterOnce(d).start();
        MainActivity.UserCities.add(d);
    }

}
