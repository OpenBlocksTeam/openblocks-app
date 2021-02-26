package com.openblocks.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout _drawer;

    private FloatingActionButton fabProjects;
    private FloatingActionButton fabModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Main Part (ActionBar)
        Toolbar _actionBar = findViewById(R.id.toolBar);
        setSupportActionBar(_actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Drawer Toggle
        _drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(MainActivity.this, _drawer, _actionBar, R.string.app_name, R.string.app_name);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();

        // View Pager
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentAdapter(getApplicationContext(), getSupportFragmentManager(), 2));
        tabLayout.setupWithViewPager(viewPager);

        // FABs
        fabProjects = findViewById(R.id.fabProjects);
        fabModules = findViewById(R.id.fabModules);

        fabModules.hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fabProjects.setTooltipText("New Project");
            fabModules.setTooltipText("Add Module");
        }

        // Listeners
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int _position, float _positionOffset, int _positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int _position) {
                if (_position == 0) {
                    fabProjects.show();
                    fabModules.hide();
                } else {
                    fabProjects.hide();
                    fabModules.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int _scrollState) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (_drawer.isDrawerOpen(GravityCompat.START)) {
            _drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public static class FragmentAdapter extends FragmentStatePagerAdapter {
        Context context;
        int tabCount;

        public FragmentAdapter(Context context, FragmentManager fm, int tabCount) {
            super(fm);
            this.context = context;
            this.tabCount = tabCount;
        }

        @Override
        public int getCount(){
            return tabCount;
        }

        @Override
        public CharSequence getPageTitle(int _position) {
            switch (_position) {
                case 0:
                    return "Projects";
                case 1:
                    return "Modules";
                default:
                    return null;
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int _position) {
            switch (_position) {
                case 0:
                    return new ProjectsFragment();
                case 1:
                    return new ModulesFragment();
                default:
                    return new Fragment();
            }
        }
    }
}