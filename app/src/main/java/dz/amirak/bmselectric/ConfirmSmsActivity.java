package dz.amirak.bmselectric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ConfirmSmsActivity extends AppCompatActivity {

    private Button sendButton;
    private ImageView logo;
    private ProgressBar spinProgress;
    private EditText inputUser;

    private FirebaseAuth numAuth;
    private String OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_sms);

        numAuth = FirebaseAuth.getInstance();
        OTP = getIntent().getStringExtra("auth");

        this.sendButton = findViewById(R.id.sendButton);
        this.spinProgress = findViewById(R.id.spinProgress);
        this.inputUser = findViewById(R.id.inputUser);
        this.logo = findViewById(R.id.logo);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeVerification = inputUser.getText().toString();
                if(!codeVerification.isEmpty()){
                    spinProgress.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP , codeVerification);
                    numAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(ConfirmSmsActivity.this, SignUpActivity.class);
                                Pair[] pairs = new Pair[3];
                                pairs[0] = new Pair<View, String>(logo, "logoTransition");
                                pairs[1] = new Pair<View, String>(inputUser, "inputUserTransition");
                                pairs[2] = new Pair<View, String>(sendButton, "sendButtonTransition");
                                Toast.makeText(ConfirmSmsActivity.this, "Authentification Reussi ! ", Toast.LENGTH_LONG).show();
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ConfirmSmsActivity.this, pairs);
                                startActivity(intent, options.toBundle());
                            }else{
                                Toast.makeText(ConfirmSmsActivity.this, "Erreur lors de l'inscription ! ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    return;
                }else{
                    inputUser.setError("Veuillez saisir le code recu !");
                }




            }
        });

    }


}


/*
//************************************************************************************************************************
//                                      PARTIE A REFAIRE EN PHONE AUTHENTIFICATION !!
//************************************************************************************************************************
                numAuth.createUserWithEmailAndPassword("moncef@mouzaoui.com", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ConfirmSmsActivity.this, "Compte cree avec succes!", Toast.LENGTH_LONG).show();
                            spinProgress.setVisibility(View.GONE);
                            Intent intent = new Intent(ConfirmSmsActivity.this, SignUpActivity.class);

                        }else{
                            Toast.makeText(ConfirmSmsActivity.this, "Erreur ! "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            spinProgress.setVisibility(View.GONE);
                        }
                    }
                });
//************************************************************************************************************************
 */
