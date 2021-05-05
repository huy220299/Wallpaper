package com.example.nvhuy.wallpaper.login;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.kalert.KAlertDialog;
import com.example.nvhuy.wallpaper.Activity.MainActivity;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {
    private ImageView btnBack;
    private CircleImageView imgAvatar;
    private TextView tvAvatar;
    private EditText edName;
    private LinearLayout btnContinue;

    private String URL = "https://firebasestorage.googleapis.com/v0/b/shoes-shop-app.appspot.com/o/user.png?alt=media&token=ac6648aa-232a-472f-b4e5-d3fcf1ef691f";

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String uID;
    private StorageReference mStorage;
    private Uri filePath;
    private KAlertDialog pDialog;

    public static final int PICK_IMAGE = 1;

    private String PHONE_NUMBER = "", PASSWORD = "", EMAIL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
        action();
    }
    private void init() {

        PHONE_NUMBER = getIntent().getExtras().getString("phone_number");
        PASSWORD = getIntent().getExtras().getString("password");
        EMAIL = getIntent().getExtras().getString("email");

        btnBack = findViewById(R.id.btnBack);
        imgAvatar = findViewById(R.id.imgAvatar);
        tvAvatar = findViewById(R.id.tvAvatar);
        edName = findViewById(R.id.edName);
        btnContinue = findViewById(R.id.btnContinue);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getCurrentUser().getUid();

    }

    private void action(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvAvatar.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = edName.getText().toString().trim();
                if (name.isEmpty()) {
                    edName.setError(getString(R.string.enter_your_name));
                    return;
                }

                if (filePath != null) {

                    pDialog = new KAlertDialog(InfoActivity.this, KAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.start_color));
                    pDialog.setTitleText(getString(R.string.loading));
                    pDialog.setCancelable(false);
                    pDialog.show();

                    final StorageReference ref;
                    ref = mStorage.child("users").child(uID);

                    ref.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    pDialog.cancel();
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(final Uri uri) {
                                            URL = uri.toString();

                                            User user = new User(name, URL, PHONE_NUMBER, PASSWORD, EMAIL);
                                            mDatabase.child("users").child(uID).setValue(user);

                                            mDatabase.child("accounts").child(PHONE_NUMBER).child("password").setValue(PASSWORD);
                                            mDatabase.child("accounts").child(PHONE_NUMBER).child("uid").setValue(uID);
                                            mDatabase.child("accounts").child(PHONE_NUMBER).child("timestamp").setValue(System.currentTimeMillis());

                                            Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pDialog.cancel();
                                }
                            });
                }
                else {
                    User user = new User(name, URL, PHONE_NUMBER, PASSWORD, EMAIL);
                    mDatabase.child("users").child(uID).setValue(user);

                    mDatabase.child("accounts").child(PHONE_NUMBER).child("password").setValue(PASSWORD);
                    mDatabase.child("accounts").child(PHONE_NUMBER).child("uid").setValue(uID);
                    mDatabase.child("accounts").child(PHONE_NUMBER).child("timestamp").setValue(System.currentTimeMillis());

                    Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE & resultCode == RESULT_OK) {
            filePath = data.getData();
            imgAvatar.setImageURI(data.getData());
        }
    }
}
