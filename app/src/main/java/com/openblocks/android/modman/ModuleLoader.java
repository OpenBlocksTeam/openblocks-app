package com.openblocks.android.modman;

import android.content.Context;
import android.widget.Toast;

import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

import dalvik.system.DexClassLoader;

/**
 * ModuleLoader is a utility class used to load modules into OpenBlocksModule
 */
public class ModuleLoader {

    public static <T extends OpenBlocksModule> T load(Context context, Module module, Class<T> type) {

        if (module == null) {
            Toast.makeText(context, "module is null, do you have all of the needed modules activated?", Toast.LENGTH_SHORT).show();
            return null;
        }

        ModuleManager moduleManager = ModuleManager.getInstance();

        try {
            Class<Object> module_class = moduleManager.fetchModule(context, module);

            return type.cast(module_class.newInstance());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            Toast.makeText(context, "Error whilst loading project manager module " + module.name + ": Wrong classpath", Toast.LENGTH_LONG).show();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();

            Toast.makeText(context, "Error while instantiating module " + module.name + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();

            Toast.makeText(context, "Error while accessing a module: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }
}
