package onboarding.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.screens.patient.SendAlert;
import com.example.emergencyalertapp.screens.service_provider.SPHomeScreen;
import com.example.emergencyalertapp.utils.Essentials;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

import com.example.emergencyalertapp.models.User;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SignUpWithPhoneNumber extends AppCompatActivity implements TextWatcher {

    private TextInputLayout userPhoneNumRegLayout;
    private EditText userPhoneNumReg;
    private Button sendCodeBtn;
    private ImageView close;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseFirestore db;
    private DocumentReference usersDoc;
    private static final String CATEGORY = "Patient";
    private static String USER_ID;
    private static String DATE_CREATED;
    private static  String TIMEZONE;
    private boolean isRecordsAvailable = false;
    private Essentials essentials;
    private User user;
    private CollectionReference usersCollection;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_with_phone_number);

        from = getIntent().getStringExtra("From");
        setUp();
        onPhoneVerificationCallback();
    }

    private void setUp(){
        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseAuth.getCurrentUser().getPhoneNumber();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("Users");
        essentials = new Essentials();
        DATE_CREATED = essentials.getCurrentDate();
        TIMEZONE = essentials.getTimeZone();

        close =findViewById(R.id.close);
        close.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        userPhoneNumReg = findViewById(R.id.userPhoneNumReg);
        userPhoneNumReg.addTextChangedListener(this);
        userPhoneNumRegLayout = findViewById(R.id.userPhoneNumRegLayout);
        sendCodeBtn = findViewById(R.id.sendCodeBtn);
        sendCodeBtn.setOnClickListener(v -> {
            startPhoneVerification();
//            Intent intent = new Intent(this, VerifyPhoneNumber.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseUser != null){
            Intent intent = new Intent(this, SendAlert.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void startPhoneVerification(){
        if(TextUtils.isEmpty(userPhoneNumReg.getText().toString())){
            userPhoneNumRegLayout.setError("Please enter your phone number");
        }
        else if(userPhoneNumReg.getText().toString().length() < 12){
            userPhoneNumRegLayout.setError("Phone number not valid");
        }
        else {
            essentials.startProgressLoader(this, "Requesting OPT code...");
            String userPhoneNumber = "+".concat(userPhoneNumReg.getText().toString().trim());
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    userPhoneNumber,
                    60, TimeUnit.SECONDS,
                    this,
                    callbacks);
        }
    }

    private void onPhoneVerificationCallback(){
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                essentials.dismissProgressBar();
                userPhoneNumRegLayout.setError("Verification failed, please try again");
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                new Handler().postDelayed(() -> {
                    essentials.dismissProgressBar();
                    Intent intent = new Intent(SignUpWithPhoneNumber.this, VerifyPhoneNumber.class);
                    intent.putExtra("AuthVerificationId", s);
                    startActivity(intent);
                }, 10000);
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignUpWithPhoneNumber.this, task -> {
                    if (task.isSuccessful()) {
                        if(from.equals("SignUpOptions")){
                            signIn();
                        }
                        else{
                            logInWithPhoneNumber();
                        }
//                        essentials.dismissProgressBar();
//                        String usersDocumentPath = "Users/".concat(firebaseAuth.getUid());
//                        usersDoc = db.document(usersDocumentPath);
//                        usersDoc.set(user);
//                        Intent intent = new Intent(SignUpWithPhoneNumber.this, CreateProfile.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
                        // ...
                    }
                    else {
                        essentials.dismissProgressBar();
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            essentials.dismissProgressBar();
                            userPhoneNumRegLayout.setError("Verification failed, please try again");
                        }
                    }
                });
    }

    private void signIn(){
        essentials.dismissProgressBar();
        usersCollection.whereEqualTo("userID", firebaseAuth.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.size() != 0){
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            user = documentSnapshot.toObject(User.class);
                        }
                        if(user.getCategory().equals(CATEGORY)){
                            if(user.isRecordsAvailable()) {
                                Intent intent = new Intent(SignUpWithPhoneNumber.this, SendAlert.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(SignUpWithPhoneNumber.this, CreateProfile.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Intent intent = new Intent(SignUpWithPhoneNumber.this, SPHomeScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else {
                        essentials.dismissProgressBar();
                        String usersDocumentPath = "Users/".concat(firebaseAuth.getUid());
                        USER_ID = firebaseAuth.getUid();
                        user = new User(null, USER_ID, null ,CATEGORY, null ,isRecordsAvailable, null);
                        usersDoc = db.document(usersDocumentPath);
                        usersDoc.set(user);
                        Toast.makeText(SignUpWithPhoneNumber.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        Intent gotoCreateProfile = new Intent(SignUpWithPhoneNumber.this, CreateProfile.class);
                        startActivity(gotoCreateProfile);
                        finish();
                    }
                });
    }

    private void logInWithPhoneNumber(){
        essentials.dismissProgressBar();
        usersCollection.whereEqualTo("userID", firebaseAuth.getUid())
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.size() != 0){
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    user = documentSnapshot.toObject(User.class);
                }
                if(user.getCategory().equals(CATEGORY)){
                    if(user.isRecordsAvailable()) {
                        Intent intent = new Intent(SignUpWithPhoneNumber.this, SendAlert.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SignUpWithPhoneNumber.this, CreateProfile.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Intent intent = new Intent(SignUpWithPhoneNumber.this, SPHomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
            else {
                firebaseAuth.signOut();
                Toast.makeText(this, "Sign in with phone not created", Toast.LENGTH_SHORT).show();

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
            userPhoneNumRegLayout.setError(null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(this, Login.class));
        finish();
    }
}


























