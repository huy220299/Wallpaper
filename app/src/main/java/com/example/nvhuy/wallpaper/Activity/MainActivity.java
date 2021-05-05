package com.example.nvhuy.wallpaper.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.adapter.ViewPagerAdapter;
import com.example.nvhuy.wallpaper.model.Album;
import com.example.nvhuy.wallpaper.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.kobakei.ratethisapp.RateThisApp;

import java.util.Arrays;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPreferences;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    FloatingActionButton fab_upload;
    private NavigationView navigationView;
    EditText edt_search;
    ImageView btn_return, btn_search, img;
    RelativeLayout relativeLayout1, relativeLayout2;
    CoordinatorLayout coordinatorLayout;
    InputMethodManager imm;


    private ChipNavigationBar mBottomNavigation;
    private ViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        toolbar = findViewById(R.id.main_toolbar);

        relativeLayout1 = findViewById(R.id.relativeLayout1);
        relativeLayout2 = findViewById(R.id.relativeLayout2);
        coordinatorLayout = findViewById(R.id.main_layout);

        btn_return = findViewById(R.id.home_back);
        btn_search = findViewById(R.id.home_search);
        edt_search = findViewById(R.id.edt_search);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        img = (ImageView) findViewById(R.id.home_update);
        RotateAnimation rotateAnimation = new RotateAnimation(30, 90,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        img.setAnimation(rotateAnimation);
        img.startAnimation(rotateAnimation);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        OnClick();
        setDarkMode();

        mBottomNavigation = findViewById(R.id.navigation);
        mBottomNavigation.setItemSelected(R.id.home, true);
        mBottomNavigation.setOnItemSelectedListener(i -> {
            switch (i) {
                case R.id.home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.live:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.trending:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.random:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.category:
                    viewPager.setCurrentItem(4);
                    break;
            }
        });
        viewPager = findViewById(R.id.viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigation.setItemSelected(R.id.home, true);
                        break;
                    case 1:
                        mBottomNavigation.setItemSelected(R.id.live, true);
                        break;
                    case 2:
                        mBottomNavigation.setItemSelected(R.id.trending, true);
                        break;
                    case 3:
                        mBottomNavigation.setItemSelected(R.id.random, true);
                        break;
                    case 4:
                        mBottomNavigation.setItemSelected(R.id.category, true);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setNavigator();


    }


    private void setDarkMode() {
        MenuItem statItem = navigationView.getMenu().findItem(R.id.item_darkMode);
        Switch s = (Switch) statItem.getActionView();
//        boolean i = s.isChecked();
        sharedPreferences = getSharedPreferences("night", 0);
        Boolean booleanValue = sharedPreferences.getBoolean("night_mode_wallpaper", false);
        if (booleanValue) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            s.setChecked(true);
        }
        s.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                s.setChecked(true);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night_mode_wallpaper", true);
                editor.apply();
                //reload activity

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                s.setChecked(false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night_mode_wallpaper", false);
                editor.apply();
                //reload activity

            }
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);

        });
        s.setOnClickListener(v -> Log.e("click", "clicked"));
    }

    private void OnClick() {
        btn_search.setOnClickListener(v -> {
            relativeLayout1.setVisibility(View.GONE);
            relativeLayout2.setVisibility(View.VISIBLE);
            edt_search.requestFocus();
            toolbar.setNavigationIcon(null);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        });
        edt_search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //logEvent search
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, edt_search.getText().toString());
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
                    //end
                    Intent intent = new Intent(MainActivity.this, ItemBySearchActivity.class);
                    intent.putExtra("keyword", edt_search.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });


        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout1.setVisibility(View.VISIBLE);
                relativeLayout2.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                setNavigator();
            }
        });


    }

    private void setNavigator() {
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_contact: {
                String uriText =
                        "mailto:adtrue@gmail.com" +
                                "?subject=" + Uri.encode("some subject text here") +
                                "&body=" + Uri.encode("some text here");
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                startActivity(Intent.createChooser(sendIntent, "Send email"));
                break;
            }
            case R.id.item_share: {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check my app: wallpaper.chPlay");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
            }
            case R.id.item_autoChange: {
                startActivity(new Intent(getApplicationContext(), AutoChangerActivity.class));
                break;

            }
            case R.id.item_favorite: {
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
                break;

            }
            case R.id.item_update: {

                break;
            }
            case R.id.item_rate: {
                RateThisApp rateThisApp = null;
                RateThisApp.Config config = new RateThisApp.Config();
                config.setUrl("http://www.google.com");
                RateThisApp.init(config);
                rateThisApp.onCreate(this);
                rateThisApp.showRateDialog(this);
                break;
            }


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}



