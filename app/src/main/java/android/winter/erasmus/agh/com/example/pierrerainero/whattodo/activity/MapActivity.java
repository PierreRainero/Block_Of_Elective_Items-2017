package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.R;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model.POI;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.service.UserLocationService;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by PierreRainero on 05/12/2017.
 */
public class MapActivity extends Activity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private Location userLocation;
    private MapFragment mMapFragment;

    private POI[] pois;

    @Override
    /**
     * {@inheritDoc}
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pois = (POI[]) getIntent().getSerializableExtra("pois");
        setContentView(R.layout.activity_map);

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void onMapReady(GoogleMap map) {
        mMap = map;

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            userLocation = UserLocationService.getLastKnownLocation(this, null);
            addMarkers();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), 15));
        }
    }

    private void addMarkers(){
        for(POI POI : pois){
            String img = "";
            switch(POI.getType()){
                case MUSEUM:
                    img = "marker_blue";
                    break;
                case PARK:
                    img = "marker_green";
                    break;
                case CHURCH:
                    img = "marker_red";
                    break;
                case NIGHT_CLUB:
                    img = "marker_purple";
                    break;
                case ZOO:
                    img = "marker_orange";
                    break;
            }

            mMap.addMarker(new MarkerOptions().position(POI.getCoord()).title(POI.getName()).icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(img, "drawable", getPackageName()))));
        }
    }
}
