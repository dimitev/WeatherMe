package screamofwoods.weatherme;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
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
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import screamofwoods.weatherme.databinding.ActivityMainBinding;

import static com.loopj.android.http.AsyncHttpClient.log;

//TODO response time check, use OnFailure
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static ActivityMainBinding binding;//the binding between the classes and UI
    public static CityInfo c = new CityInfo();//test city-current city
    public static ArrayList<CityInfo> UserCities = new ArrayList<CityInfo>();//list of user cities,should be saved on restart
    private static JobScheduler jobScheduler;
    private static JobInfo jobInfo;
    private ActionBarDrawerToggle mDrawerToggle;//holds info for the toolbar
    private DrawerLayout mDrawerLayout;//the left drawer
    private static DrawerLayout mDrawerLayoutCities;//the right drawer
    private RecyclerView mRecyclerView;//for the list of cities
    public static RecyclerView.Adapter mAdapter;//for the list of cities
    private RecyclerView.LayoutManager mLayoutManager;//for the list of cities
    private static RecyclerView mRecyclerViewHourly;//for the list of hourly forecast
    public static RecyclerView.Adapter mAdapterHourly;//for the list of hourly forecast
    private RecyclerView.LayoutManager mLayoutManagerHourly;//for the list of hourly forecast
    //private WeatherGetterPeriodically weatherGetterPeriodically;//the background service for updating
    private CityInfoSaveInstance cityInfoSaveInstance;
    private CityInfo extraCity;//extra city for casual logic
    private static Context mainContext;
    private static GraphView FiveDayGraph;
    public static int currentTab = 0;

    //Constructor to run code once only @ the beginning of the App
    public MainActivity() {
        super();
        cityInfoSaveInstance = new CityInfoSaveInstance(this);
    }

    public static void setCurrent(CityInfo in) {
        if (c == null) {
            log.e("C is NULL", "FUCK FUCK FUCK");
        }
        //c.Copy(in);
        c = in;
        binding.setState(c);
        set5DayGraph();
        mAdapterHourly = new MyAdapterHourly(MainActivity.c.hourly);
        mRecyclerViewHourly.setAdapter(mAdapterHourly);
        mDrawerLayoutCities.closeDrawer(Gravity.END);
        binding.notifyPropertyChanged(BR._all);
        if (jobScheduler != null) {//TODO fix NPE here
            jobScheduler.cancel(1); //Cancel the current job that's responsible for updating
            jobScheduler.schedule(jobInfo); //Reschedule the job
        } else {
            backgroundUpdateForecast();
            //Log.d("Set current", "NPE");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.mainContext = getAppContext();
        mainContext = this;
        if (cityInfoSaveInstance.readCitiesList()) {
            log.e("file io", "asdf: " + (c == null ? "true" : "false"));
            Intent intent = new Intent(MainActivity.this, AddCitiesActivity.class);
            startActivity(intent);
        }
        //new WeatherGetterOnce(c, this).start();
        prepareBinding();
        set5DayGraph();
        prepareToolbar();
        prepareLeftDrawer();
        prepareRightDrawer();
        prepareRecycler();//fills the cities drawer
        prepareRecyclerHourly();//fills the hourly forecast
        mAdapter.notifyDataSetChanged();//updates the drawer

        //cityInfoSaveInstance.saveCitiesList();
    }

    @Override
    protected void onStop() {
        cityInfoSaveInstance.saveCitiesList();
        if (jobScheduler != null) jobScheduler.cancel(1);
        super.onStop();
    }

    public static Context getAppContext() {
        return MainActivity.mainContext;
    }

    private static void backgroundUpdateForecast() {
        jobScheduler = (JobScheduler) getAppContext().getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName componentName = new ComponentName(getAppContext(), WeatherGetterPeriodically.class);
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
                setCurrent(UserCities.get(position));
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

    private void prepareRecyclerHourly() {
        mRecyclerViewHourly = findViewById(R.id.hourly_list);
        mRecyclerViewHourly.setHasFixedSize(true);
        mLayoutManagerHourly = new LinearLayoutManager(this);
        mRecyclerViewHourly.setLayoutManager(mLayoutManagerHourly);
        // specify an adapter
        mAdapterHourly = new MyAdapterHourly(MainActivity.c.hourly);
        mRecyclerViewHourly.setAdapter(mAdapterHourly);
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
                    if (jobScheduler != null) {
                        jobScheduler.cancel(1);
                    }
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

    public void SetVisiblePage(int i)//set the selected forecast,1-current,2-24h,3-5day
    {
        if (i == 0 || i == 1) {
            binding.current.setVisibility(View.VISIBLE);//
            binding.hourly.setVisibility(View.GONE);//sets the visible page
            binding.fiveDay.setVisibility(View.GONE);//
        } else {
            if (i == 2) {
                binding.current.setVisibility(View.GONE);//
                binding.hourly.setVisibility(View.VISIBLE);//sets the visible page
                binding.fiveDay.setVisibility(View.GONE);//
            } else {
                binding.current.setVisibility(View.GONE);//
                binding.hourly.setVisibility(View.GONE);//sets the visible page
                binding.fiveDay.setVisibility(View.VISIBLE);//
            }
        }
    }

    private void prepareBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);//creates the binding
        binding.setState(c);//sets the city to be shown in the activity_main
        SetVisiblePage(currentTab);

        binding.currentContent.swiperefresh.setOnRefreshListener(//swipe down to refresh listener
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        binding.currentContent.swiperefresh.setRefreshing(true);//changes the state of the icon
                        if (c != null && !(c.getName().equals("No city"))) {
                            new WeatherGetterOnce(c, mainContext).start();
                        } else {
                            Intent intent = new Intent(MainActivity.this, AddCitiesActivity.class);
                            startActivity(intent);
                            binding.currentContent.swiperefresh.setRefreshing(false);
                        }
                    }
                }
        );
        binding.hourlyContent.swiperefresh.setOnRefreshListener(//swipe down to refresh listener
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        binding.hourlyContent.swiperefresh.setRefreshing(true);//changes the state of the icon
                        if (c != null && !(c.getName().equals("No city"))) {
                            new WeatherGetterOnce(c, mainContext).start();
                        } else {
                            Intent intent = new Intent(MainActivity.this, AddCitiesActivity.class);
                            startActivity(intent);
                            binding.hourlyContent.swiperefresh.setRefreshing(false);
                        }
                    }
                }
        );
        binding.fiveDayContent.swiperefresh.setOnRefreshListener(//swipe down to refresh listener
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        binding.fiveDayContent.swiperefresh.setRefreshing(true);//changes the state of the icon
                        if (c != null && !(c.getName().equals("No city"))) {
                            new WeatherGetterOnce(c, mainContext).start();
                        } else {
                            Intent intent = new Intent(MainActivity.this, AddCitiesActivity.class);
                            startActivity(intent);
                            binding.fiveDayContent.swiperefresh.setRefreshing(false);
                        }
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
    }

    public static void set5DayGraph() {
        FiveDayGraph = binding.fiveDayContent.graph;//findViewById(R.id.graph);
        LineGraphSeries<DataPoint> seriesMax = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> seriesMin = new LineGraphSeries<>();
        int date=0;
        for (int i = 0; i < 5; i++) {
            date=c.fiveDay[i]==null||c.fiveDay[i].getDate()==""? i : Integer.parseInt(c.fiveDay[i].getDate().split("-|\\ ")[2]);

            seriesMax.appendData(new DataPoint(date, c.fiveDay[i].getMaxTemp()), false, 5);
            seriesMin.appendData(new DataPoint(date, c.fiveDay[i].getMinTemp()), false, 5);
                                    /*appendData(new DataPoint[] {
                                    new DataPoint(0, 1),
                                    new DataPoint(1, 5)});*/
        }
        FiveDayGraph.removeAllSeries();
        FiveDayGraph.getGridLabelRenderer().setGridColor(Color.WHITE);
        FiveDayGraph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        FiveDayGraph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        FiveDayGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);

        seriesMax.setDrawBackground(true);
        seriesMax.setColor(Color.parseColor("#AAFFE8BC"));
        seriesMax.setThickness(10);
        seriesMax.setBackgroundColor(Color.parseColor("#66FFE8BC"));
        seriesMax.setDrawDataPoints(true);
        seriesMax.setDataPointsRadius(15);


        seriesMin.setColor(Color.parseColor("#AA2D70FF"));
        seriesMin.setDrawBackground(true);
        seriesMin.setBackgroundColor(Color.parseColor("#662D70FF"));
        seriesMin.setThickness(10);
        seriesMin.setDrawDataPoints(true);
        seriesMin.setDataPointsRadius(15);
        FiveDayGraph.addSeries(seriesMax);
        FiveDayGraph.addSeries(seriesMin);
    }

    private void prepareLeftDrawer() {
        findViewById(R.id.txtCurrent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.START);
                MainActivity.currentTab = 1;
                SetVisiblePage(currentTab);
            }
        });
        findViewById(R.id.txtHourly).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.START);
                MainActivity.currentTab = 2;
                SetVisiblePage(currentTab);
            }
        });
        findViewById(R.id.txtFiveday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(Gravity.START);
                MainActivity.currentTab = 3;
                SetVisiblePage(currentTab);
                set5DayGraph();
            }
        });
        findViewById(R.id.txtAbout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
                //Uncomment the below code to Set the message and title from the strings.xml file
                //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("This weather application is made by Atanas Shalaverov and Dimitar Mitev to gather experience.")
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //finish();
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("About");
                alert.show();
            }
        });
        findViewById(R.id.txtSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
                //Uncomment the below code to Set the message and title from the strings.xml file
                //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

                //Setting message manually and performing action on button click
                builder.setMessage("Select the metric system for this location")
                        .setCancelable(true)
                        .setPositiveButton("Celsius", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //finish();
                                c.setIsMetric(true);
                                new WeatherGetterOnce(c,mainContext).start();
                                mDrawerLayout.closeDrawer(Gravity.START);
                            }
                        })
                        .setNegativeButton("Fahrenheit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //finish();
                                c.setIsMetric(false);
                                new WeatherGetterOnce(c,mainContext).start();
                                mDrawerLayout.closeDrawer(Gravity.START);
                            }
                        });

                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("SI or imperial?");
                alert.setCancelable(true);
                alert.show();
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