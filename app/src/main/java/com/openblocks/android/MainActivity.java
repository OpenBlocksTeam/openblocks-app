package com.openblocks.android;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.openblocks.android.modman.ModuleJsonCorruptedException;
import com.openblocks.android.modman.ModuleManager;
import com.openblocks.android.modman.models.Module;
import com.openblocks.android.ui.main.SectionsPagerAdapter;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // BOFA waisted 20 minutes on this
        drawer = findViewById(R.id.drawer_layout);

        // MODULES STUFF ===========================================================================

        // Get the SharedPreferences
        sp = getSharedPreferences("data", MODE_PRIVATE);

        // Check if this is the first time the user has opened this app
        if (sp.getBoolean("first_time", false)) {
            // Oo, first time huh, let's initialize the modules folder, and extract our default modules there
            try {
                // Initialize the modules folder
                File modules_folder = new File(getFilesDir(), "/modules/");
                modules_folder.mkdir();

                // Initialize the modules.json file
                new File(modules_folder, "modules.json").createNewFile();

                // TODO: EXTRACT / DOWNLOAD DEFAULT MODULES
            } catch (IOException e) {
                Toast.makeText(this, "Error while initializing modules: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        }

        // TODO: SHOW A LOADING BAR / SCREEN WHEN WE'RE LOADING MODULES
        ModuleManager moduleManager = ModuleManager.getInstance();

        // Load modules
        try {
            moduleManager.load_modules(this);

        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(this, "Error while reading modules: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (ModuleJsonCorruptedException e) {
            e.printStackTrace();

            Toast.makeText(this, "modules.json is corrupted: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // MODULES STUFF ===========================================================================

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), moduleManager.getModules());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fab.setOnClickListener(view -> Snackbar.make(view, "New Hidepain", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}