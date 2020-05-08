package com.example.emergencyalertapp.screens.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.emergencyalertapp.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import onboarding.screens.CreateProfile;
import onboarding.screens.Login;
import onboarding.screens.LoginWithEmail;

public class SendAlert extends AppCompatActivity implements BottomSheetDialog.BottomSheetListener{

    private RelativeLayout emailBtn;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_alert);

        firebaseAuth = FirebaseAuth.getInstance();
//        findViewById(R.id.emailBtn).setOnClickListener(v -> {
//            signOut();
//        });

//        startActivity(new Intent(this, CreateProfile.class));
        findViewById(R.id.sosBtn).setOnClickListener(v -> {
            showBottomSheet();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){
            startActivity(new Intent(this, Login.class));
            finish();
        }
    }

    private void showBottomSheet(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
        bottomSheetDialog.show(getSupportFragmentManager(), "BottomSheet");
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
//        LoginManager.getInstance().logOut();
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        startActivity(new Intent(SendAlert.this, Login.class));
//                        finish();
//                    }
//                });
    }

    @Override
    public void onButtonClicked(String text) {
//        if(text.equals("cancelled")){
//            signOut();
//        }
        Snackbar.make(findViewById(R.id.sosBtn), text, Snackbar.LENGTH_SHORT).show();
    }
}















