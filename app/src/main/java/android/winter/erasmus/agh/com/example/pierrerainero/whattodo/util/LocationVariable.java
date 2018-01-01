package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.util;

import android.location.Location;

/**
 * Created by PierreRainero on 01/01/2018.
 */

public class LocationVariable {
    private Location location = null;
    private ChangeListener listener;

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        if(location==null)
            return;

        this.location = location;
        if (listener != null)
            listener.onChange();
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    public interface ChangeListener {
        void onChange();
    }
}
