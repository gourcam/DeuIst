package com.deu.istatistik;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocationManager {
    private static final long DISTANCE_CHANGE = 20;
    private static final long TIME = 1000 * 30;
    private Location currentLocation;
    private LocationManager locationManager;
    private MyLocationListener myLocationListener;
    private static MyLocationManager INSTANCE;
    
    
//    private MyLocationManager(){
// 
//        public static MyLocationManager getINSTANCE(Context context){
//            if(INSTANCE == null)
//            {
//                INSTANCE = new MyLocationManager(context);
//            }
//            return INSTANCE;
//        }
//    }
    public void startUpdatingLocation(){
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setCostAllowed(false);
        String providerName = locationManager.getBestProvider(criteria, true);
        if (providerName == null){
            List<String> providerNames = locationManager.getAllProviders();
            for (String backupProvider : providerNames){
                if (locationManager.isProviderEnabled(backupProvider))
                    providerName = backupProvider;
            }
        }
        locationManager.requestLocationUpdates(providerName,TIME,DISTANCE_CHANGE,myLocationListener);
    }
    public void stopUpdatingLocation(){
        if (locationManager != null)
            locationManager.removeUpdates(myLocationListener);
    }
    private MyLocationManager(Context context){
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new MyLocationListener();
    }
    public Location getCurrentLocation()
    {
        return currentLocation;
    }
    private class MyLocationListener implements LocationListener{
        public void onLocationChanged(Location location)
        {
            currentLocation = location;
        }
        public void onStatusChanged(String s, int i, Bundle b){
 
        }
        public void onProviderDisabled(String s){
 
        }
        public void onProviderEnabled(String s) {
 
        }
 
    }
 
}
