package screamofwoods.weatherme;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CityInfoSaveInstance{
    public static final String FILELIST = "UserCities.bin";
    public static final String FILECURRENT = "Current.bin";
    private ArrayList<CityInfo> userCities;
    private CityInfo currentCity;
    private Context appContext;

    public CityInfoSaveInstance (Context appContext){
        this.appContext = appContext;
    }

    //Save the favourite cities to a file
    public void saveCitiesList(){
        userCities = MainActivity.UserCities;
        currentCity = MainActivity.c;
        try{
            File fl = new File(appContext.getFilesDir(), FILELIST);
            File fc = new File(appContext.getFilesDir(), FILECURRENT);
            FileOutputStream fos;
            ObjectOutputStream oos;
            if(userCities.isEmpty()){
                fl.delete();
            } else {
                fos = new FileOutputStream(fl);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(userCities);
                oos.close();
                fos.close();
            }
            if(currentCity == null){
                fc.delete();
            } else{
                if(currentCity.getName().equals("No city")){
                    fc.delete();
                } else {
                    fos = new FileOutputStream(fc);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(currentCity);
                    oos.close();
                    fos.close();
                }
            }
        } catch(IOException ioe) {
            Log.e("IOException write", ioe.toString());
        }
    }

    //Read the favorite cities files
    public boolean readCitiesList(){
        try
        {
            File fl = new File(appContext.getFilesDir(), FILELIST);
            File fc = new File(appContext.getFilesDir(), FILECURRENT);
            if(fl.exists()) {
                FileInputStream fis = new FileInputStream(fl);
                ObjectInputStream ois = new ObjectInputStream(fis);
                MainActivity.UserCities.clear();
                userCities = (ArrayList<CityInfo>) ois.readObject();
                if (!userCities.isEmpty()) {
                    MainActivity.UserCities = userCities;
                }
                ois.close();
                fis.close();
                if (fc.exists()) {
                    fis = new FileInputStream(fc);
                    ois = new ObjectInputStream(fis);
                    currentCity = (CityInfo) ois.readObject();
                    ois.close();
                    fis.close();
                } else {
                    if (!userCities.isEmpty()) {
                        currentCity = userCities.get(0);
                    }
                }
                if (currentCity != null) {
                    MainActivity.setCurrent(currentCity);
                }
            }
        }catch(IOException ioe){
            Log.e("IOException read", "Shit is bad man");
            ioe.printStackTrace();
        }catch(ClassNotFoundException cnfe){
            Log.e("Class not found", cnfe.getMessage());
        }
        return currentCity == null;
    }
}
