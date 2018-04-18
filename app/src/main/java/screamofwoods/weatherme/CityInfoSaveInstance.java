package screamofwoods.weatherme;

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

//TODO REWORK USING SERIALIZED FILES

public class CityInfoSaveInstance extends Application {
    public static final String FILENAME = "UserCities.bin";
    private ArrayList<CityInfo> userCities;
    private CityInfo currentCity;
    private Context appContext;

    public CityInfoSaveInstance (Context appContext){
        this.appContext = appContext;
    }

    public void saveCitiesList(){
        userCities = MainActivity.UserCities;
        currentCity = MainActivity.c;
        userCities.add(currentCity); //Add current city at the end of the list. When reading remove it!!!
        try{
            File f = new File(appContext.getFilesDir(), FILENAME);
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userCities);
            Log.e("Success in write", "Success");
            oos.close();
            fos.close();
        }catch(NotSerializableException nse){
            nse.printStackTrace();
        }
        catch(IOException ioe) {
            Log.e("IOException write", ioe.toString());
        }
    }

    public void readCitiesList(){
        try
        {
            FileInputStream fis = openFileInput(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            userCities = (ArrayList) ois.readObject();
            if(userCities.isEmpty()) {
                //get gps coordinates
                //currentCity = new CityInfo(lat, lon);
                //MainActivity.c = currentCity;
                //instantiate current city to be the city with current coordinates
            }
            ois.close();
            fis.close();
        }catch(IOException ioe){
            Log.e("IOException read", ioe.getMessage());
        }catch(ClassNotFoundException cnfe){
            Log.e("Class not found", cnfe.getMessage());
        }
    }
}
