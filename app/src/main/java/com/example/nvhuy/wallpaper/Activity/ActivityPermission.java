package com.example.nvhuy.wallpaper.Activity;

import androidx.annotation.NonNull;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.example.nvhuy.wallpaper.R;

import net.igenius.customcheckbox.CustomCheckBox;

public class ActivityPermission extends BaseActivity {
    CustomCheckBox checkBox_storage, checkBox_wallpaper;
    Button btn_start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        checkBox_storage = findViewById(R.id.checkbox_storage);
        btn_start = findViewById(R.id.btn_start);
        btn_start.setOnClickListener(v -> {
            if (checkBox_storage.isChecked()){
                if (checkPermission()){
                    startActivity(new Intent(ActivityPermission.this, MainActivity.class));
                    finish();
                }else {
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, 111);
                }
            }
            else {
                new KAlertDialog(ActivityPermission.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Please check permission!")
                        .setConfirmText("OK")
                        .show();
            }

        });


    }


    public void openSettingPermission(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(),null);
        intent.setData(uri);
        startActivity(intent);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {

//                new KAlertDialog(ActivityPermission.this, KAlertDialog.WARNING_TYPE)
//                        .setCancelText("Cancel")
//                        .setCancelClickListener(kAlertDialog1 -> {
//                            kAlertDialog1.cancel();
//                                })
//                        .setTitleText("Permission denied !")
//                        .setContentText("Open setting and return")
//                        .setConfirmText("Open now")
//                        .setConfirmClickListener(kAlertDialog -> {
//                           openSettingPermission();
//                        })
//                        .show();
            }
        }
    }
}