package dz.amirak.bmselectric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class loginActivity extends AppCompatActivity {

    private Button sendButton;
    private EditText inputUser;
    private ImageView logo;
    private TextView indicationText;
    private TextView stepText;
    private ProgressBar spinProgress;

    private FirebaseAuth numAuth;
    private FirebaseUser currentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        numAuth = FirebaseAuth.getInstance();
        currentUser = numAuth.getCurrentUser();

        this.sendButton = findViewById(R.id.sendButton);
        this.inputUser = findViewById(R.id.inputUser);
        this.logo = findViewById(R.id.logo);
        this.indicationText = findViewById(R.id.indicationText);
        this.stepText = findViewById(R.id.stepText);
        this.spinProgress = findViewById(R.id.spinProgress);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinProgress.setVisibility(View.VISIBLE);
                sendButton.setEnabled(false);
                String numPhone = inputUser.getText().toString().trim();
                if(numPhone.length() != 10){
                    inputUser.setError("Veuillez entrer un numero valide !");
                    spinProgress.setVisibility(View.GONE);
                    sendButton.setEnabled(true);
                    return;
                }
                if(numPhone.charAt(0) != '0' || !(numPhone.charAt(1) == '5' || numPhone.charAt(1) == '6' || numPhone.charAt(1) == '7')){
                    inputUser.setError("Veuillez entrer un numero valide 1 !");
                    spinProgress.setVisibility(View.GONE);
                    sendButton.setEnabled(true);
                    return;
                }
                numPhone = numPhone.substring(0);
                numPhone = "+213"+numPhone;
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(numAuth)
                                .setPhoneNumber(numPhone)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(loginActivity.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)
                                // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Toast.makeText(loginActivity.this, "Code OPT envoyer !", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(loginActivity.this, ConfirmSmsActivity.class);
                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String>(logo, "logoTransition");
                pairs[1] = new Pair<View, String>(inputUser, "inputUserTransition");
                pairs[2] = new Pair<View, String>(sendButton, "sendButtonTransition");
                pairs[3] = new Pair<View, String>(indicationText, "indicationTextTransition");
                pairs[4] = new Pair<View, String>(stepText, "stepTextTransition");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(loginActivity.this, pairs);
                        intent.putExtra("auth" , verificationId);
                        startActivity(intent, options.toBundle());
                    }
                }, 100);

            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(loginActivity.this, "OPT AUTO DETECT !", Toast.LENGTH_LONG).show();

                numAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(loginActivity.this, SignUpActivity.class);
                            Pair[] pairs = new Pair[3];
                            pairs[0] = new Pair<View, String>(logo, "logoTransition");
                            pairs[1] = new Pair<View, String>(inputUser, "inputUserTransition");
                            pairs[2] = new Pair<View, String>(sendButton, "sendButtonTransition");

                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(loginActivity.this, pairs);
                            startActivity(intent, options.toBundle());
                        }
                    }
                });
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(loginActivity.this, "Erreur ! " + e.getMessage(), Toast.LENGTH_LONG).show();
                spinProgress.setVisibility(View.GONE);
                sendButton.setEnabled(true);
            }
        };

    }
}

