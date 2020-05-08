package onboarding.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.utils.Essentials;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

import com.example.emergencyalertapp.models.User;


public class SignUpOptions extends AppCompatActivity {

    private RelativeLayout fbBtn, googleBtn, phoneBtn, emailBtn, registerHereLabel;
    private ImageView close;
    private LoginButton fb_login_button;

    //variables
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;
    private final static int RC_SIGN_IN = 123;
    private ProgressDialog progressDialog;
    private static final String EMAIL = "email";
    private static final String FB_PUBLIC_PROFILE = "public_profile";
//    private ProgressBar progressBar;

    private CallbackManager callbackManager;
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
        setContentView(R.layout.signup_options);

        setup();

        createGoogleSignInRequest();
        facebookLogin();
    }

    private void setup(){
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        essentials = new Essentials();


        DATE_CREATED = essentials.getCurrentDate();
        TIMEZONE = essentials.getTimeZone();

        fbBtn = findViewById(R.id.fbBtn);
        fb_login_button = findViewById(R.id.login_button);
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
            Intent intent = new Intent(this, Register.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        registerHereLabel = findViewById(R.id.registerHereLabel);
        registerHereLabel.setOnClickListener(v -> {
            onBackPressed();
            finish();
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
                System.out.println("Google sign in failed" + e);
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
                        String usersDocumentPath = "Users/".concat(firebaseAuth.getUid());
                        USER_ID = firebaseAuth.getUid();
                        user = new User(USER_ID, CATEGORY, DATE_CREATED, TIMEZONE, isRecordsAvailable);
                        usersDoc = db.document(usersDocumentPath);
                        usersDoc.set(user);
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(SignUpOptions.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        Intent gotoCreateProfile = new Intent(SignUpOptions.this, CreateProfile.class);
                        startActivity(gotoCreateProfile);
                        finish();
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        essentials.dismissProgressBar();
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
                        String usersDocumentPath = "Users/".concat(firebaseAuth.getUid());
                        USER_ID = firebaseAuth.getUid();
                        user = new User(USER_ID, CATEGORY, DATE_CREATED, TIMEZONE, isRecordsAvailable);
                        usersDoc = db.document(usersDocumentPath);
                        usersDoc.set(user);
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(SignUpOptions.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        Intent gotoCreateProfile = new Intent(SignUpOptions.this, CreateProfile.class);
                        startActivity(gotoCreateProfile);
                        finish();
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        essentials.dismissProgressBar();
                        System.out.println("--------signInWithCredential:failure------- " + task.getException());
                        Snackbar.make(findViewById(R.id.rootLayout), "Authentication Failed", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
}

























