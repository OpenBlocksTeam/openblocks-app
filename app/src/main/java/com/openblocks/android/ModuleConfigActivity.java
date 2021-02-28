package com.openblocks.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.openblocks.android.adapters.ConfigRecyclerViewAdapter;
import com.openblocks.android.modman.ModuleManager;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;
import com.openblocks.moduleinterface.models.config.OpenBlocksConfig;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import dalvik.system.DexClassLoader;

/**
 * This activity is where the user can edit the module's (pre-defined by the module itself) configuration
 */
public class ModuleConfigActivity extends AppCompatActivity {

    private static final String TAG = "ModuleConfigActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_config);

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
            final Class<Object> module_class = (Class<Object>) classloader.loadClass(classpath);

            // Then get the setupConfig method (one of the functions in OpenBlocks Module Interface)
            final Method setupConfig = module_class.getMethod("setupConfig");

            // Invoke it
            OpenBlocksConfig config = (OpenBlocksConfig) setupConfig.invoke(module_class);

            // Then load it
            loadConfig(config);

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
        }
    }

    private void loadConfig(OpenBlocksConfig config) {
        // This is where we will display the config to the recyclerview, simple
        // Get the recyclerview
        RecyclerView rv = findViewById(R.id.module_config);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // And apply the adapter, done
        ConfigRecyclerViewAdapter adapter = new ConfigRecyclerViewAdapter(config.getConfigs());
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
}