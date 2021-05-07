package com.example.nvhuy.wallpaper.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nvhuy.wallpaper.R;
import com.jgabrielfreitas.core.BlurImageView;

public class EditActivity extends BaseActivity {
    BlurImageView imageView;
    ImageView  btn_done;
    SeekBar seekBar;
    TextView numberBlur;
    Bitmap bitmap;

    int progress_=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        String url = getIntent().getExtras().getString("url");
        btn_done = findViewById(R.id.action_done);
        imageView = findViewById(R.id.BlurImageView);

        seekBar = findViewById(R.id.seekbar);
        numberBlur = findViewById(R.id.numberSeekBar);

        Glide.with(this).load(url).into(imageView);
        Log.e("~~~", "url:"+url);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageView.setBlur(progress/8);
                imageView.invalidate();
                progress_=progress;
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                if (drawable!=null){
                    bitmap = drawable.getBitmap();
                }

                numberBlur.setText(progress+" %");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btn_done.setOnClickListener(view -> {
            if (progress_ ==0){
                bitmap=null;
                setResult(1);
            }else {
                final Intent data = new Intent();
                data.putExtra("bitmap", bitmap);
                setResult(Activity.RESULT_OK, data);
            }
            finish();


        });
    }
}