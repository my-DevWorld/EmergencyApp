package onboarding.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.emergencyalertapp.R;
import com.example.emergencyalertapp.models.patient.MedicalRecord;
import com.example.emergencyalertapp.models.patient.PatientProfile;
import com.example.emergencyalertapp.models.patient.EmergencyContact;
import com.example.emergencyalertapp.screens.patient.PatientActivities;
import com.example.emergencyalertapp.utils.DatePickerFragment;
import com.example.emergencyalertapp.utils.Essentials;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateProfile extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    //widgets
    private ImageView pInfoExpandLess, pInfoExpandMore, medRecExpandLess,
            medRecExpandMore, contactExpandLess, contactExpandMore;
    private TextInputEditText userFullNameEditTxt, userDOBEditTxt, userPhoneNumberEditTxt, userAddressEditTxt, userAllergiesEditTxt,
            contactFullNameEditTxt, contactRelationEditTxt, contactPhoneNumbEditTxt, contactAddressEditTxt;
    private TextInputLayout userFullNameLayout, userDOBLayout, userPhoneNumLayout, userAddressLayout,
            contactFullNameLayout, contactRelationLayout, contactPhoneNumbLayout, contactAddressLayout;
    private EditText userWeightEditTxt, userHeightEditTxt, bloodGroupEditTxt;
    private LinearLayout personalInfo, medRec, contact, genderLayout;
    private RelativeLayout header1, header2, header3;
    private CheckBox femaleCheckbox, maleCheckbox;
    private RelativeLayout submitBtn, doneBtn;
    private NestedScrollView scrollView;
    private Spinner bloodGroupSpinner;

    //fields
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference usersDoc;
    private DocumentReference patientsDocProfile;
    private DocumentReference patientsDocMedicalRecord;
    private DocumentReference patientsDocEmergencyContact;
    private String usersDocumentPath;
    private String patientsProfileDocumentPath;
    private String patientsMedicalRecordDocumentPath;
    private String patientsMedicalRecordsDocumentPath;
    private Essentials essentials;
    private String userName;
    private String userDateOfBirth;
    private String userGender = "";
    private String userPhoneNumber;
    private String userResidentialAddress;
    private String userWeight;
    private String userHeight;
    private String bloodGroup;
    private String userAllergies;
    private String nameOfEmergencyContact;
    private String relationship;
    private String userEmergencyContactPhoneNumb;
    private String userEmergencyContactResidentialAddress;
    String userFirstName;
    String userLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
        setup();
    }

    private void setup() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        usersDocumentPath = "Users/".concat(firebaseAuth.getUid());
        patientsProfileDocumentPath = "Patients/".concat(firebaseAuth.getUid()).concat("/Profile/data");
        patientsMedicalRecordDocumentPath = "Patients/".concat(firebaseAuth.getUid()).concat("/MedicalRecord/data");
        patientsMedicalRecordsDocumentPath = "Patients/".concat(firebaseAuth.getUid()).concat("/EmergencyContact/data");
        scrollView = findViewById(R.id.scrollView);
        essentials = new Essentials();

        header1 = findViewById(R.id.header1);
        userFullNameLayout = findViewById(R.id.userFullNameLayout);
        userDOBLayout = findViewById(R.id.userDOBLayout);
        genderLayout = findViewById(R.id.genderLayout);
        userPhoneNumLayout = findViewById(R.id.userPhoneNumLayout);
        userAddressLayout = findViewById(R.id.userAddressLayout);
        userFullNameEditTxt = findViewById(R.id.userFullName);
        userFullNameEditTxt.addTextChangedListener(fullNameEditTextWatcher);
        userDOBEditTxt = findViewById(R.id.userDOB);
        userDOBEditTxt.setInputType(InputType.TYPE_NULL);
        userDOBEditTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                essentials.hideSoftKeyboard(this, userDOBEditTxt);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        userDOBEditTxt.setOnClickListener(v -> {
            essentials.hideSoftKeyboard(this, userDOBEditTxt);
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });
        userDOBEditTxt.addTextChangedListener(userDOBTextWatcher);
        userPhoneNumberEditTxt = findViewById(R.id.userContact);
        userPhoneNumberEditTxt.addTextChangedListener(userPhoneNumberTextWatcher);
        userAddressEditTxt = findViewById(R.id.userAddress);
        userAddressEditTxt.addTextChangedListener(userAddressTextWatcher);
        personalInfo = findViewById(R.id.personalInfo);

        header2 = findViewById(R.id.header2);
        userWeightEditTxt = findViewById(R.id.userWeight);
        userWeightEditTxt.addTextChangedListener(userWeightTextWatcher);
        userHeightEditTxt = findViewById(R.id.userHeight);
        userHeightEditTxt.addTextChangedListener(userHeightTextWatcher);
        bloodGroupEditTxt = findViewById(R.id.bloodGroup);
        bloodGroupEditTxt.addTextChangedListener(userBloodGroup);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);

        List<String> bloodGroups = new ArrayList<>();
        bloodGroups.add(0, "Select blood group");
        bloodGroups.add("A+");
        bloodGroups.add("A-");
        bloodGroups.add("B+");
        bloodGroups.add("B-");
        bloodGroups.add("O+");
        bloodGroups.add("O-");
        bloodGroups.add("AB+");
        bloodGroups.add("AB-");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bloodGroups);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(arrayAdapter);
        bloodGroupSpinner.setOnTouchListener((v, event) -> {
            userHeightEditTxt.clearFocus();
            essentials.hideSoftKeyboard(this, bloodGroupSpinner);
            essentials.scrollDown(scrollView, bloodGroupSpinner);
            return false;
        });
        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!parent.getItemAtPosition(position).toString().equals("Select blood group")){
                    bloodGroup = parent.getItemAtPosition(position).toString();
                }
                else {
                    bloodGroup = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        userAllergiesEditTxt = findViewById(R.id.userAllergies);

        header3 = findViewById(R.id.header3);
        contactFullNameLayout = findViewById(R.id.contactFullNameLayout);
        contactRelationLayout = findViewById(R.id.contactRelationLayout);
        contactPhoneNumbLayout = findViewById(R.id.contactPhoneNumbLayout);
        contactAddressLayout = findViewById(R.id.contactAddressLayout);
        contactFullNameEditTxt = findViewById(R.id.contactFullName);
        contactFullNameEditTxt.addTextChangedListener(contactFullNameTextWatcher);
        contactRelationEditTxt = findViewById(R.id.contactRelation);
        contactRelationEditTxt.addTextChangedListener(contactRelationTextWatcher);
        contactPhoneNumbEditTxt = findViewById(R.id.contactPhoneNumb);
        contactPhoneNumbEditTxt.addTextChangedListener(contactPhoneNumbTextWatcher);
        contactAddressEditTxt = findViewById(R.id.contactAddress);
        contactAddressEditTxt.addTextChangedListener(contactAddressTextWatcher);

        submitBtn = findViewById(R.id.submitBtn);

        contactFullNameEditTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (!TextUtils.isEmpty(contactFullNameEditTxt.getText())
                        && !TextUtils.isEmpty(contactRelationEditTxt.getText())
                        && !TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())
                        && !TextUtils.isEmpty(contactAddressEditTxt.getText())) {
                    doneBtn.setVisibility(View.VISIBLE);
                }
                else {
                    doneBtn.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        contactRelationEditTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!TextUtils.isEmpty(contactRelationEditTxt.getText())
                    && !TextUtils.isEmpty(contactFullNameEditTxt.getText())
                    && !TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())
                    && !TextUtils.isEmpty(contactAddressEditTxt.getText())) {
                doneBtn.setVisibility(View.VISIBLE);
            }
            else {
                doneBtn.setVisibility(View.GONE);
                submitBtn.setVisibility(View.INVISIBLE);
            }
        });
        contactPhoneNumbEditTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (!TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())
                    && !TextUtils.isEmpty(contactFullNameEditTxt.getText())
                    && !TextUtils.isEmpty(contactRelationEditTxt.getText())
                    && !TextUtils.isEmpty(contactAddressEditTxt.getText())) {
                doneBtn.setVisibility(View.VISIBLE);
            }
            else {
                doneBtn.setVisibility(View.GONE);
                submitBtn.setVisibility(View.INVISIBLE);
            }
        });
        contactAddressEditTxt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (!TextUtils.isEmpty(contactAddressEditTxt.getText())
                        && !TextUtils.isEmpty(contactFullNameEditTxt.getText())
                        && !TextUtils.isEmpty(contactRelationEditTxt.getText())
                        && !TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())) {
                    doneBtn.setVisibility(View.VISIBLE);
                }
                else {
                    doneBtn.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        contactFullNameEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null
                        && !TextUtils.isEmpty(contactRelationEditTxt.getText())
                        && !TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())
                        && !TextUtils.isEmpty(contactAddressEditTxt.getText())) {
                    doneBtn.setVisibility(View.VISIBLE);
                } else {
                    doneBtn.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        contactRelationEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null
                        && !TextUtils.isEmpty(contactFullNameEditTxt.getText())
                        && !TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())
                        && !TextUtils.isEmpty(contactAddressEditTxt.getText())) {
                    doneBtn.setVisibility(View.VISIBLE);
                } else {
                    doneBtn.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        contactPhoneNumbEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null
                        && !TextUtils.isEmpty(contactFullNameEditTxt.getText())
                        && !TextUtils.isEmpty(contactRelationEditTxt.getText())
                        && !TextUtils.isEmpty(contactAddressEditTxt.getText())) {
                    doneBtn.setVisibility(View.VISIBLE);
                } else {
                    doneBtn.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        contactAddressEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null
                        && !TextUtils.isEmpty(contactFullNameEditTxt.getText())
                        && !TextUtils.isEmpty(contactRelationEditTxt.getText())
                        && !TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())) {
                    doneBtn.setVisibility(View.VISIBLE);
                } else {
                    doneBtn.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        submitBtn.setOnClickListener(v -> {
            essentials.startProgressLoader(this, "creating profile...");
            formValidation();
        });
        doneBtn = findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(v -> {
            essentials.hideSoftKeyboard(this, doneBtn);
            if (!TextUtils.isEmpty(contactFullNameEditTxt.getText())
                    && !TextUtils.isEmpty(contactRelationEditTxt.getText())
                    && !TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())
                    && !TextUtils.isEmpty(contactAddressEditTxt.getText())) {
                contactExpandMore.setVisibility(View.VISIBLE);
                contactExpandLess.setVisibility(View.GONE);
                contact.setVisibility(View.GONE);
                submitBtn.setVisibility(View.VISIBLE);
                greenHeader(header3);
            } else {
                redHeader(header3);
                Snackbar.make(findViewById(R.id.rootLayout), "Please complete form", Snackbar.LENGTH_LONG).show();
            }
        });

        pInfoExpandLess = findViewById(R.id.pInfoExpandLess);
        pInfoExpandLess.setOnClickListener(v -> {
            infoSection();
        });
        pInfoExpandMore = findViewById(R.id.pInfoExpandMore);
        pInfoExpandMore.setOnClickListener(v -> {
            infoSection();
        });
        header1.setOnClickListener(v -> {
            infoSection();
        });

        medRec = findViewById(R.id.medRec);
        medRecExpandLess = findViewById(R.id.medRecExpandLess);
        medRecExpandLess.setOnClickListener(v -> {
            medRecordsSection();
        });
        medRecExpandMore = findViewById(R.id.medRecExpandMore);
        medRecExpandMore.setOnClickListener(v -> {
            medRecordsSection();
        });
        header2.setOnClickListener(v -> {
            medRecordsSection();
        });

        contact = findViewById(R.id.contact);
        contactExpandLess = findViewById(R.id.contactExpandLess);
        contactExpandLess.setOnClickListener(v -> {
            contactInfoSection();
        });
        contactExpandMore = findViewById(R.id.contactExpandMore);
        contactExpandMore.setOnClickListener(v -> {
            contactInfoSection();
        });

        header3.setOnClickListener(v -> {
            contactInfoSection();
        });

        femaleCheckbox = findViewById(R.id.femaleCheckbox);
        femaleCheckbox.setOnClickListener(this);
        maleCheckbox = findViewById(R.id.maleCheckbox);
        maleCheckbox.setOnClickListener(this);
    }

    private void formValidation() {
        String[] userFullNameSplit;
        if (TextUtils.isEmpty(userFullNameEditTxt.getText())) {
            essentials.dismissProgressBar();
            personalInfo.setVisibility(View.VISIBLE);
            pInfoExpandLess.setVisibility(View.VISIBLE);
            pInfoExpandMore.setVisibility(View.GONE);
            redHeader(header1);
            userFullNameLayout.setError("Please enter your full name");
            userFullNameEditTxt.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(userFullNameEditTxt.getText())) {
            userFullNameSplit = userFullNameEditTxt.getText().toString().trim().split("\\s+");
            if (userFullNameSplit.length == 1) {
                essentials.dismissProgressBar();
                personalInfo.setVisibility(View.VISIBLE);
                pInfoExpandLess.setVisibility(View.VISIBLE);
                pInfoExpandMore.setVisibility(View.GONE);
                redHeader(header1);
                userFullNameLayout.setError("Please enter your full name");
                userFullNameEditTxt.requestFocus();
                return;
            }
            else {
                userName = userFullNameEditTxt.getText().toString().trim();
                userFirstName = userFullNameSplit[0];
                userLastName = userFullNameSplit[1];
            }
        }

        if (TextUtils.isEmpty(userDOBEditTxt.getText())) {
            essentials.dismissProgressBar();
            personalInfo.setVisibility(View.VISIBLE);
            pInfoExpandLess.setVisibility(View.VISIBLE);
            pInfoExpandMore.setVisibility(View.GONE);
            redHeader(header1);
            userDOBLayout.setError("Please enter your date of birth");
            userDOBEditTxt.requestFocus();
            return;
        }
        else {
            userDateOfBirth = userDOBEditTxt.getText().toString().trim();
        }

        if (userGender == null || userGender.isEmpty()) {
            essentials.dismissProgressBar();
            personalInfo.setVisibility(View.VISIBLE);
            pInfoExpandLess.setVisibility(View.VISIBLE);
            pInfoExpandMore.setVisibility(View.GONE);
            redHeader(header1);
            if (Build.VERSION_CODES.LOLLIPOP_MR1 < 22) {
                genderLayout.setBackground(getDrawable(R.drawable.error_border_line));
            } else {
                genderLayout.setBackgroundResource(R.drawable.error_border_line);
            }
            Snackbar.make(findViewById(R.id.rootLayout), "Please check your gender", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(userPhoneNumberEditTxt.getText())) {
            essentials.dismissProgressBar();
            personalInfo.setVisibility(View.VISIBLE);
            pInfoExpandLess.setVisibility(View.VISIBLE);
            pInfoExpandMore.setVisibility(View.GONE);
            redHeader(header1);
            userPhoneNumLayout.setError("Please enter your phone number");
            userPhoneNumLayout.requestFocus();
            return;
        }

        if (userPhoneNumberEditTxt.getText().toString().length() < 10) {
            essentials.dismissProgressBar();
            personalInfo.setVisibility(View.VISIBLE);
            pInfoExpandLess.setVisibility(View.VISIBLE);
            pInfoExpandMore.setVisibility(View.GONE);
            redHeader(header1);
            userPhoneNumLayout.setError("Please enter a valid number");
            userPhoneNumLayout.requestFocus();
            return;
        }
        else {
            userPhoneNumber = userPhoneNumberEditTxt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(userAddressEditTxt.getText())) {
            essentials.dismissProgressBar();
            personalInfo.setVisibility(View.VISIBLE);
            pInfoExpandLess.setVisibility(View.VISIBLE);
            pInfoExpandMore.setVisibility(View.GONE);
            redHeader(header1);
            userAddressLayout.setError("Please enter your residential address");
            userAddressLayout.requestFocus();
            return;
        }
        else {
            userResidentialAddress = userAddressEditTxt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(userWeightEditTxt.getText())) {
            showError(userWeightEditTxt);
            Snackbar.make(findViewById(R.id.rootLayout), "Please enter your weight", Snackbar.LENGTH_LONG).show();
            return;
        }
        else {
            userWeight = userWeightEditTxt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(userHeightEditTxt.getText())) {
            showError(userHeightEditTxt);
            Snackbar.make(findViewById(R.id.rootLayout), "Please enter your height", Snackbar.LENGTH_LONG).show();
            return;
        }
        else {
            userHeight = userHeightEditTxt.getText().toString().trim();
        }

        if(!TextUtils.isEmpty(userAllergiesEditTxt.getText())){
            userAllergies = userAllergiesEditTxt.getText().toString().trim();
        }
        else {
            userAllergies = null;
        }

        if (TextUtils.isEmpty(contactFullNameEditTxt.getText())) {
            essentials.dismissProgressBar();
            contactExpandMore.setVisibility(View.GONE);
            contactExpandLess.setVisibility(View.VISIBLE);
            contact.setVisibility(View.VISIBLE);
            redHeader(header3);
            contactFullNameLayout.setError("Please enter contact full name");
            contactFullNameEditTxt.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(contactFullNameEditTxt.getText())) {
            String[] fullNameSplit = contactFullNameEditTxt.getText().toString().trim().split("\\s+");
            if (fullNameSplit.length == 1) {
                essentials.dismissProgressBar();
                contactExpandMore.setVisibility(View.GONE);
                contactExpandLess.setVisibility(View.VISIBLE);
                contact.setVisibility(View.VISIBLE);
                redHeader(header3);
                contactFullNameLayout.setError("Please enter contact full name");
                contactFullNameEditTxt.requestFocus();
                return;
            }
            else {
                nameOfEmergencyContact = contactFullNameEditTxt.getText().toString().trim();
            }
        }

        if (TextUtils.isEmpty(contactRelationEditTxt.getText())) {
            essentials.dismissProgressBar();
            contactExpandMore.setVisibility(View.GONE);
            contactExpandLess.setVisibility(View.VISIBLE);
            contact.setVisibility(View.VISIBLE);
            redHeader(header3);
            contactRelationLayout.setError("Please enter the relation with contact");
            contactRelationEditTxt.requestFocus();
            return;
        }
        else {
            relationship = contactRelationEditTxt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())) {
            essentials.dismissProgressBar();
            contactExpandMore.setVisibility(View.GONE);
            contactExpandLess.setVisibility(View.VISIBLE);
            contact.setVisibility(View.VISIBLE);
            redHeader(header3);
            contactPhoneNumbLayout.setError("Please enter your phone number");
            contactPhoneNumbEditTxt.requestFocus();
            return;
        }

        if (contactPhoneNumbEditTxt.getText().toString().length() < 10) {
            essentials.dismissProgressBar();
            contactExpandMore.setVisibility(View.GONE);
            contactExpandLess.setVisibility(View.VISIBLE);
            contact.setVisibility(View.VISIBLE);
            redHeader(header3);
            contactPhoneNumbLayout.setError("Please enter a valid number");
            contactPhoneNumbEditTxt.requestFocus();
            return;
        }
        else {
            userEmergencyContactPhoneNumb = contactPhoneNumbEditTxt.getText().toString().trim();
        }

        if (TextUtils.isEmpty(contactAddressEditTxt.getText())) {
            essentials.dismissProgressBar();
            contactExpandMore.setVisibility(View.GONE);
            contactExpandLess.setVisibility(View.VISIBLE);
            contact.setVisibility(View.VISIBLE);
            redHeader(header3);
            contactAddressLayout.setError("Please enter your residential address");
            contactAddressEditTxt.requestFocus();
            return;
        }
        else {
            userEmergencyContactResidentialAddress = contactAddressEditTxt.getText().toString().trim();
        }

        PatientProfile patientProfile = new PatientProfile(userName, userDateOfBirth,
                userGender, userPhoneNumber, userResidentialAddress);
        MedicalRecord medicalRecord = new MedicalRecord(userWeight, userHeight, bloodGroup, userAllergies);
        EmergencyContact emergencyContact = new EmergencyContact(nameOfEmergencyContact, relationship,
                userEmergencyContactPhoneNumb, userEmergencyContactResidentialAddress);
        createUserProfile(patientProfile, medicalRecord, emergencyContact);

    }

    private void createUserProfile(PatientProfile patientProfile, MedicalRecord medicalRecord, EmergencyContact emergencyContact) {
        String userName = userFirstName.substring(0,1).concat("_").concat(userLastName);
        usersDoc = db.document(usersDocumentPath);
        patientsDocProfile = db.document(patientsProfileDocumentPath);
        patientsDocMedicalRecord = db.document(patientsMedicalRecordDocumentPath);
        patientsDocEmergencyContact = db.document(patientsMedicalRecordsDocumentPath);
        usersDoc.update("recordsAvailable", true).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                patientsDocProfile.set(patientProfile);
                patientsDocMedicalRecord.set(medicalRecord);
                patientsDocEmergencyContact.set(emergencyContact);
                usersDoc.update("username", userName);
                essentials.dismissProgressBar();
                Snackbar.make(findViewById(R.id.rootLayout), "Profile created successfully", Snackbar.LENGTH_LONG).show();
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(this, PatientActivities.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }, 1250);
            }
            else {
                Snackbar.make(findViewById(R.id.rootLayout), "Something went wrong", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void switchDateUpLoadedFilter(CheckBox checkBox1, CheckBox checkBox2) {
        if (checkBox1.isChecked()) {
            checkBox1.setClickable(false);
            checkBox2.setClickable(true);
            checkBox2.setChecked(false);
        }
        else if (checkBox2.isChecked()) {
            checkBox2.setClickable(false);
            checkBox1.setClickable(true);
            checkBox2.setChecked(true);
        }

        if (Build.VERSION_CODES.LOLLIPOP_MR1 < 22) {
            genderLayout.setBackground(getDrawable(R.drawable.border_line));
        } else {
            genderLayout.setBackgroundResource(R.drawable.border_line);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.femaleCheckbox:
                switchDateUpLoadedFilter(femaleCheckbox, maleCheckbox);
                userGender = "female";
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<< " + userGender);
                break;

            case R.id.maleCheckbox:
                switchDateUpLoadedFilter(maleCheckbox, femaleCheckbox);
                userGender = "male";
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<< " + userGender);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        super.onBackPressed();
        Intent intent = new Intent(CreateProfile.this, LoginWithEmail.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        userDateOfBirth = dateFormat.format(c.getTime());
        userDOBEditTxt.setText(userDateOfBirth);
    }

    private TextWatcher fullNameEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                userFullNameLayout.setError(null);
            }
        }
    };

    private TextWatcher userDOBTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                userDOBLayout.setError(null);
            }
        }
    };

    private TextWatcher userPhoneNumberTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                userPhoneNumLayout.setError(null);
            }
        }
    };

    private TextWatcher userAddressTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                userAddressLayout.setError(null);
            }
        }
    };

    private TextWatcher userWeightTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                if (Build.VERSION_CODES.LOLLIPOP_MR1 < 22) {
                    userWeightEditTxt.setBackground(getDrawable(R.drawable.border_line));
                } else {
                    userWeightEditTxt.setBackgroundResource(R.drawable.border_line);
                }
            }
        }
    };

    private TextWatcher userHeightTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                if (Build.VERSION_CODES.LOLLIPOP_MR1 < 22) {
                    userHeightEditTxt.setBackground(getDrawable(R.drawable.border_line));
                } else {
                    userHeightEditTxt.setBackgroundResource(R.drawable.border_line);
                }
            }
        }
    };

    private TextWatcher userBloodGroup = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                if (Build.VERSION_CODES.LOLLIPOP_MR1 < 22) {
                    bloodGroupEditTxt.setBackground(getDrawable(R.drawable.border_line));
                } else {
                    bloodGroupEditTxt.setBackgroundResource(R.drawable.border_line);
                }
            }
        }
    };

    private TextWatcher contactFullNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                contactFullNameLayout.setError(null);
            }
        }
    };

    private TextWatcher contactRelationTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                contactRelationLayout.setError(null);
            }
        }
    };

    private TextWatcher contactPhoneNumbTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                contactPhoneNumbLayout.setError(null);
            }
        }
    };

    private TextWatcher contactAddressTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !TextUtils.isEmpty(s.toString())) {
                contactAddressLayout.setError(null);
            }
        }
    };

    private void greenHeader(RelativeLayout layout) {
        if (Build.VERSION_CODES.LOLLIPOP_MR1 < 22) {
            layout.setBackground(getDrawable(R.color.greenBtn));
        } else {
            layout.setBackgroundResource(R.color.greenBtn);
        }
    }

    private void redHeader(RelativeLayout layout) {
        if (Build.VERSION_CODES.LOLLIPOP_MR1 < 22) {
            layout.setBackground(getDrawable(R.color.primaryButtonColorActive));
        } else {
            layout.setBackgroundResource(R.color.primaryButtonColorActive);
        }
    }

    private void showError(View view){
        if (Build.VERSION_CODES.LOLLIPOP_MR1 < 22) {
            view.setBackground(getDrawable(R.drawable.error_border_line));
        } else {
            view.setBackgroundResource(R.drawable.error_border_line);
        }
    }

    private void infoSection(){
        if(pInfoExpandLess.getVisibility() == View.VISIBLE){
            pInfoExpandLess.setVisibility(View.GONE);
            pInfoExpandMore.setVisibility(View.VISIBLE);
            personalInfo.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(userFullNameEditTxt.getText())
                    && !TextUtils.isEmpty(userDOBEditTxt.getText())
                    && !TextUtils.isEmpty(userPhoneNumberEditTxt.getText())
                    && !TextUtils.isEmpty(userAddressEditTxt.getText())
                    && (femaleCheckbox.isChecked() || maleCheckbox.isChecked())) {
                greenHeader(header1);
            }
            else {
                redHeader(header1);
            }
        }
        else {
            pInfoExpandLess.setVisibility(View.VISIBLE);
            pInfoExpandMore.setVisibility(View.GONE);
            personalInfo.setVisibility(View.VISIBLE);
            medRecExpandMore.setVisibility(View.VISIBLE);
            medRecExpandLess.setVisibility(View.GONE);
            medRec.setVisibility(View.GONE);
            contactExpandMore.setVisibility(View.VISIBLE);
            contactExpandLess.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
        }
    }

    private void medRecordsSection(){
        if(medRecExpandLess.getVisibility() == View.VISIBLE){
            medRecExpandMore.setVisibility(View.VISIBLE);
            medRecExpandLess.setVisibility(View.GONE);
            medRec.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(userWeightEditTxt.getText())
                    && !TextUtils.isEmpty(userHeightEditTxt.getText())
//                    && !TextUtils.isEmpty(bloodGroupEditTxt.getText())
                    && bloodGroup != null) {
                greenHeader(header2);
            }
            else {
                redHeader(header2);
            }
        }
        else{
            if (!TextUtils.isEmpty(userFullNameEditTxt.getText())
                    && !TextUtils.isEmpty(userDOBEditTxt.getText())
                    && !TextUtils.isEmpty(userPhoneNumberEditTxt.getText())
                    && !TextUtils.isEmpty(userAddressEditTxt.getText())
                    && (femaleCheckbox.isChecked() || maleCheckbox.isChecked())) {
                medRecExpandMore.setVisibility(View.GONE);
                medRecExpandLess.setVisibility(View.VISIBLE);
                medRec.setVisibility(View.VISIBLE);
                personalInfo.setVisibility(View.GONE);
                pInfoExpandLess.setVisibility(View.GONE);
                pInfoExpandMore.setVisibility(View.VISIBLE);
                contactExpandMore.setVisibility(View.VISIBLE);
                contactExpandLess.setVisibility(View.GONE);
                contact.setVisibility(View.GONE);
                greenHeader(header1);
            }
            else {
                redHeader(header1);
                essentials.hideSoftKeyboard(this, medRecExpandMore);
                Snackbar.make(findViewById(R.id.rootLayout), "Please fill in your personal info", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void contactInfoSection(){
        if(contactExpandLess.getVisibility() == View.VISIBLE){
            contactExpandMore.setVisibility(View.VISIBLE);
            contactExpandLess.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(contactFullNameEditTxt.getText())
                    && !TextUtils.isEmpty(contactRelationEditTxt.getText())
                    && !TextUtils.isEmpty(contactPhoneNumbEditTxt.getText())
                    && !TextUtils.isEmpty(contactAddressEditTxt.getText())) {
                doneBtn.setVisibility(View.GONE);
                redHeader(header3);
            }
            else {
                redHeader(header3);
            }
        }
        else {
            if (!TextUtils.isEmpty(userWeightEditTxt.getText())
                    && !TextUtils.isEmpty(userHeightEditTxt.getText())
//                    && !TextUtils.isEmpty(bloodGroupEditTxt.getText())
                    && bloodGroup != null) {
                contactExpandMore.setVisibility(View.GONE);
                contactExpandLess.setVisibility(View.VISIBLE);
                contact.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.GONE);
                pInfoExpandLess.setVisibility(View.GONE);
                pInfoExpandMore.setVisibility(View.VISIBLE);
                personalInfo.setVisibility(View.GONE);
                medRecExpandMore.setVisibility(View.VISIBLE);
                medRecExpandLess.setVisibility(View.GONE);
                medRec.setVisibility(View.GONE);
                essentials.scrollDown(scrollView, contact);
                greenHeader(header2);
                redHeader(header3);
            }
            else {
                redHeader(header2);
                essentials.hideSoftKeyboard(this, medRecExpandMore);
                Snackbar.make(findViewById(R.id.rootLayout), "Please fill in your medical records", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}



























