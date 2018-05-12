package screamofwoods.weatherme;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static screamofwoods.weatherme.MainActivity.getAppContext;

public class AddCitiesActivity extends AppCompatActivity {

    public static RecyclerView mRecyclerView;//for the list of cities
    public static RecyclerView.Adapter mAdapter;//for the list of cities
    private RecyclerView.LayoutManager mLayoutManager;//for the list of cities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_cities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//shows the toolbar arrow and hamburger menu
        getSupportActionBar().setHomeButtonEnabled(true);//shows the toolbar arrow and hamburger menu
        SearchForCity.citiesFound.clear();
        //SearchForCity.citiesFound.add(new CityInfo("Varna", 0, 0, true));
        prepareRecycler();
        Button search = findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.textView);
                SearchForCity.citiesFound.clear();
                //SearchForCity.citiesFound.add(new CityInfo("Varna",0,0,true));
                SearchForCity newCity = new SearchForCity(tv.getText().toString());
                newCity.findCity();
                mAdapter.notifyDataSetChanged();//updates the drawer
            }
        });
//        CityInfo d =new CityInfo("Varna",0,0,true);
//        new WeatherGetterOnce(d).start();
//        UserCities.add(d);
    }

    private void prepareRecycler() {
        mRecyclerView = findViewById(R.id.found_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.e("AddCitiesActivity", "Prepare Recycler Success coming here");
        // specify an adapter
        mAdapter = new MyAdapter(SearchForCity.citiesFound);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            //Change selected city from right hand side list
            @Override
            public void onClick(View view, int position) {
                CityInfo newCity = SearchForCity.citiesFound.get(position);
                for (CityInfo s : MainActivity.UserCities) {
                    if (s.getName().equals((newCity.getName().split(",")[0])) && s.getCountry().equals(newCity.getCountry())) {
                        Toast.makeText(getAppContext(), "City already added", Toast.LENGTH_SHORT).show();
                        //Log.e("Add a city","gets in the for");
                        return;
                    }
                    //Log.e("coutries","'"+nc.getCountry()+"'  '"+s.getCountry()+"'");
                }
                newCity = new CityInfo(newCity.getName().split(",")[0], 0, 0, true);
                new WeatherGetterOnce(newCity, getApplicationContext()).start();
                MainActivity.UserCities.add(newCity);
                MainActivity.setCurrent(newCity);
                SearchForCity.citiesFound.clear();
                MainActivity.mAdapter.notifyDataSetChanged();
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }
    @Override
    protected void onStop() {
        SearchForCity.citiesFound.clear();
        super.onStop();
    }
}
