package com.example.nvhuy.wallpaper.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class ForgotActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edPhone;
    private Button btnSend;
    private ImageView btnBack;
    private ImageView checkPhone;
    private boolean CHECK_PHONE;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        getSupportActionBar().hide();
        init();
        action();
    }

    private void init() {
        edPhone = findViewById(R.id.edPhone);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        checkPhone = findViewById(R.id.checkPhone);
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    private void action() {
        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);

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
    }

    private boolean validatePhone(String s) {
        String phone_regex = "";
        return s.length() == 10;
    }

    private void requestNewPassword(){
        if (CHECK_PHONE){
            final String phone = edPhone.getText().toString().trim();
            mDatabase.child("accounts").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Intent intent = new Intent(ForgotActivity.this, LoginPhoneVerifyActivity.class);
                        intent.putExtra("phone_number",  phone);
                        intent.putExtra("type_verify", "forgot_password");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        DialogUtils.showDialog(ForgotActivity.this,  getString(R.string.phone_was_not_used), KAlertDialog.ERROR_TYPE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            DialogUtils.showDialog(ForgotActivity.this, getString(R.string.invalid_phone), KAlertDialog.ERROR_TYPE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:
                requestNewPassword();
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }
}
