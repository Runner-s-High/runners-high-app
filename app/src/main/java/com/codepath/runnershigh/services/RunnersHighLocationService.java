package com.codepath.runnershigh.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class RunnersHighLocationService extends Service {
    public static final String TAG = RunnersHighLocationService.class.getCanonicalName();
    public static final String START_LOCATION_UPDATE = "START_LOCATION_UPDATE";
    public static final String STOP_LOCATION_UPDATE = "STOP_LOCATION_UPDATE";
    public static final String STOP_LOCATION_SERVICE = "STOP_LOCATION_SERVICE";
    public static final String LOCATION_PARCELABLE = "LOCATION_PARCELABLE";
    public static final String TOTAL_DISTANCE = "TOTAL_DISTANCE";
    public static final int LOCATION_INTERVAL = 8000;       //Get location every x miliseconds, probably gotta change
    public static final int LOCATION_FAST_INTERVAL = 4000;       //if location is available get it as early as x miliseconds
    public static final int LOCATION_SMALLEST_DISPLACEMENT = 5;     //update location every x meters

    Messenger messenger;

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    Location lastLocation;
    Float totalDistance=0.0f;

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                Log.i(TAG, locationResult.getLastLocation().toString());
                if(lastLocation!=null){
                    Message message = Message.obtain();
                    Bundle data = new Bundle();
                    totalDistance+=lastLocation.distanceTo(locationResult.getLastLocation())*0.000621371f;

                    data.putParcelable(LOCATION_PARCELABLE, lastLocation);
                    data.putFloat(TOTAL_DISTANCE,totalDistance);

                    message.setData(data);
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                lastLocation=locationResult.getLastLocation();
            }
        }
    };


    public RunnersHighLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(LOCATION_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_FAST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(LOCATION_SMALLEST_DISPLACEMENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();
        if(extras!=null){
            if(messenger==null) {
                messenger = (Messenger) extras.get("messenger");
            }
        }
        switch(intent.getAction()){
            case START_LOCATION_UPDATE:
                startLocationUpdates();
                break;
            case STOP_LOCATION_UPDATE:      //Might not actually use idk
                stopLocationUpdates();
                break;
            case STOP_LOCATION_SERVICE:
                stopLocationService();
                break;
            default:
                break;
        }


        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelID="location_notif";
        NotificationCompat.Builder builder=new NotificationCompat.Builder(
                getApplicationContext(),
                channelID
        );
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentTitle("Runner's High");
        builder.setPriority(NotificationCompat.PRIORITY_LOW);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if(notificationManager!=null
                    && notificationManager.getNotificationChannel(channelID)==null){
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelID,
                        "Location Service",
                        NotificationManager.IMPORTANCE_LOW
                );
                notificationChannel.setDescription("Used by location Service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        startForeground(123,builder.build());
    }

    private void stopLocationUpdates(){
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            stopForeground(true);
        }catch (Exception ignored){}
    }

    private void stopLocationService(){
        stopLocationUpdates();
        stopSelf();
    }


    @Override
    public void onDestroy() {
        stopLocationService();
        super.onDestroy();
    }
}