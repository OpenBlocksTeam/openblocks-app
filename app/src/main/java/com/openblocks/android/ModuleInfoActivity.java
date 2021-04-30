package com.openblocks.android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.openblocks.android.modman.ModuleManager;
import com.openblocks.android.modman.models.Module;
import com.openblocks.android.databinding.ActivityModuleInfoBinding;

public class ModuleInfoActivity extends AppCompatActivity {

    Module module;
    ActivityModuleInfoBinding binding;

    ModuleManager moduleManager = ModuleManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModuleInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        module = getIntent().getParcelableExtra("module");

        setSupportActionBar(binding.toolBarModuleInfo);

        bindInfos();
    }

    private void bindInfos() {
        binding.moduleInfoName.setText(module.name);
        binding.moduleInfoType.setText(module.module_type.toString());
        binding.moduleInfoDescription.setText(module.description);

        // Check if this module is an active module
        if (isModuleActive()) {
            // Yes this is an active module, remove the activate module button
            binding.moduleInfoActivate.setVisibility(View.GONE);
        }
    }

    public void activateModule(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.activate_module);
        builder.setMessage(R.string.dialog_activate_module_confirmation);

        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            // Activate the module
            moduleManager.activateModule(module.module_type, module);

            // Check if the activation succeed
            if (isModuleActive()) {
                Toast.makeText(this, "Failed to activate module for an unknown reason, ModuleInfoActivity.java at line 61", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, R.string.module_activate_success_restarting, Toast.LENGTH_LONG).show();
                recreate();
            }
        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.module_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();

                return true;

            case R.id.delete_module:
                removeModule();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void removeModule() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.remove_module);
        builder.setMessage(R.string.dialog_remove_module_confirmation);

        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            if (isModuleActive()) {
                Toast.makeText(this, R.string.failure_remove_active_module, Toast.LENGTH_SHORT).show();
                return;
            }

            moduleManager.removeModule(module.module_type, module);
            Toast.makeText(this, module.name + getString(R.string.success_remove_module), Toast.LENGTH_SHORT).show();

            finish();
        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    /**
     * A simple function used to check if the current module is activated
     * @return Is this module activated?
     */
    private boolean isModuleActive() {
        return moduleManager.getActiveModule(module.module_type).equals(module);
    }
}