package onboarding.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.utils.Essentials;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.emergencyalertapp.models.User;

public class VerifyPhoneNumber extends AppCompatActivity implements TextWatcher {

    private TextInputLayout user_optNumLayout;
    private EditText optNum;
    private Button verifyBtn;
    private ImageView close;

    private String authVerificationId;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DocumentReference usersDoc;
    private static final String CATEGORY = "Patient";
    private static String USER_ID;
    private static String DATE_CREATED;
    private static  String TIMEZONE;
    private boolean isRecordsAvailable = false;
    private Essentials essentials;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_phone_number);

        setUp();
    }

    private void setUp(){
        firebaseAuth = FirebaseAuth.getInstance();
        authVerificationId = getIntent().getStringExtra("AuthVerificationId");

        db = FirebaseFirestore.getInstance();
        essentials = new Essentials();
        USER_ID = firebaseAuth.getUid();
        DATE_CREATED = essentials.getCurrentDate();
        TIMEZONE = essentials.getTimeZone();
        user = new User(null, USER_ID, null ,CATEGORY, null ,isRecordsAvailable, null);


        user_optNumLayout = findViewById(R.id.user_optNumLayout);
        optNum = findViewById(R.id.optNum);
        optNum.addTextChangedListener(this);
        verifyBtn = findViewById(R.id.verifyBtn);
        verifyBtn.setOnClickListener(v -> {
            essentials.startProgressLoader(this, "Verifying...");
            verifyOPTCode();
        });
        close = findViewById(R.id.close);
        close.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void verifyOPTCode(){
        if(TextUtils.isEmpty(optNum.getText().toString())){
            user_optNumLayout.setError("Enter verification code");
        }
        else if(optNum.getText().toString().trim().length() < 6){
            user_optNumLayout.setError("Invalid code");
        }
        else {
            String otp = optNum.getText().toString().trim();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(authVerificationId, otp);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNumber.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            essentials.dismissProgressBar();
                            String usersDocumentPath = "Users/".concat(firebaseAuth.getUid());
                            usersDoc = db.document(usersDocumentPath);
                            usersDoc.set(user);
                            Intent intent = new Intent(VerifyPhoneNumber.this, CreateProfile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            // ...
                        } else {
                            essentials.dismissProgressBar();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                user_optNumLayout.setError("Invalid code, please try again");
                            }
                        }
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(s != null){
            user_optNumLayout.setError(null);
        }
    }
}
