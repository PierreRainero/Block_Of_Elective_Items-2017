package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.R;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model.Country;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model.POI;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model.Type;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.service.UserLocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private CheckBox cbMuseum;
    private CheckBox cbPark;
    private CheckBox cbChurch;
    private CheckBox cbNightClub;
    private CheckBox cbZoo;

    private POI[] POIToDisplay;

    private int userRange;

    @Override
    /**
     * {@inheritDoc}
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cbMuseum = this.findViewById(R.id.museumMarker);
        cbPark = this.findViewById(R.id.parkMarker);
        cbChurch = this.findViewById(R.id.churchMarker);
        cbNightClub = this.findViewById(R.id.nightClubMarker);
        cbZoo = this.findViewById(R.id.zooMarker);

        getIntent().setAction("Already created");

        initLocView();
        userSettings();
        updateActivitiesView();
        initButtons();
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
                if(edRange.getText().toString().length()<=0)
                    edRange.setText("1");

                userRange = Integer.parseInt(edRange.getText().toString());
            }
        });
    }

    private void updateActivitiesView(){
        final TextView tvMuseum = findViewById(R.id.museum);
        final TextView tvPark = findViewById(R.id.park);
        final TextView tvChurch = findViewById(R.id.church);
        final TextView tvNightClub = findViewById(R.id.nightClub);
        final TextView tvZoo = findViewById(R.id.zoo);

        if(userLocation==null)
            return;

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

    private void initButtons(){

        ImageButton refreshBtn = this.findViewById(R.id.refreshButton);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateActivitiesView();
            }
        });

        Button mapBtn = this.findViewById(R.id.mapButton);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(poiMuseums.get()==null || poiParks.get()==null || poiChurchs.get()==null || poiNightClubs.get()==null || poiZoos.get()==null )
                    return;
                Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                POIToDisplay = selectPoiToDisplay();
                myIntent.putExtra("pois", POIToDisplay);
                if(UserLocationService.isGPSworking(getApplicationContext()))
                    MainActivity.this.startActivity(myIntent);
            }
        });
    }

    private POI[] selectPoiToDisplay(){
        int nbOfPoi = 0, i=0, localMax;
        boolean useMuseum = cbMuseum.isChecked(), usePark = cbPark.isChecked(),
                useChurch = cbChurch.isChecked(), useNightClub = cbNightClub.isChecked(),
                useZoo = cbZoo.isChecked();

        if(useMuseum)
            nbOfPoi += poiMuseums.get().length();
        if(usePark)
            nbOfPoi += poiParks.get().length();
        if(useChurch)
            nbOfPoi += poiChurchs.get().length();
        if(useNightClub)
            nbOfPoi += poiNightClubs.get().length();
        if(useZoo)
            nbOfPoi += poiZoos.get().length();

        POI[] result = new POI[nbOfPoi];

        if(useMuseum){
            localMax = poiMuseums.get().length();
            for(int y=0;y<localMax;y++){
                String name = "";
                Type type = Type.MUSEUM;
                double lat=0, lng=0;
                try {
                    JSONObject element = (JSONObject) poiMuseums.get().get(y);
                    JSONObject location = element.getJSONObject("geometry").getJSONObject("location");
                    lat = location.getDouble("lat");
                    lng = location.getDouble("lng");
                    name = element.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result[i] = new POI(name, type, lat, lng);
                i++;
            }
        }

        if(usePark){
            localMax = poiParks.get().length();
            for(int y=0;y<localMax;y++){
                String name = "";
                Type type = Type.PARK;
                double lat=0, lng=0;
                try {
                    JSONObject element = (JSONObject) poiParks.get().get(y);
                    JSONObject location = element.getJSONObject("geometry").getJSONObject("location");
                    lat = location.getDouble("lat");
                    lng = location.getDouble("lng");
                    name = element.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result[i] = new POI(name, type, lat, lng);
                i++;
            }
        }

        if(useChurch){
            localMax = poiChurchs.get().length();
            for(int y=0;y<localMax;y++){
                String name = "";
                Type type = Type.CHURCH;
                double lat=0, lng=0;
                try {
                    JSONObject element = (JSONObject) poiChurchs.get().get(y);
                    JSONObject location = element.getJSONObject("geometry").getJSONObject("location");
                    lat = location.getDouble("lat");
                    lng = location.getDouble("lng");
                    name = element.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result[i] = new POI(name, type, lat, lng);
                i++;
            }
        }

        if(useNightClub){
            localMax = poiNightClubs.get().length();
            for(int y=0;y<localMax;y++){
                String name = "";
                Type type = Type.NIGHT_CLUB;
                double lat=0, lng=0;
                try {
                    JSONObject element = (JSONObject) poiNightClubs.get().get(y);
                    JSONObject location = element.getJSONObject("geometry").getJSONObject("location");
                    lat = location.getDouble("lat");
                    lng = location.getDouble("lng");
                    name = element.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result[i] = new POI(name, type, lat, lng);
                i++;
            }
        }

        if(useZoo){
            localMax = poiZoos.get().length();
            for(int y=0;y<localMax;y++){
                String name = "";
                Type type = Type.ZOO;
                double lat=0, lng=0;
                try {
                    JSONObject element = (JSONObject) poiZoos.get().get(y);
                    JSONObject location = element.getJSONObject("geometry").getJSONObject("location");
                    lat = location.getDouble("lat");
                    lng = location.getDouble("lng");
                    name = element.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                result[i] = new POI(name, type, lat, lng);
                i++;
            }
        }

        return result;
    }



    @Override
    /**
     * {@inheritDoc}
     */
    protected void onResume() {
        String action = getIntent().getAction();

        if(action == null || !action.equals("Already created")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
            getIntent().setAction(null);

        super.onResume();
    }
}

