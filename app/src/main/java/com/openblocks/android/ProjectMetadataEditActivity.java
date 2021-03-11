package com.openblocks.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProjectMetadataEditActivity extends AppCompatActivity {

    // TODO: 3/11/21 Be able to edit the app icon
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_project_metadata);
    }

    public void okButtonClicked(View view) {
        EditText app_name = findViewById(R.id.edit_app_name);
        EditText package_edit = findViewById(R.id.edit_package);
        EditText version_name = findViewById(R.id.edit_version_name);
        EditText version_code = findViewById(R.id.edit_version_code);
        
        Intent i = new Intent();
        i.putExtra("app_name", app_name.getText().toString());
        i.putExtra("package", package_edit.getText().toString());
        i.putExtra("version_name", version_name.getText().toString());
        i.putExtra("version_code", version_code.getText().toString());

        setResult(RESULT_OK, i);
    }

    // Not sure if this is necessary, but just in case
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_CANCELED);
    }
}