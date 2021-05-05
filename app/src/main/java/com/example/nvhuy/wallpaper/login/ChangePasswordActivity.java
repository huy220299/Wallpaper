package com.example.nvhuy.wallpaper.login;


import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.kalert.KAlertDialog;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.Ultility.DialogUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uID;

    private ImageView btnBack;
    private EditText edNewPass, edConfirmPass;
    private ImageView checkNewPass, checkConfirmPass;
    private ImageView btnShowPass1, btnShowPass2;
    private boolean SHOW_PASS = false;
    private boolean CHECK_NEW_PASS, CHECK_CONFIRM_PASS;
    private Button btnChange;
    private String PHONE_NUMBER;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_change_password);

        init();
        action();

    }

    private void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();

        PHONE_NUMBER = getIntent().getStringExtra("phone_number");

        btnBack = findViewById(R.id.btnBack);
        edNewPass = findViewById(R.id.edNewPass);
        edConfirmPass = findViewById(R.id.edConfirmPass);
        checkNewPass = findViewById(R.id.checkNewPass);
        checkConfirmPass = findViewById(R.id.checkConfirmPass);
        btnShowPass1 = findViewById(R.id.btnShowPass1);
        btnShowPass2 = findViewById(R.id.btnShowPass2);
        btnChange = findViewById(R.id.btnChange);
    }

    private void action() {
        btnBack.setOnClickListener(this);
        checkNewPass.setOnClickListener(this);
        checkConfirmPass.setOnClickListener(this);
        btnShowPass1.setOnClickListener(this);
        btnShowPass2.setOnClickListener(this);
        btnChange.setOnClickListener(this);

        edNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    btnShowPass1.setVisibility(View.VISIBLE);
                    checkNewPass.setVisibility(View.VISIBLE);
                    CHECK_NEW_PASS = validatePassword(s.toString());

                    if (validatePassword(s.toString())) {
                        checkNewPass.setImageResource(R.drawable.ok);
                    } else {
                        checkNewPass.setImageResource(R.drawable.error);
                    }
                } else {
                    btnShowPass1.setVisibility(View.GONE);
                    checkNewPass.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Confirm pass
        edConfirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    btnShowPass2.setVisibility(View.VISIBLE);
                    checkConfirmPass.setVisibility(View.VISIBLE);
                    CHECK_CONFIRM_PASS = validateConfirmPassword(s.toString());

                    if (validateConfirmPassword(s.toString())) {
                        checkConfirmPass.setImageResource(R.drawable.ok);
                    } else {
                        checkConfirmPass.setImageResource(R.drawable.error);
                    }
                } else {
                    btnShowPass2.setVisibility(View.GONE);
                    checkConfirmPass.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean validatePassword(String s) {
        return s.length() >= 8 && s.length() <= 30;
    }
    private boolean validateConfirmPassword(String s) {
        return s.equals(edNewPass.getText().toString().trim()) && validatePassword(edNewPass.getText().toString().trim());
    }


    private void changeNewPass(){
        if (CHECK_NEW_PASS && CHECK_CONFIRM_PASS){
            String password = edNewPass.getText().toString().trim();
            mDatabase.child("accounts").child(PHONE_NUMBER).child("password").setValue(password);
            mDatabase.child("users").child(uID).child("password").setValue(password)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            DialogUtils.showDialog(ChangePasswordActivity.this, getString(R.string.change_success), KAlertDialog.SUCCESS_TYPE);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onBackPressed();
                                }
                            }, 1000);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            DialogUtils.showDialog(ChangePasswordActivity.this, getString(R.string.change_failed), KAlertDialog.ERROR_TYPE);
                        }
                    });
        } else {
            DialogUtils.showDialog(ChangePasswordActivity.this,  getString(R.string.invalid_password), KAlertDialog.ERROR_TYPE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowPass1:
            case R.id.btnShowPass2:
                SHOW_PASS = !SHOW_PASS;
                if (SHOW_PASS) {
                    edNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    edNewPass.setSelection(edNewPass.getText().toString().length());
                    edConfirmPass.setSelection(edConfirmPass.getText().toString().length());
                } else {
                    edNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    edNewPass.setSelection(edNewPass.getText().toString().length());
                    edConfirmPass.setSelection(edConfirmPass.getText().toString().length());
                }
                break;

            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnChange:
                changeNewPass();
                break;
        }
    }

    @Override
    protected void onPause() {
        mAuth.signOut();
        super.onPause();
    }
}
