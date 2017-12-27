package android.winter.erasmus.agh.com.example.pierrerainero.whattodo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.R;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.model.Country;
import android.winter.erasmus.agh.com.example.pierrerainero.whattodo.service.UserLocationService;

public class MainActivity extends AppCompatActivity {
    private Location userLocation;
    private Country userCountry;

    @Override
    /**
     * {@inheritDoc}
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
}

