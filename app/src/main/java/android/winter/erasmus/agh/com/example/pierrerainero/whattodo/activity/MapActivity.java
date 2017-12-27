package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.R;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.service.UserLocationService;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by PierreRainero on 05/12/2017.
 */
public class MapActivity extends Activity implements OnMapReadyCallback {
    private GoogleMap mMap;

    private Location userLocation;
    private MapFragment mMapFragment;

    @Override
    /**
     * {@inheritDoc}
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            userLocation = UserLocationService.getLastKnownLocation(this);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()), 15));
        }
    }
}
