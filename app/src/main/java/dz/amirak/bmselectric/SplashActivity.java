package dz.amirak.bmselectric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private static int splash_screen_timer = 2000;

    private FirebaseAuth numAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        numAuth = FirebaseAuth.getInstance();
        currentUser = numAuth.getCurrentUser();

        this.logo =findViewById(R.id.logo);

        if(currentUser == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, loginActivity.class);
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View, String>(logo, "logoTransition");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent, options.toBundle());
                }
            }, splash_screen_timer);
        }else {
            currentUser.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Intent intentHome = new Intent(SplashActivity.this,homeActivity.class);
                        startActivity(intentHome);
                        finish();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, loginActivity.class);
                                Pair[] pairs = new Pair[1];
                                pairs[0] = new Pair<View, String>(logo, "logoTransition");
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent, options.toBundle());
                            }
                        }, splash_screen_timer);
                    }
                }
            });
        }
    }


}