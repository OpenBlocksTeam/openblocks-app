package com.openblocks.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.openblocks.android.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding _binding;
    private Toolbar _toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());

        _toolBar = _binding.toolbarAbout;
        setSupportActionBar(_toolBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            _toolBar.setNavigationOnClickListener(v -> onBackPressed());
        }
    }
}