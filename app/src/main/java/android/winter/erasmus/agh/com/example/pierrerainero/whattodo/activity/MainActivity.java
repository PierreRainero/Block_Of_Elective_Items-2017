package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.R;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model.Country;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.service.UserLocationService;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private Location userLocation;
    private Country userCountry;
    private final AtomicReference<JSONArray> poiMuseums = new AtomicReference<>();
    private final AtomicReference<JSONArray> poiParks = new AtomicReference<>();

    @Override
    /**
     * {@inheritDoc}
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initLocView();
        initActivitiesView();

        Button returnButton = this.findViewById(R.id.button2);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, MapActivity.class);

                if(UserLocationService.isGPSworking(getApplicationContext()))
                    MainActivity.this.startActivity(myIntent);
            }
        });
    }

    private void initLocView(){
        userLocation = UserLocationService.getLastKnownLocation(this);

        final TextView tvCity = findViewById(R.id.city);
        final TextView tvCountry = findViewById(R.id.country);
        final ImageView ivCountry = findViewById(R.id.countryFlag);
        tvCity.setText(UserLocationService.getNearestCity(this, userLocation));
        tvCountry.setText(UserLocationService.getCountry(this, userLocation));

        userCountry = Country.getEnumOf((String) tvCountry.getText());
        if(userCountry!=null){
            Drawable drawable = getResources().getDrawable(getResources().getIdentifier("flag_"+userCountry.getCode() , "drawable", getPackageName()));
            ivCountry.setImageDrawable(drawable);
            ivCountry.setVisibility(View.VISIBLE);
        }else
            ivCountry.setVisibility(View.INVISIBLE);
    }

    private void initActivitiesView(){
        final TextView tvMuseum = findViewById(R.id.museum);
        final TextView tvPark = findViewById(R.id.park);

        new Thread (new Runnable() {
            public void run() {
                try {
                    poiMuseums.set((UserLocationService.getPOI(userLocation, 1000, "museum").getJSONArray("results")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvMuseum.post(new Runnable() {
                    public void run() {
                        tvMuseum.setText(poiMuseums.get().length()+" "+getString(R.string.museums));
                    }
                });
            }
        }).start();

        new Thread (new Runnable() {
            public void run() {
                try {
                    poiParks.set((UserLocationService.getPOI(userLocation, 1000, "park").getJSONArray("results")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvPark.post(new Runnable() {
                    public void run() {
                        tvPark.setText(poiParks.get().length()+" "+getString(R.string.parks));
                    }
                });
            }
        }).start();
    }
}

