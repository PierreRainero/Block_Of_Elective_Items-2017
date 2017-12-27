package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by PierreRainero on 27/12/2017.
 */

public class UserLocationService {

    public static boolean isGPSworking(Context context){
        return ((LocationManager)context.getSystemService(LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        if (!isGPSworking(context))
            buildAlertMessageNoGps(context);

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public static String getNearestCity(Context context, Location location){
        Address address = getNearestAddress(context, location);
        return address==null ? context.getString(R.string.loading) : address.getLocality();
    }

    public static String getCountry(Context context, Location location){
        Address address = getNearestAddress(context, location);
        return address==null ? context.getString(R.string.loading) : address.getCountryName();
    }

    private static Address getNearestAddress(Context context, Location location){
        Address result = null;

        if (!isGPSworking(context))
            return result;

        Geocoder geoCoder = new Geocoder(context, Locale.ENGLISH);
        List<Address> list = null;
        try {
            list = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list != null & list.size() > 0) {
            Address address = list.get(0);
            result = address;
        }

        return result;
    }

    private static void buildAlertMessageNoGps(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.error));

        builder.setMessage(context.getString(R.string.errorGPS));
        builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton(context.getString(R.string.no), null);
        builder.show();
    }
}