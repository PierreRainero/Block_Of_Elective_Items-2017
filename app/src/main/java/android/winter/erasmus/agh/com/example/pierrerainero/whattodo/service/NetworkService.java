package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by PierreRainero on 01/01/2018.
 */

public class NetworkService {

    /**
     * Allow to know if the phone had access to internet
     * @param context current context (activity) of the app
     * @return true if the network is on, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
