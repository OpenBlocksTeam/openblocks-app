package com.openblocks.android.dialog;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.openblocks.android.databinding.DialogEditProjectMetadataBinding;
import com.openblocks.moduleinterface.models.OpenBlocksProjectMetadata;
import com.openblocks.moduleinterface.models.OpenBlocksRawProject;

public class ProjectMetadataEditDialog extends AlertDialog {

    protected final TextView title;
    protected final TextInputEditText appName;
    protected final TextInputEditText packageName;
    protected final TextInputEditText versionName;
    protected final TextInputEditText versionCode;
    protected final MaterialButton button;
    protected String projectId;

    // TODO: Set title to "Edit <project name>"

    protected String titleText = "Edit";
    protected String buttonText = "Save project";

    protected OnMetadataEnteredListener listener;

    public ProjectMetadataEditDialog(@NonNull Context context, OpenBlocksProjectMetadata metadata, String project_id) {
        super(context);
        projectId = project_id;

        DialogEditProjectMetadataBinding binding = DialogEditProjectMetadataBinding.inflate(getLayoutInflater());
        setView(binding.getRoot());

        title = binding.textView2;
        appName = binding.editAppName;
        packageName = binding.editPackage;
        versionName = binding.editVersionName;
        versionCode = binding.editVersionCode;
        button = binding.okButton;

        // Set these values
        appName.setText(metadata.getName());
        packageName.setText(metadata.getPackageName());
        versionName.setText(metadata.getVersionName());
        versionCode.setText(metadata.getVersionCode());
    }

    @Override
    protected void onStart() {
        super.onStart();

        title.setText(titleText);
        button.setText(buttonText);

        button.setOnClickListener(v -> {
            if (!validateInput()) return;

            listener.onMetadataEntered(
                    appName.getText().toString(),
                    packageName.getText().toString(),
                    versionName.getText().toString(),
                    Integer.parseInt(versionCode.getText().toString())
            );

            dismiss();
        });
    }

    /**
     * @param listener The {@link OnMetadataEnteredListener} object to register as callback for this dialog instance.
     * @param <T>      Any class extending {@link ProjectMetadataEditDialog}.
     * @return The current {@link ProjectMetadataEditDialog}.
     */
    public <T extends ProjectMetadataEditDialog> T addOnMetadataEnteredListener(OnMetadataEnteredListener listener) {
        this.listener = listener;
        return (T) this;
    }

    /**
     * Validates input of all TextInputEditTexts in this dialog. If a text field has invalid data,
     * an error is set, if not, its error gets cleared.
     *
     * @return If the input in the TextInputEditTexts is valid.
     */
    protected boolean validateInput() {
        if (appName.getText().toString().isEmpty()) {
            appName.setError("Missing app name");
        } else {
            appName.setError(null);
        }

        if (packageName.getText().toString().isEmpty()) {
            packageName.setError("Missing package name");
        } else {
            packageName.setError(null);
        }

        if (versionName.getText().toString().isEmpty()) {
            versionName.setError("Missing version name");
        } else {
            versionName.setError(null);
        }

        if (versionCode.getText().toString().isEmpty()) {
            versionCode.setError("Missing version code");
        } else {
            versionCode.setError(null);
        }

        return appName.getError() == null && packageName.getError() == null
                && versionName.getError() == null && versionCode.getError() == null;
    }

    /**
     * A listener class used with a {@link ProjectMetadataEditDialog} class, providing an event
     * when the metadata for a project has been entered, validated and saved.
     */
    public interface OnMetadataEnteredListener {
        /**
         * Called when metadata for the project has been entered, validated and saved.
         * Returns null if the dialog was dismissed or canceled in any way without saving the data.
         *
         * @param appName     The project's new application name.
         * @param packageName The project's new APK package name.
         * @param versionName The project's new version name.
         * @param versionCode The project's new version code.
         */
        void onMetadataEntered(String appName, String packageName, String versionName, int versionCode);
    }
}
