package android.winter.erasmus.agh.com.example.pierrerainero.whattodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button returnButton = (Button) this.findViewById(R.id.button2);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, MapActivity.class);
                //myIntent.putExtra("store", selectedStore);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }
}

