package dz.amirak.bmselectric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

//import android.content.res.Configuration;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.Locale;

import static android.view.View.VISIBLE;

public class addStoreActivity extends AppCompatActivity {
    private MapView map;

    private BottomNavigationView bottomNavigationView;
    private ImageView currentPosition;
    private Button sendButton;
    private ProgressBar spinProgress;
    private TextView storeName, vendorName, phoneStore, adresse;


    protected LocationManager locationManager;


    protected double latitude=0;
    protected double longitude=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_add_store);

        this.storeName = findViewById(R.id.storeName);
        this.vendorName = findViewById(R.id.vendorName);
        this.phoneStore = findViewById(R.id.phoneStore);
        this.adresse = findViewById(R.id.adresse);
        this.spinProgress = findViewById(R.id.spinProgress);

        this.bottomNavigationView = findViewById(R.id.bottomNavBar);
        this.currentPosition = findViewById(R.id.currentPosition);
        this.sendButton = findViewById(R.id.sendButton);
        bottomNavigationView.setSelectedItemId(R.id.addStore);
        this.map = (MapView) findViewById(R.id.mapOSM);


        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        map.getController().setZoom(16.0);
        map.getController().setCenter(new GeoPoint(30.266000, -97.739000));

        Drawable iconMarker = addStoreActivity.this.getResources().getDrawable(R.drawable.ic_location);

        currentPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) addStoreActivity.this.getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(addStoreActivity.this, "ACTIVER POSSITION", Toast.LENGTH_LONG).show();
                    return;
                }
                if (ActivityCompat.checkSelfPermission(addStoreActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(addStoreActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(addStoreActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                    return;
                }
                Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location locationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if(locationGps != null){
                    latitude = locationGps.getLatitude();
                    longitude = locationGps.getLongitude();
                }else if(locationNet != null){
                    latitude = locationNet.getLatitude();
                    longitude = locationNet.getLongitude();
                }else if(locationPassive != null){
                    latitude = locationPassive.getLatitude();
                    longitude = locationPassive.getLongitude();
                }else{
                    Toast.makeText(addStoreActivity.this, "khra", Toast.LENGTH_LONG).show();
                    return;
                }
                map.getController().setCenter(new GeoPoint(latitude, longitude));
                map.getController().setZoom(18.0);

                Marker marker=new Marker(map);
                marker.setPosition(new GeoPoint(latitude, longitude));
                marker.setIcon(iconMarker);
                //marker.setImage(drawable);
                //marker.showInfoWindow();
                map.getOverlays().add(marker);
                //map.invalidate();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuHome:
                        Intent intentHome = new Intent(getApplicationContext(), homeActivity.class);
                        startActivity(intentHome);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.addStore:
                        return true;

                    case R.id.menuProfil:
                        Intent intentProfil = new Intent(getApplicationContext(), profileActivity.class);
                        startActivity(intentProfil);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            };
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storeName.getText().toString().trim().isEmpty()){
                    storeName.setError("Saisissez le nom du magasin !");
                    //return;
                }
                if(vendorName.getText().toString().trim().isEmpty()){
                    vendorName.setError("Saisissez le nom complet du vendeur !");
                    //return;
                }
                if(phoneStore.getText().toString().trim().isEmpty()){
                    phoneStore.setError("Saisissez le numero du magasin !");
                    //return;
                }
                if(adresse.getText().toString().trim().isEmpty()){
                    adresse.setError("Saisissez l'adresse du magasin !");
                    //return;
                }
                if(longitude == 0 || latitude ==  0){
                    Toast.makeText(addStoreActivity.this, "Position non definie !", Toast.LENGTH_LONG).show();
                    return;
                }
                spinProgress.setVisibility(View.VISIBLE);
                sendButton.setEnabled(false);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

}

