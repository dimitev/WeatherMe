package screamofwoods.weatherme;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GeoLocation {
    private final int REQUEST_PERMISSION_COARSE_LOCATION = 2;
    private FusedLocationProviderClient mFusedLocationClient;
    private Activity appContext;

    public GeoLocation(Activity appContext){
        this.appContext = appContext;
    }

    public void getLocation(){
        if(ContextCompat.checkSelfPermission(appContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Permission for geolocation
            if(ActivityCompat.shouldShowRequestPermissionRationale(appContext, Manifest.permission.ACCESS_COARSE_LOCATION)){
                showExplanation("Permission Needed!", "Access GeoLocation", Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_COARSE_LOCATION);
            } else {
                requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_PERMISSION_COARSE_LOCATION);
            }
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(appContext, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //TODO provide dialogue to ask the user for metric or not!
                    if(location != null){
                        MainActivity.c = new CityInfo((float) location.getLatitude(), (float) location.getLongitude(), true);
                    }
                }
            });
        }
    }

    private void showExplanation(String title, String message, final String permission, final int permissionRequestCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setTitle(title).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id) {
                requestPermission(permission, permissionRequestCode);
            }
        });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode){
        ActivityCompat.requestPermissions(appContext, new String[]{permissionName}, permissionRequestCode);
    }
}
