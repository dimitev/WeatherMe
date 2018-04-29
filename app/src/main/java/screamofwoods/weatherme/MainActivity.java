package screamofwoods.weatherme;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import screamofwoods.weatherme.databinding.ActivityMainBinding;

//TODO check internet connectivity in the threads for updates
//TODO check if the file doesn't exist or the list is empty -> fileMotFoundException -> GPS coordinates
//TODO GPS Coordinates via android gps services
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ActivityMainBinding binding;//the binding between the classes and UI
    public static CityInfo c;//test city-current city
    public static ArrayList<CityInfo> UserCities = new ArrayList<CityInfo>();//list of user cities,should be saved on restart
    private static JobScheduler jobScheduler;
    private static JobInfo jobInfo;
    private ActionBarDrawerToggle mDrawerToggle;//holds info for the toolbar
    private DrawerLayout mDrawerLayout;//the left drawer
    private static DrawerLayout mDrawerLayoutCities;//the right drawer
    private RecyclerView mRecyclerView;//for the list of cities
    public static RecyclerView.Adapter mAdapter;//for the list of cities
    private RecyclerView.LayoutManager mLayoutManager;//for the list of cities
    private WeatherGetterPeriodically weatherGetterPeriodically;//the background service for updating
    private CityInfoSaveInstance cityInfoSaveInstance;
    private CityInfo extraCity;//extra city for casual logic
    private Context mainContext;


    //Constructor to run code once only @ the beginning of the App
    public MainActivity() {
        super();
        cityInfoSaveInstance = new CityInfoSaveInstance(this);
        //cityInfoSaveInstance.readCitiesList();
        //c = new CityInfo("Sofia", (float) 25.25, (float) 55.28, true);
        //backgroundUpdateForecast();
        //new WeatherGetterOnce(c).start();
        //UserCities.add(c);
        //CityInfo d = new CityInfo("Plovdiv", (float) 0, (float) 0, true);
        //new WeatherGetterOnce(d).start();
        //UserCities.add(d);//adds a secondary city for testing
    }

    public static void setCurrent(CityInfo in) {
        c = in;
        binding.setState(c);//sets the city to be shown in the activity_main
        binding.currentContent.setState(c);
        binding.hourlyContent.setState(c);
        binding.fiveDayContent.setState(c);
        mDrawerLayoutCities.closeDrawer(Gravity.END);
        if (jobScheduler != null) {//TODO fix NPE here
            jobScheduler.cancel(1); //Cancel the current job that's responsible for updating
            jobScheduler.schedule(jobInfo); //Reschedule the job
        }
        else{
            Log.d("Set current","NPE");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainContext = this;
        cityInfoSaveInstance.readCitiesList();

        //backgroundUpdateForecast();
        //c = new CityInfo("Sofia", (float) 25.25, (float) 55.28, true);
        //UserCities.add(c);
        //c.forecast.getMomentForecast();//gets some forecast

        prepareBinding();
        prepareToolbar();
        prepareRightDrawer();
        prepareLeftDrawer();
        prepareRecycler();//fills the cities drawer
        mAdapter.notifyDataSetChanged();//updates the drawer
        //cityInfoSaveInstance.saveCitiesList();
        backgroundUpdateForecast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cityInfoSaveInstance.saveCitiesList();
    }

    private void backgroundUpdateForecast() {
        jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(getApplicationContext(), WeatherGetterPeriodically.class);
        jobInfo = new JobInfo.Builder(1, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(1000 * 60 * 15) //Every 15 mins
                .build();
        jobScheduler.schedule(jobInfo);
    }

    private void prepareRecycler() {
        mRecyclerView = (RecyclerView) binding.citiesDrawerHolder.citiesList;// findViewById(R.id.cities_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new MyAdapter(UserCities);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            //Change selected city from right hand side list
            @Override
            public void onClick(View view, int position) {
                CityInfo city = UserCities.get(position);
                // Toast.makeText(getApplicationContext(), city.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                c = city;
                new WeatherGetterOnce(c, mainContext).start();
                setCurrent(c);
            }

            @Override
            public void onLongClick(View view, int position) {
                extraCity = UserCities.get(position);
                registerForContextMenu(view);
                openContextMenu(view);
                unregisterForContextMenu(view);
            }
        }));
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.city_options, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_city: {
                UserCities.remove(extraCity);
                //Toast.makeText(getApplicationContext(),extraCity.getName()+" removed", Toast.LENGTH_SHORT).show();
                if (UserCities.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No remaining cities", Toast.LENGTH_LONG).show();
                    jobScheduler.cancel(1);
                    c.setName("No city");
                    c.setCountry("showing last info");
                } else {
                    c = UserCities.get(0);
                    setCurrent(c);
                }
                mAdapter.notifyDataSetChanged();

                return true;
            }
            case R.id.cancel:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void prepareBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);//creates the binding
        binding.setState(c);//sets the city to be shown in the activity_main
        binding.currentContent.setState(c);//sets the city to be shown in the content
        binding.swiperefresh.setOnRefreshListener(//swipe down to refresh listener
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    binding.swiperefresh.setRefreshing(true);//changes the state of the icon
                    binding.swiperefresh.setRefreshing(false);
                    new WeatherGetterOnce(c, mainContext).start();
                }
            }
        );
    }

    private void prepareToolbar() {

        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//shows the toolbar arrow and hamburger menu
        getSupportActionBar().setHomeButtonEnabled(true);//shows the toolbar arrow and hamburger menu
        getSupportActionBar().setDisplayShowTitleEnabled(false);//removes the title

    }

    private void prepareRightDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//sets up the left drawer
        mDrawerLayoutCities = (DrawerLayout) findViewById(R.id.drawer_layoutcities);//sets up the right drawer
        TextView add = findViewById(R.id.add_city);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCitiesActivity.class);
                startActivity(intent);
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layoutcities);
                drawerLayout.closeDrawer(Gravity.END);
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Forecast");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        setupDrawer();
        mDrawerLayoutCities.addDrawerListener(new DrawerLayout.DrawerListener() {// handles events
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //MainActivity.mAdapter.notifyDataSetChanged();//updates the drawer to deal with the async
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                mDrawerLayout.closeDrawer(Gravity.START);//closes the other drawer of it was opened

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        binding.citiesDrawerHolder.btnByLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(this,"Forecast by location",Toast.LENGTH_SHORT);
                Log.d("Button clicked:","by location");
            }
        });
    }

    private void prepareLeftDrawer() {
        TextView current = findViewById(R.id.txtCurrent);
        TextView hourly = findViewById(R.id.txtHourly);
        TextView fiveday = findViewById(R.id.txtFiveday);
        binding.hourly.setVisibility(View.INVISIBLE);//sets the visible page
        binding.fiveDay.setVisibility(View.INVISIBLE);//
        binding.current.setVisibility(View.VISIBLE);//
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.START);
                binding.hourly.setVisibility(View.INVISIBLE);
                binding.fiveDay.setVisibility(View.INVISIBLE);
                binding.current.setVisibility(View.VISIBLE);
            }
        });
        hourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.current.setVisibility(View.INVISIBLE);
                binding.hourly.setVisibility(View.VISIBLE);
                binding.fiveDay.setVisibility(View.INVISIBLE);
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        });
        fiveday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.START);
                binding.hourly.setVisibility(View.INVISIBLE);
                binding.fiveDay.setVisibility(View.VISIBLE);
                binding.current.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();//syncs the arrow/hamburger with the drawer state
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {//if the drawer has been intervined with of the phone rotated the buttons will be updated
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawer() {
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_buttons, menu);//adds the button for the cities to the toolbar
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        DrawerLayout drawer2 = (DrawerLayout) findViewById(R.id.drawer_layoutcities);
        if (drawer.isDrawerOpen(GravityCompat.START) || drawer2.isDrawerOpen(GravityCompat.END)) {//closes the drawers
            drawer.closeDrawer(GravityCompat.START);
            drawer2.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {//handles the hamburger /arrow click
            return true;
        }
        if (id == R.id.action_cities) {

            DrawerLayout drawerLayout2 = findViewById(R.id.drawer_layoutcities);//opens the cities drawer if the button was pressed
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            if (drawerLayout2.isDrawerOpen(Gravity.END)) {
                drawerLayout2.closeDrawer(Gravity.END);
            } else {
                drawerLayout2.openDrawer(Gravity.END);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {//handles the click in drawer
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);//closes the drawer upon click
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}