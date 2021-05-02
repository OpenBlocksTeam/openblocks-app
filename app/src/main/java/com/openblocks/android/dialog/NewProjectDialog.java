package com.openblocks.android.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.openblocks.android.R;
import com.openblocks.moduleinterface.models.OpenBlocksProjectMetadata;

public class NewProjectDialog extends ProjectMetadataEditDialog {

    public NewProjectDialog(@NonNull Context context, String newProjectId) {
        super(context, new OpenBlocksProjectMetadata("", "", "", 1), newProjectId);

        projectId = newProjectId;
        titleText = context.getString(R.string.new_project);
        buttonText = context.getString(R.string.create_project);
    }
}
