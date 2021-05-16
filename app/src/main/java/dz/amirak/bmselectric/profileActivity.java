package dz.amirak.bmselectric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.bottomNavigationView = findViewById(R.id.bottomNavBar);

        bottomNavigationView.setSelectedItemId(R.id.menuProfil);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.addStore:
                        Intent intentAddStore = new Intent(getApplicationContext(),addStoreActivity.class);
                        startActivity(intentAddStore);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menuProfil:
                        return true;

                    case R.id.menuHome:
                        Intent intentHome = new Intent(getApplicationContext(),homeActivity.class);
                        startActivity(intentHome);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            };
        });

    }
}