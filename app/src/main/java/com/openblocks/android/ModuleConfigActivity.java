package com.openblocks.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openblocks.android.adapters.ConfigRecyclerViewAdapter;
import com.openblocks.android.modman.ModuleLogger;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;
import com.openblocks.moduleinterface.callbacks.Logger;
import com.openblocks.moduleinterface.models.config.OpenBlocksConfig;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import dalvik.system.DexClassLoader;

/**
 * This activity is where the user can edit the module's (pre-defined by the module itself) configuration
 */
public class ModuleConfigActivity extends AppCompatActivity {

    private static final String TAG = "ModuleConfigActivity";

    ConfigRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_config);

        setSupportActionBar(findViewById(R.id.toolBarModuleConfig));
        // We need a back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Get the module extra
        Module module = getIntent().getParcelableExtra("module");

        // Null check
        if (module == null) {
            Log.e(TAG, "onCreate: Module is null", null);
            finish();

            return;
        }

        File jarfile = module.jar_file;
        String classpath = module.classpath;

        // Now load the module
        try {
            // Get the jar file's path
            final String libPath = jarfile.getAbsolutePath();

            // Load it using DexClassLoader
            final DexClassLoader classloader = new DexClassLoader(libPath, getCodeCacheDir().getAbsolutePath(), null, this.getClass().getClassLoader());
            final Class<?> module_class = (Class<?>) classloader.loadClass(classpath);

            if (OpenBlocksModule.class.isAssignableFrom(module_class)) {
                //module empty constructor
                Constructor<?> constructor = module_class.getConstructor();

                // Then get the setupConfig method (one of the functions in OpenBlocks Module Interface)
                final Method setupConfig = module_class.getMethod("setupConfig");

                //get the object that represents the class
                OpenBlocksModule openBlocksModule = (OpenBlocksModule) constructor.newInstance();

                //initialize method
                final Method init = module_class.getMethod("initialize", Context.class, Logger.class);

                //initialize the module
                init.invoke(openBlocksModule, this, ModuleLogger.getInstance());
                // get the config object
                OpenBlocksConfig config = (OpenBlocksConfig) setupConfig.invoke(openBlocksModule);

                // Then load it
                loadConfig(config);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            Toast.makeText(this, "Error while loading config: Wrong classpath: " +  classpath, Toast.LENGTH_LONG).show();
        } catch (IllegalAccessException e) {
            e.printStackTrace();

            Toast.makeText(this, "IllegalAccessException: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();

            Toast.makeText(this, "Method not found: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (InvocationTargetException e) {
            e.printStackTrace();

            Toast.makeText(this, "InvocationTargetException: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (InstantiationException e) {
            e.printStackTrace();

            Toast.makeText(this, "InstantiationException: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadConfig(OpenBlocksConfig config) {
        // This is where we will display the config to the recyclerview, simple
        // Get the recyclerview
        RecyclerView rv = findViewById(R.id.module_config);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // And apply the adapter, done
        adapter = new ConfigRecyclerViewAdapter(config, this);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Did the user clicked the back button?
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        adapter.onResult(requestCode, data);
    }
}