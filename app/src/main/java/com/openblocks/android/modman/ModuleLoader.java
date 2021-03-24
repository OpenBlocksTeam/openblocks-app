package com.openblocks.android.modman;

import android.content.Context;
import android.widget.Toast;

import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

/**
 * ModuleLoader is a utility class used to load modules into OpenBlocksModule
 */
public class ModuleLoader {

    @SuppressWarnings("unchecked")
    // TODO: 3/23/21 restore this
    public static <T extends OpenBlocksModule> T load(Context context, Module module, Class<T> type) {
        ModuleManager moduleManager = ModuleManager.getInstance();

        Module project_manager  = moduleManager.getActiveModule(OpenBlocksModule.Type.PROJECT_MANAGER);
        Class<Object> project_manager_class;

        try {
            project_manager_class = moduleManager.fetchModule(context, project_manager);

            return (T) project_manager_class.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            Toast.makeText(context, "Error whilst loading project manager module " + project_manager.name + ": Wrong classpath", Toast.LENGTH_LONG).show();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();

            Toast.makeText(context, "Error while instantiating module " + project_manager.name + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();

            Toast.makeText(context, "Error while accessing a module: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }
}
