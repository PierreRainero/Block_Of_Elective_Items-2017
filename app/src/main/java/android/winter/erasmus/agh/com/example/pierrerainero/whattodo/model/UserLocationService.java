package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.R;

import java.io.Serializable;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by PierreRainero on 27/12/2017.
 */

public class UserLocationService {

    public static boolean isGPSworking(LocationManager locationManager){
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
        if (!isGPSworking(locationManager))
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
