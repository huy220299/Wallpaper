package com.example.nvhuy.wallpaper.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import com.developer.kalert.KAlertDialog;
import com.example.nvhuy.wallpaper.Activity.BaseActivity;
import com.example.nvhuy.wallpaper.Activity.MainActivity;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.Ultility.DialogUtils;
import com.example.nvhuy.wallpaper.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;



public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText edPhone, edPassword;
    private RelativeLayout btnForgot;
    private TextView btnSignUp;
    private Button btnLogin;
    private ImageView btnShowPass;
    private boolean SHOW_PASS = false;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";
    private LinearLayout btnLoginEmail, btnLoginFacebook;
    private String URL = "https://firebasestorage.googleapis.com/v0/b/shoes-shop-app.appspot.com/o/user.png?alt=media&token=ac6648aa-232a-472f-b4e5-d3fcf1ef691f";

    //-----------Variant for LOGIN EMAIL-----------//
    private static final int RC_SIGN_IN = 9001;
    //---------------------------------------------//

    //-----------Variant for LOGIN FACEBOOK--------//
//    private CallbackManager mCallbackManager;
    public ProgressDialog mProgressDialog;
    //---------------------------------------------//

    private SharedPreferences sp;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        action();

    }

    private void init() {
        sp = getSharedPreferences("settings", MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        edPhone = findViewById(R.id.edPhone);
        edPassword = findViewById(R.id.edPassword);
        btnForgot = findViewById(R.id.btnForgot);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        btnShowPass = findViewById(R.id.btnShowPass);
        btnLoginEmail = findViewById(R.id.btnLoginEmail);
        btnLoginFacebook = findViewById(R.id.btnLoginFacebook);

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
    }

    private void action() {
        String lang = sp.getString("lang", "en");
        loadLocal(lang);

        String mode = sp.getString("mode", "light");
        if (mode.equals("light")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }


        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
        btnShowPass.setOnClickListener(this);
        btnLoginEmail.setOnClickListener(this);
        btnLoginFacebook.setOnClickListener(this);

        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    btnShowPass.setVisibility(View.VISIBLE);
                } else {
                    btnShowPass.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Todo: Facebook login
        //----------------Facebook Login--------------//
//        mCallbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//            }
//        });

        //Todo: Gmail Login

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void login() {

        if (edPhone.getText().toString().trim().isEmpty() || edPassword.getText().toString().trim().isEmpty()) {
            DialogUtils.showDialog(LoginActivity.this,  getString(R.string.password_or_phone_not_correct), KAlertDialog.ERROR_TYPE);
            return;
        }

        final String phone = edPhone.getText().toString().trim();
        final String password = edPassword.getText().toString().trim();


        mDatabase.child("accounts").child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String pass = (String) dataSnapshot.child("password").getValue();
                    if (password.equals(pass)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("phone_number",  phone);
//                        intent.putExtra("type_verify", "login");
                        startActivity(intent);
                    } else {
                        DialogUtils.showDialog(LoginActivity.this,  getString(R.string.password_or_phone_not_correct), KAlertDialog.ERROR_TYPE);
                    }
                    return;
                } else {
                    DialogUtils.showDialog(LoginActivity.this,  getString(R.string.password_or_phone_not_correct), KAlertDialog.ERROR_TYPE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnShowPass:
                SHOW_PASS = !SHOW_PASS;
                if (!SHOW_PASS) {
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                edPassword.setSelection(edPassword.getText().toString().length());
                break;
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnForgot:
                intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSignUp:
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLoginEmail:

                break;
            case R.id.btnLoginFacebook:
                break;
        }
    }
    public void loadLocal(String lang){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(lang)); // API 17+ only.
        }
        res.updateConfiguration(conf, dm);
    }

}
