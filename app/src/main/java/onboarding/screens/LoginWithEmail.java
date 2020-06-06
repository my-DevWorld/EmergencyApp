package onboarding.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.service_providers.ServiceProvider;
import com.example.emergencyalertapp.screens.patient.PatientActivities;
import com.example.emergencyalertapp.screens.service_provider.SPHomeScreen;
import com.example.emergencyalertapp.utils.Essentials;
import com.example.emergencyalertapp.utils.UserClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

import com.example.emergencyalertapp.models.User;

public class LoginWithEmail extends AppCompatActivity {

    //widgets
    private EditText userEmailLogin, userPasswordLogin;
    private TextInputLayout userEmailRegLayout, userPasswordRegLayout;
    private RelativeLayout btnLogin;
    private ImageView close;
    private TextView forgottenPassword;

    //member fields
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private CollectionReference usersCollection;
    private static final String TAG = "LoginWithEmail";
    private static final String CATEGORY = "Patient";
    private User user;
    private Essentials essentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_with_email);

        setup();
    }

    private void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection("Users");
        essentials = new Essentials();

        userEmailRegLayout = findViewById(R.id.userEmailRegLayout);
        userPasswordRegLayout = findViewById(R.id.userPasswordRegLayout);
        close = findViewById(R.id.close);
        close.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        userEmailLogin = findViewById(R.id.userEmailLogin);
        userEmailLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null){
                    userEmailRegLayout.setError(null);
                }
            }
        });
        userPasswordLogin = findViewById(R.id.userPasswordLogin);
        userPasswordLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null){
                    userPasswordRegLayout.setError(null);
                }
            }
        });
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            essentials.hideSoftKeyboard(this, btnLogin);
            validateForm();
        });

        forgottenPassword = findViewById(R.id.forgottenPassword);
        forgottenPassword.setOnClickListener(v -> {
            essentials.hideSoftKeyboard(this, forgottenPassword);
            resetPassword();
        });
    }

    private void validateForm(){
        if(TextUtils.isEmpty(userEmailLogin.getText())){
            userEmailRegLayout.setError("Enter your email address");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmailLogin.getText().toString()).matches()){
            userEmailRegLayout.setError("Enter a valid email address");
            return;
        }
        if(TextUtils.isEmpty(userPasswordLogin.getText().toString())){
            userPasswordRegLayout.setError("Enter your password");
            return;
        }
        if(!TextUtils.isEmpty(userPasswordLogin.getText().toString())){
            String passwordCount = userPasswordLogin.getText().toString();
            if(passwordCount.length() < 6){
                userPasswordRegLayout.setError("Password must be at least 6 characters");
                return;
            }
        }
        logIn();
    }

    private void logIn() {
        essentials.startProgressLoader(this, "Logging in...");
        String email = userEmailLogin.getText().toString().trim();
        String password = userPasswordLogin.getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser.isEmailVerified()){
                        essentials.dismissProgressBar();
                        usersCollection.whereEqualTo("userID", firebaseAuth.getUid())
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        user = documentSnapshot.toObject(User.class);
                                    }
                                    if(user.getCategory().equals(CATEGORY)){
                                        if(user.isRecordsAvailable()) {
                                            Intent intent = new Intent(LoginWithEmail.this, PatientActivities.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Intent intent = new Intent(LoginWithEmail.this, CreateProfile.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    else {
                                        Intent intent = new Intent(LoginWithEmail.this, SPHomeScreen.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                    else {
                        FirebaseAuth.getInstance().signOut();
                        essentials.dismissProgressBar();
                        Snackbar.make(findViewById(R.id.rootLayout), "Email address not verified.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("VERIFY", v -> resendVerificationLink()).show();
                    }
                }).addOnFailureListener(e -> {
                    if(Objects.equals(e.getMessage(), "A network error (such as timeout, interrupted connection or unreachable host) has occurred.")){
                        essentials.dismissProgressBar();
                        Snackbar.make(findViewById(R.id.rootLayout), "Network error occurred, check network and try again.", Snackbar.LENGTH_LONG).show();

                    }
                    else {
                        essentials.dismissProgressBar();
                        Snackbar.make(findViewById(R.id.rootLayout), "Something went wrong, please check email address or password and try again.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

    private void resetPassword(){
        if(!TextUtils.isEmpty(userEmailLogin.getText().toString())){
            firebaseAuth.sendPasswordResetEmail(userEmailLogin.getText().toString().trim())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Snackbar.make(findViewById(R.id.rootLayout), "Password reset link sent.", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                        }
                        else {
                            Snackbar.make(findViewById(R.id.rootLayout), "Something went wrong, please check email address and try again.", Snackbar.LENGTH_LONG)
                                    .setAction("RETRY", v -> resetPassword()).show();
                        }
                    });
        }
        else {
            userEmailRegLayout.setError("Enter your email address");
        }
    }

    private void resendVerificationLink(){
        if(TextUtils.isEmpty(userEmailLogin.getText().toString()) ||
                TextUtils.isEmpty(userPasswordLogin.getText().toString())){
            Snackbar.make(findViewById(R.id.rootLayout), "Please enter your email and password", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(firebaseUser != null){
            if(firebaseUser.getEmail().equals(userEmailLogin.getText().toString())){
                essentials.startProgressLoader(this, "Sending...");
                firebaseUser.sendEmailVerification().addOnSuccessListener(aVoid -> {
                    essentials.dismissProgressBar();
                    Snackbar.make(findViewById(R.id.rootLayout), "Verification link sent to this email.", Snackbar.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    essentials.dismissProgressBar();
                    Snackbar.make(findViewById(R.id.rootLayout), e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "onFailure: " + e.getMessage());
                });
            }
            else {
                Snackbar.make(findViewById(R.id.rootLayout), "Please log in with your email and password.", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginWithEmail.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

//    private void getServiceProviderDetails(){
//        getServiceProvider = db.collection("Service Providers")
//                .document(firebaseUser.getUid());
//        getServiceProvider.addSnapshotListener(this, (documentSnapshot, e) -> {
//            if(e != null){
//                return;
//            }
//            provider = documentSnapshot.toObject(ServiceProvider.class);
//            new Handler().postDelayed(() -> {
//                System.out.println("?????????????????????????????? " + provider.toString());
//            },400);
//        });
//    }
}




















