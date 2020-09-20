package com.londonappbrewery.climapm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.londonappbrewery.climapm.model.ResponseData;
import com.londonappbrewery.climapm.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WeatherController extends AppCompatActivity {

    final int REQUEST_CODE = 123;
    final String BASE_URL = "http://api.openweathermap.org/";
    final String APP_ID = BuildConfig.APP_ID;
    final long MIN_TIME = 5000;         // Time between location updates in milliseconds (5 s)
    final float MIN_DISTANCE = 1000;    // Distance between location updates in meters (1 km)
    String LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;

    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;
    ResponseData mResponseData;

    LocationManager mLocationManager;   // component that starts/stops location updates
    LocationListener mLocationListener; // component that's notified when the location is changed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);
        Log.d("Clima", "onCreate called");

        mCityLabel = findViewById(R.id.locationTV);
        mWeatherImage = findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = findViewById(R.id.tempTV);
        ImageButton changeCityButton = findViewById(R.id.changeCityButton);

        changeCityButton.setOnClickListener(view -> {
            Intent intent = new Intent(WeatherController.this, ChangeCityActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Clima", "onResume called");

        Intent intent = getIntent();
        String city = intent.getStringExtra("City");

        if (city != null) {
            getWeatherForCity(city);
        } else {
            getWeatherForCurrentLocation();
        }
    }

    private void getWeatherForCity(String city) {
        getWeather(city);
    }

    private void getWeatherForCurrentLocation() {
        Log.d("Clima", "Inside getWeatherForCurrentLocation");
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = createLocationListener();
        checkPermission();
        Log.d("Clima", "Beginning location checking");
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    private LocationListener createLocationListener() {
        return new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                Log.d("Clima", "Inside onLocationChanged");
                getWeather(latitude, longitude);

                Log.d("Clima", "Call executed");
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d("Clima", "Provider enabled");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("Clima", "Provider disabled, location access has been turned off");
            }
        };
    }

    private void getWeather(String... parameters) {
        Log.d("Clima", "Inside getWeather");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseData> request = null;

        if (parameters.length == 1) {
            String city = parameters[0];
            Log.d("Clima", "Getting weather for " + city);
            request = apiService.getWeatherForCity(city, APP_ID);
        } else if (parameters.length == 2) {
            String latitude = parameters[0];
            String longitude = parameters[1];
            Log.d("Clima", "Getting weather for location: " + latitude + ", " + longitude);
            request = apiService.getWeatherForLocation(latitude, longitude, APP_ID);
        }

        fetchDataAndUpdateUI(request);

    }

    private void fetchDataAndUpdateUI(Call<ResponseData> request) {

        request.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if (!response.isSuccessful()) {
                    Log.d("Clima", "Request failed, code " + response.code() + "\n" + response.errorBody());
                    Toast.makeText(WeatherController.this, "Problem with network", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mResponseData = response.body();
                    Log.d("Clima", "Request successful, current temperature for " + mResponseData.getCity() + " is " + mResponseData.getTemperature());
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.d("Clima", "Request failed: " + t.getMessage());
                Toast.makeText(WeatherController.this, "Problem with network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        Log.d("Clima", "Got inside updateUI");
        mTemperatureLabel.setText(mResponseData.getTemperature() + "°");
        mCityLabel.setText(mResponseData.getCity());

        // Get picture resource id based on its name inside the drawable folder
        int resId = getResources().getIdentifier(mResponseData.getIconName(), "drawable", getPackageName());
        mWeatherImage.setImageResource(resId);
    }

    private void checkPermission() {
        Log.d("Clima", "Checking location permission");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Clima", "Location permission  needed");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Clima", "Permission granted");
                getWeatherForCurrentLocation();
            } else {
                Log.d("Clima", "Permission denied");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Clima", "onPause called");

        if (mLocationManager != null) {
            // Stop listening in the background if the activity is being paused
            mLocationManager.removeUpdates(mLocationListener);
        }
    }
}
