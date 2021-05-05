package com.example.nvhuy.wallpaper.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.developer.kalert.KAlertDialog;
import com.example.nvhuy.wallpaper.Service.MyDatabaseHelper;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.Ultility.Common;
import com.example.nvhuy.wallpaper.Service.VideoLiveWallpaper;
import com.example.nvhuy.wallpaper.fragment.BottomSheetDialog;
import com.example.nvhuy.wallpaper.model.Album;
import com.example.nvhuy.wallpaper.model.Image;
import com.example.nvhuy.wallpaper.model.Liked_image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class DetailActivity extends BaseActivity implements BottomSheetDialog.Callback {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "DetailActivity";
    public int RequestCode = 111;
    Button changeWallpaper;
    String url;
    ImageView user_image, button_back;
    ImageView imageView;
    ImageButton download, like, edit;
    String id_image, id_user, img_user, tittle, user, downloaded, views, set, resolution, create, size, kind;
    TextView tv_tittle, tv_user, tv_downloaded, tv_views, tv_set, tv_resolution, tv_create, tv_size, tv_follow;
    MyDatabaseHelper myDatabaseHelper;
    VideoView videoView;
    ProgressBar progressBar;
    private Bitmap result;
    Button text_download;
    Image image;
    FirebaseFirestore db;
    private FirebaseAnalytics mFirebaseAnalytics;

    BottomSheetDialog bottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "________________________________________");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        progressBar = findViewById(R.id.progress);
        text_download = findViewById(R.id.text_download);
        changeWallpaper = findViewById(R.id.btn_apply_wallpaper);
        changeWallpaper.setClickable(true);
        imageView = findViewById(R.id.imageBackground);
        download = findViewById(R.id.download);
        download.setClickable(true);
        edit = findViewById(R.id.edit);
        tv_tittle = findViewById(R.id.detail_imageName);
        tv_user = findViewById(R.id.detail_username);
        tv_downloaded = findViewById(R.id.detail_dowloaded);
        tv_views = findViewById(R.id.detail_views);
        tv_set = findViewById(R.id.detail_set);
        tv_resolution = findViewById(R.id.detail_resolution);
        tv_create = findViewById(R.id.detail_created);
        tv_size = findViewById(R.id.detail_size);
        tv_follow = findViewById(R.id.detail_follow);
        user_image = findViewById(R.id.detail_userImage);
        like = findViewById(R.id.like);
        button_back = findViewById(R.id.button_back);
        myDatabaseHelper = new MyDatabaseHelper(this);
        videoView = findViewById(R.id.videoView);
        createFirebaseStore();
        getFromIntent();

        onClick();
        setBackground();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RequestCode == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                // Nhận dữ liệu từ Intent trả về
                result = data.getParcelableExtra("bitmap");
                imageView.setImageBitmap(result);
            } else {

            }
        }
    }

    private void setBackground() {
        if (kind.equals("video")) {
            edit.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            //resize videoView
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            getApplicationContext().getDisplay();
            int width = displayMetrics.widthPixels;
            RelativeLayout.LayoutParams videoviewlp = new RelativeLayout.LayoutParams(width, height);
            videoviewlp.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            videoviewlp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            videoView.setLayoutParams(videoviewlp);
            videoView.invalidate();
            videoView.setVideoPath(url);
            videoView.start();

            videoView.setOnPreparedListener(mp -> {
                mp.setLooping(true);
                mp.setVolume(0f, 0f);
                progressBar.setVisibility(View.GONE);
            });
        } else {
            edit.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(DetailActivity.this)
                    .load(url)
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            new KAlertDialog(DetailActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Reload please,  error connection!")
                                    .setConfirmText("OK")
                                    .show();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        }
    }

    private void createFirebaseStore(){
         db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }
    private void saveFirebaseStore(){
        DocumentReference docIdRef = db.collection("Image").document(image.getId().toString());
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "Document exists!");

                    } else {
                        db.collection("Image").document(image.getId().toString()).set(image);
                        Log.d(TAG, "Document does not exist!");
                    }
                } else {
                    Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });
