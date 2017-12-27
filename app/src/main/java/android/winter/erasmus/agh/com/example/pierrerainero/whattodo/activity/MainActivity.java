package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.activity;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.R;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model.UserLocationService;

public class MainActivity extends AppCompatActivity {
    private Location userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        userLocation = UserLocationService.getLastKnownLocation(this);

        Button returnButton = this.findViewById(R.id.button2);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                //myIntent.putExtra("store", selectedStore);

                /*
                * GET NEAREST CITY
                *
                * List<Address> list = geoCoder.getFromLocation(location
                    .getLatitude(), location.getLongitude(), 1);
                    if (list != null & list.size() > 0) {
                        Address address = list.get(0);
                        result = address.getLocality();
                        return result;
                * */
                if(UserLocationService.isGPSworking((LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE)))
                    MainActivity.this.startActivity(myIntent);
            }
        });
    }
}

