package com.londonappbrewery.climapm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ChangeCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);

        final EditText editTextField = findViewById(R.id.queryET);
        ImageView backButton = (ImageButton) findViewById(R.id.backButton);

        backButton.setOnClickListener(view -> {
            // Close the activity
            finish();
        });

        editTextField.setOnEditorActionListener((textView, actionId, event) -> {

            String newCity = editTextField.getText().toString();
            // Pass info to main activity
            Intent showCityWeatherIntent = new Intent(ChangeCityActivity.this, WeatherController.class);
            showCityWeatherIntent.putExtra("City", newCity);
            startActivity(showCityWeatherIntent);

            return false;
        });
    }
}
