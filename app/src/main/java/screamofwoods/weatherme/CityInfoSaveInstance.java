package screamofwoods.weatherme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//TODO REWORK USING SERIALIZED FILES

public class CityInfoSaveInstance extends Application {
    public static final String PREFERENCE_NAME = "USER_CITIES_PREFERENCE";
    private SharedPreferences cityListPreference;
    private static Context appContext;
    private ArrayList<CityInfo> userCities;
    private Set<CityInfo> userCitiesHash;
    private CityInfo currentCity;

    public CityInfoSaveInstance (ArrayList<CityInfo> userCities, CityInfo currentCity){
        super.onCreate();
        appContext = this;
        this.userCities = userCities;
        cityListPreference = appContext.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        userCitiesHash = new HashSet<>();
        this.currentCity = currentCity;
    }

    public void saveCitiesList(){
        //Open file
        userCities.add(userCities.size()-1,currentCity); //Add current city at the end of the list. When reading remove it!!!
        //Serialize the Array list to the file
        //CLose the file
    }
    //TODO IMPLEMENT METHOD TO READ THE FILE
    //TODO IMPLEMENT METHOD TO UPDATE THE FILE
    //The above two could be done in one method

}
