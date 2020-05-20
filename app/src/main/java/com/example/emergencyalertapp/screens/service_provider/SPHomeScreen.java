package com.example.emergencyalertapp.screens.service_provider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.emergencyalertapp.R;
import com.google.firebase.auth.FirebaseAuth;

import onboarding.screens.CreateProfile;
import onboarding.screens.Login;
import onboarding.screens.LoginWithEmail;

public class SPHomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_p_home_screen);

        TextView textView = findViewById(R.id.id);
        textView.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SPHomeScreen.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
}
