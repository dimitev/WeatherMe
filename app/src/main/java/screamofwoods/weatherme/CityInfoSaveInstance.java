package screamofwoods.weatherme;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

//TODO REWORK USING SERIALIZED FILES

public class CityInfoSaveInstance extends Application {
    public static final String FILELIST = "UserCities.bin";
    public static final String FILECURRENT = "Current.bin";
    private ArrayList<CityInfo> userCities;
    private CityInfo currentCity;
    private Activity appContext;

    public CityInfoSaveInstance (Activity appContext){
        this.appContext = appContext;
    }

    public void saveCitiesList(){
        userCities = MainActivity.UserCities;
        currentCity = MainActivity.c;
        try{
            File fl = new File(appContext.getFilesDir(), FILELIST);
            File fc = new File(appContext.getFilesDir(), FILECURRENT);
            FileOutputStream fos = new FileOutputStream(fl);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userCities);
            Log.e("Success in write", "Success writing list");
            oos.close();
            fos.close();
            fos = new FileOutputStream(fc);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(currentCity);
            Log.e("Success in write", "Success writing current");
            oos.close();
            fos.close();
        } catch(IOException ioe) {
            Log.e("IOException write", ioe.toString());
        }
    }

    public void readCitiesList(){
        try
        {
            File fl = new File(appContext.getFilesDir(), FILELIST);
            File fc = new File(appContext.getFilesDir(), FILECURRENT);
            if(fl.exists())
            {
                FileInputStream fis = new FileInputStream(fl);
                ObjectInputStream ois = new ObjectInputStream(fis);
                MainActivity.UserCities.clear();
                userCities = (ArrayList<CityInfo>) ois.readObject();
                MainActivity.UserCities = userCities;
                ois.close();
                fis.close();
                if(fc.exists()){
                    fis = new FileInputStream(fc);
                    ois = new ObjectInputStream(fis);
                    currentCity = (CityInfo) ois.readObject();
                    ois.close();
                    fis.close();
                } else {
                    currentCity = userCities.get(0);
                }
                MainActivity.c = currentCity;
            } else {
                Log.e("No files found", "Redirect to search activity");
                MainActivity.UserCities.clear();

                //Log.e("Current city", ""+MainActivity.c.getLat());
//                MainActivity.c = new CityInfo("Sofia", (float) 25.25, (float) 55.28, true);
//                MainActivity.UserCities.add(MainActivity.c);
                //get gps coordinates
                //currentCity = new CityInfo(lat, lon);
                //MainActivity.c = currentCity;
                //instantiate current city to be the city with current coordinates
            }
        }catch(IOException ioe){
            Log.e("IOException read", "Shit is bad man");
            ioe.printStackTrace();
        }catch(ClassNotFoundException cnfe){
            Log.e("Class not found", cnfe.getMessage());
        }
    }
}
