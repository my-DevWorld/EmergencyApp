package onboarding.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.utils.Essentials;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.emergencyalertapp.models.patient.User;

public class Register extends AppCompatActivity {

    //widgets
    private EditText userEmailReg, userPasswordReg;
    private TextInputLayout userEmailRegLayout, userPasswordRegLayout;
    private RelativeLayout btnRegister;
    private ImageView close;

    //member fields
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private CollectionReference patientRef;
    private DocumentReference usersDoc;
    private DocumentReference patientsDoc;
    private static final String TAG = "Register";
    private static final String CATEGORY = "Patient";
    private static String USER_ID;
    private static String DATE_CREATED;
    private static  String TIMEZONE;
    private boolean isRecordsAvailable = false;
    private Essentials essentials;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        setup();
    }

    private void setup() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        essentials = new Essentials();

        userEmailRegLayout = findViewById(R.id.userEmailRegLayout);
        userPasswordRegLayout = findViewById(R.id.userPasswordRegLayout);
        close = findViewById(R.id.close);
        close.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        userEmailReg = findViewById(R.id.userEmailReg);
        userEmailReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    userEmailRegLayout.setError(null);
                }
            }
        });
        userPasswordReg = findViewById(R.id.userPasswordReg);
        userPasswordReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    userPasswordRegLayout.setError(null);
                }
            }
        });
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            essentials.hideSoftKeyboard(this, btnRegister);
            validateForm();
        });
    }

    private void signUp() {
        essentials.startProgressLoader(this, "Registering...");

        String email = userEmailReg.getText().toString().trim();
        String password = userPasswordReg.getText().toString().trim();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    firebaseUser = firebaseAuth.getCurrentUser();

                    String usersDocumentPath = "Users/".concat(firebaseAuth.getUid());
//                    String patientDocumentPath = "Patients/".concat(firebaseAuth.getUid());

                    USER_ID = firebaseAuth.getUid();
                    DATE_CREATED = essentials.getCurrentDate();
                    TIMEZONE = essentials.getTimeZone();

                    User user = new User(email, USER_ID, null ,CATEGORY, DATE_CREATED, TIMEZONE, null ,isRecordsAvailable, null);

                    usersDoc = db.document(usersDocumentPath);
//                    patientsDoc = db.document(patientDocumentPath);
                    usersDoc.set(user).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            firebaseUser.sendEmailVerification().addOnSuccessListener(aVoid -> {
                                FirebaseAuth.getInstance().signOut();
                                essentials.dismissProgressBar();
                                Snackbar.make(findViewById(R.id.rootLayout), "Verification link sent to this email address.", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("OK", v -> {
                                            Intent intent = new Intent(Register.this, LoginWithEmail.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        }).show();
                            })
                                    .addOnFailureListener(e -> {
                                        Snackbar.make(findViewById(R.id.rootLayout), "Account created, please log in", Snackbar.LENGTH_LONG).show();
                                        Log.d(TAG, "onFailure: " + e.getMessage());
                                    });
                        }
                        else {
                            essentials.dismissProgressBar();
                            Snackbar.make(findViewById(R.id.rootLayout), "Verification link sent to this email address.", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("OK", v -> {
                                        Intent intent = new Intent(Register.this, LoginWithEmail.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }).show();
                        }
                    });
//                    if(!patientRef1.getPath().isEmpty()){
//                        System.out.println(">>>>>>>>>>>>>>>>>>>>> Path created: " + usersDoc + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//                        System.out.println(">>>>>>>>>>>>>>>>>>>>> Path created: " + patientRef1.getParent().getPath() + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//                        System.out.println(">>>>>>>>>>>>>>>>>>>>> Document path created: " + patientRef1.getParent().getParent().getPath() + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//                        System.out.println(">>>>>>>>>>>>>>>>>>>>> Date created: " + dateFormat.format(currentDate).concat(" " + timeZone.getID()) + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//
//                    }
                })
                .addOnFailureListener(e -> {
                    essentials.dismissProgressBar();
                    Snackbar.make(findViewById(R.id.rootLayout), e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
        });
    }

//    private void createAlert(String alertTitle, String alertMessage, String positiveText){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(alertTitle)
//                .setMessage(alertMessage)
//                .setPositiveButton(positiveText, null)
//                .create().show();
//    }

    private void validateForm() {
        if (TextUtils.isEmpty(userEmailReg.getText())) {
            userEmailRegLayout.setError("Please enter your email address");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmailReg.getText().toString()).matches()) {
            userEmailRegLayout.setError("Please enter a valid email address");
            return;
        }
        if (TextUtils.isEmpty(userPasswordReg.getText().toString())) {
            userPasswordRegLayout.setError("Please enter your password");
            return;
        }
        if (!TextUtils.isEmpty(userPasswordReg.getText().toString())) {
            String passwordCount = userPasswordReg.getText().toString();
            if (passwordCount.length() < 6) {
                userPasswordRegLayout.setError("Password must be at least 6 characters");
                return;
            }
        }
        signUp();
    }

//    public static void hideSoftKeyboard(Activity activity, View view) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
//    }
}



























