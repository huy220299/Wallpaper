package com.example.nvhuy.wallpaper.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.developer.kalert.KAlertDialog;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.Ultility.DialogUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edPhone, edPass, edConfirmPass, edEmail;
    private RelativeLayout btnLogin;
    private Button btnSignUp;
    private ImageView checkPhone, checkPass, checkConfirmPass, checkEmail;
    private DatabaseReference mDatabase;
    private ImageView btnEmailHelp;
    private ImageView btnBack;
    private ImageView btnShowPass1, btnShowPass2;
    private boolean SHOW_PASS = false;
    private boolean CHECK_PHONE, CHECK_PASS, CHECK_CONFIRM_PASS, CHECK_EMAIL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        init();
        action();
    }

    private void init() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        edPhone = findViewById(R.id.edPhone);
        edPass = findViewById(R.id.edPass);
        edConfirmPass = findViewById(R.id.edConfirmPass);
        edEmail = findViewById(R.id.edEmail);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        checkPhone = findViewById(R.id.checkPhone);
        checkPass = findViewById(R.id.checkPass);
        checkConfirmPass = findViewById(R.id.checkConfirmPass);
        checkEmail = findViewById(R.id.checkEmail);
        btnEmailHelp = findViewById(R.id.btnEmailHelp);
        btnBack = findViewById(R.id.btnBack);
        btnShowPass1 = findViewById(R.id.btnShowPass1);
        btnShowPass2 = findViewById(R.id.btnShowPass2);
    }

    private void action() {
        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnShowPass1.setOnClickListener(this);
        btnShowPass2.setOnClickListener(this);

        //Phone
        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    checkPhone.setVisibility(View.VISIBLE);
                    CHECK_PHONE = validatePhone(s.toString());

                    if (validatePhone(s.toString())) {
                        checkPhone.setImageResource(R.drawable.ok);
                    } else {
                        checkPhone.setImageResource(R.drawable.error);
                    }
                } else {
                    checkPhone.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Password
        edPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    btnShowPass1.setVisibility(View.VISIBLE);
                    checkPass.setVisibility(View.VISIBLE);
                    CHECK_PASS = validatePassword(s.toString());

                    if (validatePassword(s.toString())) {
                        checkPass.setImageResource(R.drawable.ok);
                    } else {
                        checkPass.setImageResource(R.drawable.error);
                    }
                } else {
                    btnShowPass1.setVisibility(View.GONE);
                    checkPass.setVisibility(View.INVISIBLE);
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

        //Email
        edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    checkEmail.setVisibility(View.VISIBLE);
                    CHECK_EMAIL = validateEmail(s.toString());

                    if (validateEmail(s.toString())) {
                        checkEmail.setImageResource(R.drawable.ok);
                    } else {
                        checkEmail.setImageResource(R.drawable.error);
                    }
                } else {
                    checkEmail.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validatePhone(String s) {
        String phone_regex = "";
        return s.length() == 10;
    }

    private boolean validatePassword(String s) {
        return s.length() >= 8 && s.length() <= 30;
    }

    private boolean validateConfirmPassword(String s) {
        return s.equals(edPass.getText().toString().trim()) && validatePassword(edPass.getText().toString().trim());
    }

    private boolean validateEmail(String s) {
        String email_regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return s.matches(email_regex);
    }

    private void signUp() {

        if (CHECK_PHONE && CHECK_PASS && CHECK_CONFIRM_PASS && CHECK_EMAIL) {

            final String phone_number = edPhone.getText().toString().trim();
            final String password = edPass.getText().toString().trim();
            final String email = edEmail.getText().toString().trim();


            //Todo: check account exist
            mDatabase.child("accounts").child(phone_number).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {

                        Intent intent = new Intent(SignUpActivity.this, LoginPhoneVerifyActivity.class);
                        intent.putExtra("phone_number", phone_number);
                        intent.putExtra("password", password);
                        intent.putExtra("email", email);
                        intent.putExtra("type_verify", "sign_up");
                        startActivity(intent);
                    } else {
                        DialogUtils.showDialog(SignUpActivity.this, getString(R.string.phone_number_used), KAlertDialog.ERROR_TYPE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } else {
            DialogUtils.showDialog(SignUpActivity.this, getString(R.string.invalid_infor), KAlertDialog.ERROR_TYPE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShowPass1:
            case R.id.btnShowPass2:
                SHOW_PASS = !SHOW_PASS;
                if (SHOW_PASS){
                    edPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    edPass.setSelection(edPass.getText().toString().length());
                    edConfirmPass.setSelection(edConfirmPass.getText().toString().length());
                } else{
                    edPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    edPass.setSelection(edPass.getText().toString().length());
                    edConfirmPass.setSelection(edConfirmPass.getText().toString().length());
                }
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnLogin:
                onBackPressed();
                break;
            case R.id.btnSignUp:
                signUp();
                break;
            case R.id.btnEmailHelp:
                break;
        }
    }
}

