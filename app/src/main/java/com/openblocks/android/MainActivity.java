package com.openblocks.android;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private Toolbar _actionBar;
    private DrawerLayout _drawer;
    private ActionBarDrawerToggle _toggle;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _actionBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(_actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Drawer Toogle
        _drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        _toggle = new ActionBarDrawerToggle(MainActivity.this, _drawer, _actionBar, R.string.app_name, R.string.app_name);
        _drawer.addDrawerListener(_toggle);
        _toggle.syncState();

        // View Pager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.setAdapter(new FragmentAdapter(getApplicationContext(), getSupportFragmentManager(), 2));
        tabLayout.setupWithViewPager(viewPager);
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