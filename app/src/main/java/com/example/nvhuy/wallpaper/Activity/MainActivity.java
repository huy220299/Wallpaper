package com.example.nvhuy.wallpaper.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.nvhuy.wallpaper.R;
import com.example.nvhuy.wallpaper.adapter.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.kobakei.ratethisapp.RateThisApp;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


public class MainActivity extends BaseActivity {
    SharedPreferences sharedPreferences;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    FloatingActionButton fab_upload;
    private NavigationView navigationView;
    EditText edt_search;
    CoordinatorLayout coordinatorLayout;
    private ChipNavigationBar mBottomNavigation;
    private ViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        toolbar = findViewById(R.id.main_toolbar);
        coordinatorLayout = findViewById(R.id.main_layout);
        edt_search = findViewById(R.id.edt_search);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        OnClick();
        setDarkMode();
        itemNavigationSelected();
        setNavigator();

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
                case R.id.category:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.random:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.trending:
                    viewPager.setCurrentItem(4);
                    break;
            }
        });
        viewPager = findViewById(R.id.viewpager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        OverScrollDecoratorHelper.setUpOverScroll(viewPager);
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
                        mBottomNavigation.setItemSelected(R.id.category, true);
                        break;
                    case 3:
                        mBottomNavigation.setItemSelected(R.id.random, true);
                        break;
                    case 4:
                        mBottomNavigation.setItemSelected(R.id.trending, true);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void itemNavigationSelected() {
        View rootView = navigationView.getRootView();

        ImageView exit = rootView.findViewById(R.id.exit);
        LinearLayout item_change_wallpaper = rootView.findViewById(R.id.item_autoChange);
        LinearLayout item_favorite = rootView.findViewById(R.id.item_favorite);
        LinearLayout item_rate = rootView.findViewById(R.id.item_rate);
        LinearLayout item_share = rootView.findViewById(R.id.item_share);
        LinearLayout item_feedback = rootView.findViewById(R.id.item_feedback);
        //exit
        exit.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.START));
        //change wallpaper
        item_change_wallpaper.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AutoChangerActivity.class)));
        //favorite
        item_favorite.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FavoriteActivity.class)));
        //rate
        item_rate.setOnClickListener(v -> {
            RateThisApp rateThisApp = null;
            RateThisApp.Config config = new RateThisApp.Config();
            config.setUrl("http://www.google.com");
            RateThisApp.init(config);
            rateThisApp.onCreate(MainActivity.this);
            rateThisApp.showRateDialog(MainActivity.this);
        });
        //share
        item_share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check my app: wallpaper.chPlay");
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
        //feedback
        item_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriText =
                        "mailto:adtrue@gmail.com" +
                                "?subject=" + Uri.encode("") +
                                "&body=" + Uri.encode("");
                Uri uri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                startActivity(Intent.createChooser(sendIntent, "Send email"));
            }
        });
    }

    private void setDarkMode() {

        View rootView = navigationView.getRootView();
        Switch s = rootView.findViewById(R.id.item_darkMode);
        boolean i = s.isChecked();
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


//        btn_return.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                relativeLayout1.setVisibility(View.VISIBLE);
//                relativeLayout2.setVisibility(View.GONE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                setNavigator();
//            }
//        });


    }

    private void setNavigator() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_navigation_drawer_dark, this.getTheme());
        drawable.setTint(getColor(R.color.text_change_color));
        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);
        actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }


}



