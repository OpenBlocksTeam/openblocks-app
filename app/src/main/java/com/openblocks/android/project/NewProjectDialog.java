package com.openblocks.android.project;

import android.content.Context;

import androidx.annotation.NonNull;

import com.openblocks.moduleinterface.models.OpenBlocksRawProject;

import java.util.ArrayList;

public class NewProjectDialog extends ProjectMetadataEditDialog {

    public NewProjectDialog(@NonNull Context context, String newProjectId) {
        super(context, new OpenBlocksRawProject(newProjectId, new ArrayList<>()));

        projectId = newProjectId;
        titleText = "New project";
        buttonText = "Create project";
    }
}
