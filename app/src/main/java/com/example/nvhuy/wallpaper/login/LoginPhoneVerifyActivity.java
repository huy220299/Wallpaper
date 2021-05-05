package com.example.nvhuy.wallpaper.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.developer.kalert.KAlertDialog;
import com.example.nvhuy.wallpaper.Activity.MainActivity;
import com.example.nvhuy.wallpaper.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;


import es.dmoral.toasty.Toasty;


public class LoginPhoneVerifyActivity extends AppCompatActivity {
    private TextView tvPhoneNumber;
    private ImageView btnBack;
    private TextView tvResend;
    private LinearLayout btnContinue;
    private PinEntryEditText edPinEntry;

    private static final String TAG ="PhoneVerifyActivity";

    private KAlertDialog pDialog;
    private KAlertDialog dialog;
    private DatabaseReference mDatabase;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String PHONE_NUMBER = "", PASSWORD = "", EMAIL = "";
    private String PHONE_FULL;
    private String uID;
    private String TYPE_VERIFY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verify);


        PHONE_NUMBER = getIntent().getStringExtra("phone_number");
        PHONE_FULL = "+84" + PHONE_NUMBER.substring(1);
        PASSWORD = getIntent().getStringExtra("password");
        EMAIL = getIntent().getStringExtra("email");
        TYPE_VERIFY = getIntent().getStringExtra("type_verify");

        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        btnBack = findViewById(R.id.btnBack);
        tvResend = findViewById(R.id.tvResend);
        btnContinue = findViewById(R.id.btnContinue);
        edPinEntry = findViewById(R.id.edPinEntry);

//        dialog = new SweetAlertDialog(LoginPhoneVerifyActivity.this, SweetAlertDialog.SUCCESS_TYPE);
//        dialog.setTitleText(getString(R.string.code_sent))
//                .hideConfirmButton()
//                .show();
        tvPhoneNumber.setText(PHONE_NUMBER);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(PHONE_FULL, mResendToken);
                dialog = new KAlertDialog(LoginPhoneVerifyActivity.this, KAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText(getString(R.string.code_resent))
                        .show();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumberWithCode(mVerificationId, edPinEntry.getText().toString());
            }
        });

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]
                pDialog = new KAlertDialog(LoginPhoneVerifyActivity.this, KAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.start_color));
                pDialog.setTitleText(getString(R.string.loading));
                pDialog.setCancelable(false);
                pDialog.show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed"+ e.getMessage());


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    edPinEntry.setError(getString(R.string.invalid_phone_number));
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.quote_exceeded),
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

            }
        };
        // [END phone_auth_callbacks]

        startPhoneNumberVerification(PHONE_FULL);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            // [END verify_with_code]
            signInWithPhoneAuthCredential(credential);
        }catch (IllegalArgumentException e){
            Toasty.error(LoginPhoneVerifyActivity.this, getString(R.string.verify_wrong), Toasty.LENGTH_SHORT).show();
        }

    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            if (!TYPE_VERIFY.equalsIgnoreCase("forgot_password")) {
                                dialog = new KAlertDialog(LoginPhoneVerifyActivity.this, KAlertDialog.SUCCESS_TYPE);
                                dialog.setTitleText(getString(R.string.login_success))
                                        .show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                }, 1000);
                            }

                            final FirebaseUser user = task.getResult().getUser();

                            uID = user.getUid();

                            switch (TYPE_VERIFY.toLowerCase()) {
                                case "login":
                                case "sign_up":
                                    mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            boolean check = false;
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.getKey().equals(uID)) {
                                                    check = true;
                                                    break;
                                                }
                                            }
                                            if (check) {
                                                Intent intent = new Intent(LoginPhoneVerifyActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(LoginPhoneVerifyActivity.this, InfoActivity.class);
                                                intent.putExtra("phone_number", PHONE_NUMBER);
                                                intent.putExtra("password", PASSWORD);
                                                intent.putExtra("email", EMAIL);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    break;
                                case "forgot_password":
                                    Intent intent = new Intent(LoginPhoneVerifyActivity.this, ChangePasswordActivity.class);
                                    intent.putExtra("phone_number", PHONE_NUMBER);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    break;
                            }
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(getApplication(), "Signin Failed!!", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                dialog = new KAlertDialog(LoginPhoneVerifyActivity.this, KAlertDialog.ERROR_TYPE);
                                dialog.setTitleText(getString(R.string.verify_wrong))
                              .show();
                                // [END_EXCLUDE]
                            }
                        }
                    }
                });
    }
    // [END sign_in_with_phone]


    @Override
    protected void onStop() {
        super.onStop();
        if(pDialog!=null)
            pDialog.dismiss();
        if(dialog!=null)
            dialog.dismiss();
    }
}
