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
    ImageView btn_blur, btn_done;
    LinearLayout action_blur, action_done;
    SeekBar seekBar;
    TextView numberBlur;
    Bitmap bitmap;
    RelativeLayout relativeLayout;
    int progress_=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        String url = getIntent().getExtras().getString("url");
        action_blur = findViewById(R.id.action_blur);
        action_done = findViewById(R.id.action_done);
        imageView = findViewById(R.id.BlurImageView);

        seekBar = findViewById(R.id.seekbar);
        numberBlur = findViewById(R.id.numberSeekBar);
        relativeLayout = findViewById(R.id.relative_layout_editor_activity_saturation);
        Glide.with(this).load(url).into(imageView);
        Log.e("~~~", "url:"+url);

        action_blur.setOnClickListener(v -> relativeLayout.setVisibility(View.VISIBLE));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageView.setBlur(progress/8);
                imageView.invalidate();
                progress_=progress;
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                 bitmap = drawable.getBitmap();
                numberBlur.setText(progress+" %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        action_done.setOnClickListener(view -> {
            relativeLayout.setVisibility(View.GONE);
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