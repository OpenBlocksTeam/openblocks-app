package com.openblocks.android.modman;

import android.content.Context;
import android.util.Pair;

import com.openblocks.android.helpers.FileHelper;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ModuleManager {
    private static ModuleManager single_instance = null;

    // HashMap<ModuleType: OpenBlocksModule.Type, Modules: ArrayList<Module>>
    HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules;

    private ModuleManager() { }

    // Yes this is a singleton class
    public static ModuleManager getInstance() {
        if (single_instance == null)
            single_instance = new ModuleManager();

        return single_instance;
    }

    public void load_modules(Context context) throws IOException, JSONException {
        // Modules are stored on the internal directory /modules/
        File modules_directory = new File(context.getFilesDir(), "modules");

        JSONObject modules_information = null;
        ArrayList<Pair<String, File>> jar_files = new ArrayList<>();

        /* The module folder should contain something like this:
         *
         * modules
         *  L IyxanProjectManager.jar
         *  L BOFACompiler.jar
         *  L OpenModule-CodeEditor.jar
         *  ...
         *  L modules.json <- Contains information about these jars, like description, name, classpath, etc
         */

        /* modules.json should be something like this:
         * {
         *   "modules": {
         *     "IyxanProjectManager.jar": {
         *       "name": "IyxanProjectManager",
         *       "description": "IyxanProjectManager is a super lightweight and super simple project manager.",
         *       "classpath": "com.iyxan23.project.manager.IyxanProjectManager",
         *       "type": "PROJECT_MANAGER",
         *       "version": 1, // This is the module's version
         *       "lib_version": 1, // This is the library's (OpenBlocksInterface) version
         *     }
         *   }
         * }
         */

        // Create the module directory if it doesn't exist
        if (!modules_directory.exists()) {
            modules_directory.mkdir();
        }

        // Loop per files, extract their information, and add them to the modules HashMap<ArrayList>
        for (File module: modules_directory.listFiles()) {
            if (module.getName().equals("modules.json")) {
                modules_information = new JSONObject(FileHelper.readFile(module));
            } else {
                // This is a jar file
                jar_files.add(new Pair<>(module.getName(), module));
            }
        }

        // just return empty handed if there is no modules.json
        if (modules_information == null) {
            return;
        }

        // Loop per every jar files
        for (Pair<String, File> jar_file : jar_files) {
            // Get the module info from the modules.json
            JSONObject current_module_info = modules_information.getJSONObject(jar_file.first);

            // Get the module type
            OpenBlocksModule.Type module_type;

            if        (current_module_info.getString("type").equals("PROJECT_MANAGER")) {
                module_type = OpenBlocksModule.Type.PROJECT_MANAGER;

            } else if (current_module_info.getString("type").equals("PROJECT_PARSER")) {
                module_type = OpenBlocksModule.Type.PROJECT_PARSER;

            } else if (current_module_info.getString("type").equals("PROJECT_LAYOUT_GUI")) {
                module_type = OpenBlocksModule.Type.PROJECT_LAYOUT_GUI;

            } else if (current_module_info.getString("type").equals("PROJECT_CODE_GUI")) {
                module_type = OpenBlocksModule.Type.PROJECT_CODE_GUI;

            } else if (current_module_info.getString("type").equals("PROJECT_COMPILER")) {
                module_type = OpenBlocksModule.Type.PROJECT_COMPILER;

            } else {
                continue;
            }

            // Check if the arraylist is already initialized
            if (!modules.containsKey(module_type)) {
                // Oop, it haven't, let's initialize it
                modules.put(module_type, new ArrayList<>());
            }

            // ohk, add the module
            Objects.requireNonNull(
                modules.get(module_type)
            ).add(new Module(
                    current_module_info.getString("name"),
                    current_module_info.getString("description"),
                    current_module_info.getString("classpath"),
                    current_module_info.getInt("version"),
                    current_module_info.getInt("lib_version"),
                    jar_file.second
            ));
        }
    }
}
