package dz.amirak.bmselectric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class homeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setSelectedItemId(R.id.menuHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.addStore:
                        Intent intentAddStore = new Intent(getApplicationContext(),addStoreActivity.class);
                        startActivity(intentAddStore);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menuHome:
                        return true;

                    case R.id.menuProfil:
                        Intent intentProfil = new Intent(getApplicationContext(),profileActivity.class);
                        startActivity(intentProfil);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }
}