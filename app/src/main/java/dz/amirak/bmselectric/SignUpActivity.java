package dz.amirak.bmselectric;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameInput, lastnameInput, mailInput;
    private Button sendButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fStore;
    private String UserID;
    private ProgressBar spinProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.nameInput = findViewById(R.id.nameInput);
        this.lastnameInput = findViewById(R.id.lastnameInput);
        this.mailInput = findViewById(R.id.mailInput);
        this.sendButton = findViewById(R.id.sendButton);
        this.spinProgress = findViewById(R.id.spinProgress);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        UserID = firebaseAuth.getCurrentUser().getUid();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = nameInput.getText().toString().trim();
                String lastnameString = lastnameInput.getText().toString().trim();
                String mailString = mailInput.getText().toString().trim();
                if(nameString.isEmpty()){
                    nameInput.setError("Veuillez saisir votre nom !");
                    return;
                }
                if(lastnameString.isEmpty()){
                    lastnameInput.setError("Veuillez saisir votre prenom !");
                    return;
                }
                if(mailString.isEmpty()){
                    mailInput.setError("Veuillez saisir votre adresse email !");
                    return;
                }
                spinProgress.setVisibility(View.VISIBLE);
                sendButton.setEnabled(false);
                DocumentReference documentReference = fStore.collection("marketers").document(UserID);
                Map<String, Object> marketer = new HashMap<>();
                marketer.put("nom", nameString);
                marketer.put("prenom", lastnameString);
                marketer.put("email", mailString);
                documentReference.set(marketer).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUpActivity.this, "Donnee enregistree !", Toast.LENGTH_LONG).show();
                        spinProgress.setVisibility(View.GONE);
                        sendButton.setEnabled(true);
                        Intent intentHome = new Intent(SignUpActivity.this,homeActivity.class);
                        startActivity(intentHome);
                        finish();
                    }
                });

            }
        });
    }
}