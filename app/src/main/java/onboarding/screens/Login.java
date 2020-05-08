package onboarding.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.screens.patient.SendAlert;
import com.example.emergencyalertapp.utils.Essentials;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    private RelativeLayout fbBtn, googleBtn, emailBtn, phoneBtn, registerHereLabel;
    private ImageView close;

    //variables
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private final static int RC_SIGN_IN = 123;
//    private ProgressDialog progressDialog;
    private static final String EMAIL = "email";
    private static final String FB_PUBLIC_PROFILE = "public_profile";
    private Essentials essentials;

    private CallbackManager callbackManager;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setup();

        createGoogleSignInRequest();
        facebookLogin();
    }

    private void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        essentials = new Essentials();

        fbBtn = findViewById(R.id.fbBtn);
        fbBtn.setOnClickListener(v -> {
            essentials.startProgressLoader(this, "Loading...");
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(EMAIL, FB_PUBLIC_PROFILE));
        });
        googleBtn = findViewById(R.id.googleBtn);
        googleBtn.setOnClickListener(v -> {
            essentials.startProgressLoader(this, "Loading...");
            signIn();
        });
        phoneBtn = findViewById(R.id.phoneBtn);
        phoneBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpWithPhoneNumber.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        emailBtn = findViewById(R.id.emailBtn);
        emailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginWithEmail.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        registerHereLabel = findViewById(R.id.registerHereLabel);
        registerHereLabel.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignUpOptions.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        close = findViewById(R.id.close);
        close.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void createGoogleSignInRequest(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void facebookLogin(){
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Retrieving access token using the LoginResult
                essentials.dismissProgressBar();
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                essentials.dismissProgressBar();
                Snackbar.make(findViewById(R.id.rootLayout), "Sign up with facebook Cancelled", Snackbar.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {
                essentials.dismissProgressBar();
                Snackbar.make(findViewById(R.id.rootLayout), "Sign up with facebook failed", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                essentials.dismissProgressBar();
                Snackbar.make(findViewById(R.id.rootLayout), "Google sign in failed", Snackbar.LENGTH_SHORT).show();
                System.out.println("========> Google sign in failed <<<<<  " + e.getLocalizedMessage());
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        essentials.dismissProgressBar();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(Login.this, "Log in successful", Toast.LENGTH_SHORT).show();
                        Intent gotoSendAlert = new Intent(Login.this, SendAlert.class);
                        startActivity(gotoSendAlert);
                        finish();
                    }
                    else {
                        essentials.dismissProgressBar();
                        // If sign in fails, display a message to the user.
                        System.out.println("--------signInWithCredential:failure------- " + task.getException());
                        Snackbar.make(findViewById(R.id.rootLayout), "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void firebaseAuthWithFacebook(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        essentials.dismissProgressBar();
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(Login.this, "Log in successful", Toast.LENGTH_SHORT).show();
                        Intent gotoSendAlert = new Intent(Login.this, SendAlert.class);
                        startActivity(gotoSendAlert);
                        finish();
                    }
                    else {
                        essentials.dismissProgressBar();
                        // If sign in fails, display a message to the user.
                        System.out.println("--------signInWithCredential:failure------- " + task.getException());
                        Snackbar.make(findViewById(R.id.rootLayout), "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}

























