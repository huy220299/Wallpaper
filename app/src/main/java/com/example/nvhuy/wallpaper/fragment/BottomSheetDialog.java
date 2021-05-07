package com.example.nvhuy.wallpaper.fragment;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.nvhuy.wallpaper.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    String url;
    String kind;
    Bitmap bitmap;
    private Callback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);


        LinearLayout setHomeScreen = v.findViewById(R.id.linear1);
        LinearLayout setLockScreen = v.findViewById(R.id.linear2);
        LinearLayout setAllScreen = v.findViewById(R.id.linear3);

        setHomeScreen.setOnClickListener(v1 -> {
            if (callback != null) {
                callback.onButtonClicked(v1);
            }
        });
        setLockScreen.setOnClickListener(v12 -> {
            if (callback != null) {
                callback.onButtonClicked(v12);
            }
        });
        setAllScreen.setOnClickListener(v13 -> {
            if (callback != null) {
                callback.onButtonClicked(v13);
            }
        });
        return v;
    }

    public interface Callback {
        public void onButtonClicked(View view);
    }

    @Override
    public void onAttach(Activity ac) {
        super.onAttach(ac);
        callback = (Callback) ac;
    }

    @Override
    public void onDetach() {
        callback = null;
        super.onDetach();
    }
}
