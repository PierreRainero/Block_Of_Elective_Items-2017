package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by PierreRainero on 28/12/2017.
 */

public class POI implements Serializable{
    private String name;
    private Type type;
    private double lat;
    private double lng;

    public POI(String name, Type type, double lat, double lng){
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName(){
        return name;
    }

    public Type getType(){
        return type;
    }

    public LatLng getCoord(){
        return  new LatLng(lat, lng);
    }
}
