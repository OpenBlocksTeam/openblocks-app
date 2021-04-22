package com.openblocks.android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.openblocks.android.modman.models.Module;
import com.openblocks.android.databinding.ActivityModuleInfoBinding;

public class ModuleInfoActivity extends AppCompatActivity {

    Module module;
    ActivityModuleInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModuleInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        module = getIntent().getParcelableExtra("module");

        getSupportActionBar().setTitle("Module: " + module.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}