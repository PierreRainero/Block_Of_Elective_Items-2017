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
import android.util.Log;
import android.widget.TextView;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by PierreRainero on 27/12/2017.
 */

public class UserLocationService {
    private static final String REQUEST_BASE = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private static final String GOOGLE_PLACE_API_KEY = "AIzaSyBre2SVX990QCnYvyzw8zTgni-fiGYMStA";

    /**
     * Allow to know if the GPS is working
     * @param context current context (activity) of the app
     * @return true if the GPS is on, false otherwise
     */
    public static boolean isGPSworking(Context context){
        return ((LocationManager)context.getSystemService(LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Return the current user location
     * @param context current context (activity) of the app
     * @return current user location (or 1)
     */
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
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    /**
     * Find the closest city from the user
     * @param context current context (activity) of the app
     * @param location current user location
     * @return name of the closest city
     */
    public static String getNearestCity(Context context, Location location){
        Address address = getNearestAddress(context, location);
        return address==null ? context.getString(R.string.loading) : address.getLocality();
    }

    /**
     * Find the country where the user is
     * @param context current context (activity) of the app
     * @param location current user location
     * @return country name
     */
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

    public static JSONObject getPOI (Location location, int range, String type) throws IOException, JSONException {
        String answer = "";
        String temp = "";
        String request =  REQUEST_BASE + location.getLatitude() + "," + location.getLongitude() + "&radius=" + range + "&type=" + type +"&key=" + GOOGLE_PLACE_API_KEY;

        URL url = new URL(request);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        InputStream in = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        while ((temp = reader.readLine()) != null)
        {
            answer = answer + temp;
        }
        reader.close();

        return new JSONObject(answer);
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
