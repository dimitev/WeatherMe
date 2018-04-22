package screamofwoods.weatherme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static screamofwoods.weatherme.MainActivity.binding;
import static screamofwoods.weatherme.MainActivity.c;

public class AddCitiesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;//for the list of cities
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
        SearchForCity.citiesFound.add(new CityInfo("Varna", 0, 0, true));
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
        //CityInfo d =new CityInfo("Varna",0,0,true);
        //new WeatherGetterOnce(d).start();
        //UserCities.add(d);
    }

    private void prepareRecycler() {
        mRecyclerView = (RecyclerView) findViewById(R.id.found_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new MyAdapter(SearchForCity.citiesFound);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            //Change selected city from right hand side list
            @Override
            public void onClick(View view, int position) {
                CityInfo city = SearchForCity.citiesFound.get(position);
                // Toast.makeText(getApplicationContext(), city.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                String name[]=city.getName().split(",");
                city=new CityInfo(name[0],0,0,true);
                new WeatherGetterOnce(city).start();
                MainActivity.UserCities.add(city);
                MainActivity.setCurrent(city);
                SearchForCity.citiesFound.clear();
                MainActivity.mAdapter.notifyDataSetChanged();
                //Intent intent = new Intent(AddCitiesActivity.this, MainActivity.class);
                //startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {

               /* extraCity = UserCities.get(position);
                //Toast.makeText(getApplicationContext(), extraCity.getName() + " settings!", Toast.LENGTH_SHORT).show();
                registerForContextMenu(view);
                openContextMenu(view);
                unregisterForContextMenu(view);*/
            }
        }));
    }

}
