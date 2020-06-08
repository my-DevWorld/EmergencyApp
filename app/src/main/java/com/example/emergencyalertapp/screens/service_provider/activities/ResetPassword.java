package com.example.emergencyalertapp.screens.service_provider.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.utils.Essentials;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    //widgets
    private TextView editLabel, cancelBtn, updateBtn;
    private RelativeLayout currentPasswordWrapper;
    private LinearLayout resetPasswordWrapper;
    private TextInputEditText currentPassword, newPassword, confirmNewPassword;
    private TextInputLayout currentPasswordLayout, newPasswordLayout, confirmNewPasswordLayout;

    //fields
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Essentials essentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        setup();
    }

    private void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        essentials = new Essentials();
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            onBackPressed();
        });

        currentPasswordWrapper = findViewById(R.id.currentPasswordWrapper);
        resetPasswordWrapper = findViewById(R.id.resetPasswordWrapper);
        editLabel = findViewById(R.id.editLabel);
        editLabel.setOnClickListener(this);
        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);
        updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(this);

        currentPassword = findViewById(R.id.currentPassword);
        currentPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null){
                    currentPasswordLayout.setError(null);
                }
            }
        });
        newPassword = findViewById(R.id.newPassword);
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null){
                    newPasswordLayout.setError(null);
                }
            }
        });
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        confirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null){
                    confirmNewPasswordLayout.setError(null);
                }
            }
        });

        currentPasswordLayout = findViewById(R.id.currentPasswordLayout);
        newPasswordLayout = findViewById(R.id.newPasswordLayout);
        confirmNewPasswordLayout = findViewById(R.id.confirmNewPasswordLayout);
    }

    private void hideAndShowViews(RelativeLayout relativeLayout, LinearLayout linearLayout, TextView textView){
        if(textView.getId() == R.id.editLabel){
            linearLayout.setVisibility(View.VISIBLE);
//            YoYo.with(Techniques.BounceInDown)
//                    .duration(800)
//                    .delay(900)
//                    .playOn(relativeLayout);


            YoYo.with(Techniques.FadeIn)
                    .duration(350)
                    .playOn(linearLayout);
            relativeLayout.setVisibility(View.GONE);
        }
        else {
            relativeLayout.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.FadeIn)
                    .duration(350)
                    .playOn(relativeLayout);
            linearLayout.setVisibility(View.GONE);
            currentPasswordLayout.setError(null);
            currentPassword.setText(null);
            newPasswordLayout.setError(null);
            newPassword.setText(null);
            confirmNewPasswordLayout.setError(null);
            confirmNewPassword.setText(null);
        }

    }

    private void validateForm(){
        if(TextUtils.isEmpty(currentPassword.getText().toString())){
            currentPasswordLayout.setError("Please enter the current password");
            currentPasswordLayout.setEndIconActivated(false);
            currentPassword.requestFocus();
            return;
        }
        if(currentPassword.getText().toString().trim().length() < 6){
            currentPasswordLayout.setError("Password must be at least 6 characters");
            currentPasswordLayout.setEndIconActivated(false);
            currentPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(newPassword.getText().toString())){
            newPasswordLayout.setError("Please enter the new password");
            newPasswordLayout.setEndIconActivated(false);
            newPassword.requestFocus();
            return;
        }
        if(newPassword.getText().toString().trim().length() < 6){
            newPasswordLayout.setError("Password must be at least 6 characters");
            newPasswordLayout.setEndIconActivated(false);
            newPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(confirmNewPassword.getText().toString())){
            confirmNewPasswordLayout.setError("Please enter the new password again");
            confirmNewPasswordLayout.setEndIconActivated(false);
            confirmNewPassword.requestFocus();
            return;
        }
        if(confirmNewPassword.getText().toString().trim().length() < 6){
            confirmNewPasswordLayout.setError("Password must be at least 6 characters");
            confirmNewPasswordLayout.setEndIconActivated(false);
            confirmNewPassword.requestFocus();
            return;
        }
        if(!newPassword.getText().toString().trim().equals(confirmNewPassword.getText().toString().trim())){
            confirmNewPasswordLayout.setError("Password do not match");
            confirmNewPasswordLayout.setEndIconActivated(false);
            confirmNewPassword.requestFocus();
        }

        authenticateCurrentPassword(currentPassword.getText().toString().trim(), newPassword.getText().toString().trim());
    }

    private void authenticateCurrentPassword(String currentPassword, String newPassword){
        essentials.hideSoftKeyboard(this, updateBtn);
        essentials.startProgressLoader(this, "Updating password...");
        String email = firebaseUser.getEmail();
        firebaseAuth.signInWithEmailAndPassword(email, currentPassword)
                .addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                updateUserPassword(newPassword);
            }
            else {
                essentials.dismissProgressBar();
                Toast.makeText(this, "Current password is invalid", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUserPassword(String newPassword){
        firebaseUser.updatePassword(newPassword).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                essentials.dismissProgressBar();
                hideAndShowViews(currentPasswordWrapper,resetPasswordWrapper,  cancelBtn);
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_LONG).show();
            }
            else {
                essentials.dismissProgressBar();
                Toast.makeText(this, "Password update failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editLabel:
                hideAndShowViews(currentPasswordWrapper,resetPasswordWrapper, editLabel);
                break;
            case R.id.cancelBtn:
                hideAndShowViews(currentPasswordWrapper,resetPasswordWrapper,  cancelBtn);
                break;

            case R.id.updateBtn:
                validateForm();
        }
    }
}
















