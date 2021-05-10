package com.example.nvhuy.wallpaper.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.developer.kalert.KAlertDialog;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.example.nvhuy.wallpaper.Service.MyDatabaseHelper;
import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.Ultility.Common;
import com.example.nvhuy.wallpaper.Service.VideoLiveWallpaper;
import com.example.nvhuy.wallpaper.fragment.BottomSheetDialog;
import com.example.nvhuy.wallpaper.model.Image;
import com.example.nvhuy.wallpaper.model.Liked_image;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


import java.io.File;
import java.io.IOException;


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
//    ProgressBar progressBar;
    private Bitmap result;
    Button text_download;
    Image image;
    FirebaseFirestore db;
    private FirebaseAnalytics mFirebaseAnalytics;
    DownloadManager dm;
    LottieAnimationView animationApply, animationLoading;

    BottomSheetDialog bottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "________________________________________");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        animationApply = findViewById(R.id.animation_apply);
        animationLoading = findViewById(R.id.animation_loading);
//        progressBar = findViewById(R.id.progress);
//        text_download = findViewById(R.id.text_download);
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
        getFromIntent();
        setBackground();
        onClick();
        createFirebaseStore();

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
                animationLoading.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
            });
        } else {
            edit.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(DetailActivity.this)
                    .load(url)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            progressBar.setVisibility(View.GONE);
                            animationLoading.setVisibility(View.GONE);
                            new KAlertDialog(DetailActivity.this, KAlertDialog.WARNING_TYPE)
                                    .setTitleText("Reload please,  error connection!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(kAlertDialog -> onBackPressed())
                                    .show();
                            Log.e(TAG,e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            progressBar.setVisibility(View.GONE);
                            animationLoading.setVisibility(View.GONE);
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
//        DocumentReference docIdRef = db.collection("Image").document(image.getId().toString());
//        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "Document exists!");
//
//                    } else {
//                        db.collection("Image").document(image.getId().toString()).set(image);
//                        Log.d(TAG, "Document does not exist!");
//                    }
//                } else {
//                    Log.d(TAG, "Failed with: ", task.getException());
//                }
//            }
//        });


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
        tittle = getIntent().getExtras().getString("tittle");

        kind = getIntent().getExtras().getString("kind");


//        Glide.with(this).load(img_user)
//                .into(user_image);
        //set follow and liked
        if (myDatabaseHelper.checkLiked(id_image)) {
            like.setImageResource(R.drawable.icon_favorited);
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

            Bundle params = new Bundle();
            params.putString("image_name", tittle);
            params.putString("full_text", url);
            mFirebaseAnalytics.logEvent("apply_wallpaper", params);

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
//                    download.setVisibility(View.GONE);
//                    text_download.setVisibility(View.VISIBLE);

                    //start analytics
                    Bundle params = new Bundle();
                    params.putString("image_name", tittle);
                    params.putString("full_text", url);
                    mFirebaseAnalytics.logEvent("download_image", params);
                    //end


                    if (kind.equals("video")) {
                        download(url,id_image+".mp4");

                    } else {
                        download(url,id_image+".jpg");
                    }
                }
            }


        });
        //like button
        like.setOnClickListener(v -> {
            if (!myDatabaseHelper.checkLiked(id_image)) {
                myDatabaseHelper.insertImage(new Liked_image(id_image, ""));//add to local database
                like.setImageResource(R.drawable.icon_favorited);
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
                like.setImageResource(R.drawable.icon_favorite);
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



    private void downloadImageNewInApp(String filename, String downloadUrlOfImage, String fileType) {
        try {
            File direct = new File("/myWallpaper");
            if (!direct.exists()) {
                direct.mkdirs();
            }
            dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
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
            case R.id.linear1:
//                showAlertDialog(5000);
                showAnimation(5000);
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
            case R.id.linear2:
//                showAlertDialog(10000);
                showAnimation(10000);
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
            case R.id.linear3:
//              showAlertDialog(10000);
              showAnimation(10000);
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
    private void showAnimation(long time){
        animationLoading.setVisibility(View.GONE);
        animationApply.setVisibility(View.VISIBLE);
        animationApply.setAnimation(R.raw.download);
        animationApply.loop(true);
        animationApply.playAnimation();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            animationApply.loop(false);
            animationApply.pauseAnimation();
            animationApply.setAnimation(R.raw.tick);
            animationApply.playAnimation();
            animationApply.addAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animationApply.setVisibility(View.GONE);
                }
            });
        }, time);


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

    private  void download(String url, String filename){


        File direct = new File(this.getFilesDir().getAbsolutePath() + File.separator + "downloaded");
        Log.e("downloadTask",this.getFilesDir().getAbsolutePath() +File.separator + "downloaded");
        if (!direct.exists()) {
            direct.mkdirs();
        }
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
        PRDownloader.download(url, direct.getAbsolutePath(), filename)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        animationLoading.setVisibility(View.GONE);
                        animationApply.setVisibility(View.VISIBLE);
                        animationApply.setAnimation(R.raw.download_1);
                        animationApply.playAnimation();
                    Log.e("downloadTask","start or resume");
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        Log.e("downloadTask","on pause");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        Log.e("downloadTask","cancel");
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
//                        text_download.setText(Math.round((1.0*progress.currentBytes/progress.totalBytes)*100) + "%");
                        Log.e("downloadTask",Math.round((1.0*progress.currentBytes/progress.totalBytes)*100) + "%");

                    }
                })
                .start(new OnDownloadListener() {

                    @Override
                    public void onDownloadComplete() {

                        Bundle params = new Bundle();
                        params.putString("image_name", tittle);
                        params.putString("full_text", url);
                        mFirebaseAnalytics.logEvent("download_image_success", params);

                        animationApply.pauseAnimation();
                        animationApply.setAnimation(R.raw.success);
                        animationApply.playAnimation();
                        animationApply.addAnimatorListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                animationApply.setVisibility(View.GONE);
                            }
                        });


//                        text_download.setVisibility(View.GONE);
//                        download.setVisibility(View.VISIBLE);
                        download.setImageResource(R.drawable.downloaded);
                        download.setClickable(false);
                        Log.e("downloadTask","complete");
                    }

                    @Override
                    public void onError(Error error) {
                        text_download.setVisibility(View.GONE);
                        download.setVisibility(View.VISIBLE);

                        Bundle params = new Bundle();
                        params.putString("image_name", tittle);
                        params.putString("full_text", error.toString());
                        mFirebaseAnalytics.logEvent("download_image_error", params);
                        Log.e("downloadTask","error");
                    }
                });


    }
}