package com.openblocks.android.modman;

import android.content.Context;

import androidx.annotation.Nullable;

import com.openblocks.android.modman.models.Module;

import java.io.File;

public class ModuleResourceManager {

    public static File getModuleResource(Context context, Module module) {
        return new File(context.getFilesDir(), "modules_resources/" + module.jar_file.getName());
    }

    @Nullable
    public static File getResource(Context context, Module module, String filename) {
        File resource = new File(getModuleResource(context, module), filename);

        if (!resource.exists()) {
            return null;
        }

        return resource;
    }
}
