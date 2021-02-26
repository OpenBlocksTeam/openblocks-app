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
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar _actionBar;
    private DrawerLayout _drawer;
    private NavigationView _drawer_navView;
    private ActionBarDrawerToggle _toggle;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private FloatingActionButton fabProjects;
    private FloatingActionButton fabModules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permission Stuff
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 69);

        // Main Part (ActionBar)
        _actionBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(_actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Drawer Toggle & Drawer
        _drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        _drawer_navView = (NavigationView) findViewById(R.id.nav_view) ;

        _toggle = new ActionBarDrawerToggle(MainActivity.this, _drawer, _actionBar, R.string.app_name, R.string.app_name);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();

        _drawer_navView.setNavigationItemSelectedListener(this);

        // View Pager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentAdapter(getApplicationContext(), getSupportFragmentManager(), 2));
        tabLayout.setupWithViewPager(viewPager);

        // FABs
        fabProjects = (FloatingActionButton) findViewById(R.id.fabProjects);
        fabModules = (FloatingActionButton) findViewById(R.id.fabModules);

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
                }
                else {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 69: {
                File openBlocksData = new File(Environment.getExternalStorageDirectory() + File.separator + ".OpenBlocks");
                if (!openBlocksData.exists()) openBlocksData.mkdirs();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i1 = new Intent();

        switch (item.getItemId()) {
            case R.id.home:
                _drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.settings:
                i1.setClass(MainActivity.this, SettingsActivity.class);
                startActivity(i1);
                break;
            case R.id.about:
                i1.setClass(MainActivity.this, AboutActivity.class);
                startActivity(i1);
                break;
            case R.id.dc:
                Intent i2 = new Intent();
                i2.setAction(Intent.ACTION_VIEW);
                i2.setData(Uri.parse("https://discord.gg/ESCfUBy26Z"));
                startActivity(i2);
                break;
            case R.id.gh:
                Intent i3 = new Intent();
                i3.setAction(Intent.ACTION_VIEW);
                i3.setData(Uri.parse("https://github.com/OpenBlocksTeam"));
                startActivity(i3);
                break;
            case R.id.web:
                Intent i4 = new Intent();
                i4.setAction(Intent.ACTION_VIEW);
                i4.setData(Uri.parse("https://openblocks.tk/"));
                startActivity(i4);
                break;
        }

        return false;
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {
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
            if (_position == 0) return "Projects";
            if (_position == 1) return "Modules";
            else return null;
        }

        @Override
        public Fragment getItem(int _position) {
            if (_position == 0) return new ProjectsFragment();
            if (_position == 1) return new ModulesFragment();
            else return null;
        }
    }
}