//        checkExist();
//        db.collection("Image").document(image.getId().toString()).set(image);

    }
    public void getAll(){
        ArrayList<Image> arrayList = new ArrayList<>();
        db.collection("Image")
                .get()
                .addOnCompleteListener((OnCompleteListener<QuerySnapshot>) task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Image image = (Image) document.toObject(Image.class);
                            arrayList.add(image);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }
    public void updatePathImage(String path){
        //update
        DocumentReference documentReference = db.collection("Image").document(image.getId().toString());

        // Set the "original" field of 2250
        documentReference
                .update("original", path)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    private void getFromIntent() {
        image = (Image) getIntent().getSerializableExtra("myObject");

        url = getIntent().getExtras().getString("image");
        id_image = getIntent().getExtras().getString("id_image");
        id_user = getIntent().getExtras().getString("id_user");
        tittle = getIntent().getExtras().getString("tittle");
        tv_tittle.setText(tittle);
        user = getIntent().getExtras().getString("user");
        tv_user.setText(user);
        downloaded = String.valueOf(getIntent().getExtras().getInt("download"));
        tv_downloaded.setText(downloaded + " Downloads");
        resolution = getIntent().getExtras().getString("resolution");
        tv_resolution.setText(resolution);
        size = getIntent().getExtras().getString("size");
        tv_size.setText(size);
        set = String.valueOf(getIntent().getExtras().getInt("set"));
        tv_set.setText(set + " Applied");
        views = String.valueOf(getIntent().getExtras().getInt("views"));
        tv_views.setText(views + " Views");
        create = getIntent().getExtras().getString("created");
        tv_create.setText(create);
        kind = getIntent().getExtras().getString("kind");
        img_user = getIntent().getExtras().getString("img_user");

        Glide.with(this).load(img_user).into(user_image);
        //set follow and liked
        if (myDatabaseHelper.checkLiked(id_image)) {
            like.setImageResource(R.drawable.liked);
        }
        if (myDatabaseHelper.checkFollow(id_user)) {
            tv_follow.setText("UNFOLLOW");
        }
        if (Common.checkDownload(id_image,DetailActivity.this)){
            download.setImageResource(R.drawable.downloaded);
        }

        saveFirebaseStore();
        //pass url to service
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("url", url);
        editor.apply();

    }

    private void onClick() {
        //back button
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.super.onBackPressed();
            }
        });
        //change Wallpaper
        changeWallpaper.setOnClickListener(v -> {
            if (kind.equals("video")) {
                VideoLiveWallpaper.setToWallPaper(DetailActivity.this);

            } else {
                Bundle bundle = new Bundle();
                bottomSheet = new BottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
                bottomSheet.setArguments(bundle);
            }
        });

        //download image
        download.setOnClickListener(v -> {
            if (checkPermission()) {
                if (Common.checkDownload(id_image,DetailActivity.this)) {
                    Toast.makeText(this, "You has downloaded", Toast.LENGTH_SHORT).show();
                } else {
                    download.setVisibility(View.GONE);
                    text_download.setVisibility(View.VISIBLE);
                    ValueAnimator animator = ValueAnimator.ofInt(0, 100);
                    animator.setDuration(5000);
                    animator.addUpdateListener(animation -> {
                        text_download.setText(animation.getAnimatedValue().toString() + "%");
                    });
                    animator.start();
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        text_download.setVisibility(View.GONE);
                        download.setVisibility(View.VISIBLE);
                        download.setImageResource(R.drawable.downloaded);
                        download.setClickable(false);
                    }, 6000);

                    //start analytics
                    Bundle params = new Bundle();
                    params.putString("image_name", tittle);
                    params.putString("full_text", url);
                    mFirebaseAnalytics.logEvent("download_image", params);
                    //end


                    if (kind.equals("video")) {
                        downloadImageNew(id_image, url, ".mp4");
                    } else {
                        downloadImageNew(id_image, url, ".jpg");
                    }
                }
            }


        });
        //like button
        like.setOnClickListener(v -> {
            if (!myDatabaseHelper.checkLiked(id_image)) {
                myDatabaseHelper.insertImage(new Liked_image(id_image, ""));//add to local database
                like.setImageResource(R.drawable.liked);
                //download in data app
                if (kind.equals("video")) {
                    downloadImageNewInApp(id_image, url, ".mp4");
                } else {
                    downloadImageNewInApp(id_image, url, ".jpg");
                }
                //start analytics
                Bundle params = new Bundle();
                params.putString("image_name", tittle);
                params.putString("full_text", url);
                mFirebaseAnalytics.logEvent("love_image", params);
                //end

            } else {
                myDatabaseHelper.deleteImage(id_image);
                like.setImageResource(R.drawable.like);
                //delete in data app
                String path = Environment.getExternalStorageDirectory().toString()
                        + File.separator + "Android"
                        + File.separator + "data"
                        + File.separator + getPackageName()
                        + File.separator + "files"
                        + File.separator + "myWallpaper"
                        + File.separator + id_image + ".jpg";
                File file = new File(path);
                file.delete();
            }

        });
        //follow
        tv_follow.setOnClickListener(v -> {
            if (!myDatabaseHelper.checkFollow(id_user)) {
                myDatabaseHelper.insertImage(new Liked_image("" + Integer.parseInt(id_user) + 1000000, id_user));//add to local database
                tv_follow.setText("UNFOLLOW");
            } else {
                myDatabaseHelper.unfollow(id_user);
                tv_follow.setText("FOLLOW");
            }
        });
        //editor
        edit.setOnClickListener(
                v -> {
                    Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                    intent.putExtra("url", url);
                    startActivityForResult(intent, RequestCode);
                });
    }

    private void downloadImageNew(String filename, String downloadUrlOfImage, String fileType) {
        try {

            File direct = new File("/downloaded");
            if (!direct.exists()) {
                direct.mkdirs();
            }

            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalFilesDir(this, "/downloaded", File.separator + filename + fileType);
            dm.enqueue(request);


        } catch (Exception e) {
            Toast.makeText(this, "Can't save image.", Toast.LENGTH_SHORT).show();
            Log.e("!!!", e.getMessage());
        }
    }

    private void downloadImageNewInApp(String filename, String downloadUrlOfImage, String fileType) {
        try {
            File direct = new File("/myWallpaper");
            if (!direct.exists()) {
                direct.mkdirs();
            }
            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                    .setDestinationInExternalFilesDir(this, "/myWallpaper", File.separator + filename + fileType);
            dm.enqueue(request);
        } catch (Exception e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            Log.e("!!!", e.getMessage());
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onButtonClicked(View view) {
        changeWallpaper.setClickable(false);
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(DetailActivity.this);
        switch (view.getId()){
            case R.id.home_img1:
                showAlertDialog(5000);
                if (result!=null){
                    try {
                        wallpaperManager.setBitmap(result, null, true, WallpaperManager.FLAG_SYSTEM);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    setHomeScreen();
                }
                break;
            case R.id.home_img2:
                showAlertDialog(10000);
                if (result!=null){
                    try {
                        wallpaperManager.setBitmap(result, null, true, WallpaperManager.FLAG_LOCK);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    setLockScreen();
                }
                break;
            case R.id.home_img3:
              showAlertDialog(10000);
                if (result!=null){
                    try {
                        wallpaperManager.setBitmap(result);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    setHomeScreen();
                    setLockScreen();
                }
                break;
        }
        bottomSheet.dismiss();
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(5000);
        animator.addUpdateListener(animation -> {
            changeWallpaper.setText(animation.getAnimatedValue().toString() + "%");
        });
        animator.start();
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            changeWallpaper.setText("Applied ");
        }, 6000);

    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissions, 111);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                download.isClickable();
            }
        }
    }

    private void setHomeScreen() {
        Glide.with(DetailActivity.this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(DetailActivity.this);
                        try {
                            //set wallpaper
                            wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_SYSTEM);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DetailActivity.this, "Something was wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void setLockScreen() {
        Glide.with(DetailActivity.this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(DetailActivity.this);
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                //set lock screen
                                wallpaperManager.setBitmap(resource, null, true, WallpaperManager.FLAG_LOCK);
                            } else {
                                Toast.makeText(DetailActivity.this, "Lock screen wallpaper not supported",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }
    public void showAlertDialog(long time){
        KAlertDialog pDialog = new KAlertDialog(DetailActivity.this, KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Applying");
        pDialog.setCancelable(false);
        pDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            pDialog.dismiss();
            new KAlertDialog(DetailActivity.this, KAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success!")
                    .setContentText("Your wallpaper has changed!")
                    .setConfirmText("OK")
                    .show();
        }, time);
    }

}