package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
    private final AtomicReference<JSONArray> poiChurchs = new AtomicReference<>();
    private final AtomicReference<JSONArray> poiNightClubs = new AtomicReference<>();
    private final AtomicReference<JSONArray> poiZoos = new AtomicReference<>();

    private int userRange;

    @Override
    /**
     * {@inheritDoc}
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initLocView();
        userSettings();
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

    private void userSettings(){
        final EditText edRange = findViewById(R.id.rangeInput);
        userRange = Integer.parseInt(edRange.getText().toString());
        edRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(edRange.getText().toString().length()<=0 )
                    edRange.setText("1");

                userRange = Integer.parseInt(edRange.getText().toString());
            }
        });
    }

    private void initActivitiesView(){
        final TextView tvMuseum = findViewById(R.id.museum);
        final TextView tvPark = findViewById(R.id.park);
        final TextView tvChurch = findViewById(R.id.church);
        final TextView tvNightClub = findViewById(R.id.nightClub);
        final TextView tvZoo = findViewById(R.id.zoo);

        new Thread (new Runnable() {
            public void run() {
                try {
                    poiMuseums.set((UserLocationService.getPOI(userLocation, userRange, "museum").getJSONArray("results")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvMuseum.post(new Runnable() {
                    public void run() {
                        tvMuseum.setText(poiMuseums.get().length()+" "+getString(R.string.museums));
                        System.out.println(poiMuseums.get().toString());
                    }
                });
            }
        }).start();

        new Thread (new Runnable() {
            public void run() {
                try {
                    poiParks.set((UserLocationService.getPOI(userLocation, userRange, "park").getJSONArray("results")));
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

        new Thread (new Runnable() {
            public void run() {
                try {
                    poiChurchs.set((UserLocationService.getPOI(userLocation, userRange, "church").getJSONArray("results")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvChurch.post(new Runnable() {
                    public void run() {
                        tvChurch.setText(poiChurchs.get().length()+" "+getString(R.string.churchs));
                    }
                });
            }
        }).start();

        new Thread (new Runnable() {
            public void run() {
                try {
                    poiNightClubs.set((UserLocationService.getPOI(userLocation, userRange, "night_club").getJSONArray("results")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvNightClub.post(new Runnable() {
                    public void run() {
                        tvNightClub.setText(poiNightClubs.get().length()+" "+getString(R.string.nightclubs));
                    }
                });
            }
        }).start();

        new Thread (new Runnable() {
            public void run() {
                try {
                    poiZoos.set((UserLocationService.getPOI(userLocation, userRange, "zoo").getJSONArray("results")));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tvZoo.post(new Runnable() {
                    public void run() {
                        tvZoo.setText(poiZoos.get().length()+" "+getString(R.string.zoos));
                    }
                });
            }
        }).start();
    }
}

