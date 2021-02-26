package com.openblocks.android.modman;

import android.content.Context;
import android.util.Pair;

import com.openblocks.android.helpers.FileHelper;
import com.openblocks.android.modman.models.Module;
import com.openblocks.android.modman.models.ModuleIsActiveException;
import com.openblocks.moduleinterface.OpenBlocksModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ModuleManager {
    private static ModuleManager single_instance = null;

    // HashMap<ModuleType: OpenBlocksModule.Type, Modules: ArrayList<Module>>
    HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules;

    // NOTE: modules in active_modules also exists in the variable modules
    HashMap<OpenBlocksModule.Type, Module> active_modules;

    private ModuleManager() { }

    // Yes this is a singleton class
    public static ModuleManager getInstance() {
        if (single_instance == null)
            single_instance = new ModuleManager();

        return single_instance;
    }

    public void load_modules(Context context) throws IOException, ModuleJsonCorruptedException {
        // Modules are stored on the internal directory /modules/
        File modules_directory = new File(context.getFilesDir(), "modules");

        JSONObject modules_information = null;
        JSONObject active_modules_json = null;
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
         *   "active_modules": {
         *     "PROJECT_MANAGER": "IyxanProjectManager.jar",
         *     "PROJECT_COMPILER": "BOFACompiler",
         *     ...
         *   },
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

                try {
                    JSONObject modules_json = new JSONObject(FileHelper.readFile(module));

                    active_modules_json = modules_json.getJSONObject("active_modules");
                    modules_information = modules_json.getJSONObject("modules");
                } catch (JSONException e) {
                    throw new ModuleJsonCorruptedException(e.getMessage());
                }

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
            try {
                String filename = jar_file.first;

                // Get the module info from the modules.json by it's name
                JSONObject current_module_info = modules_information.getJSONObject(filename);

                // Get the module type
                OpenBlocksModule.Type module_type = OpenBlocksModule.Type.valueOf(current_module_info.getString("type"));

                // Check if the arraylist is already initialized
                if (!modules.containsKey(module_type)) {
                    // Oop, it haven't, let's initialize it
                    modules.put(module_type, new ArrayList<>());
                }

                Module module = new Module(
                        current_module_info.getString("name"),
                        current_module_info.getString("description"),
                        current_module_info.getString("classpath"),
                        current_module_info.getInt("version"),
                        current_module_info.getInt("lib_version"),
                        jar_file.second
                );

                // ohk, add the module
                Objects.requireNonNull(
                        modules.get(module_type)
                ).add(module);

                // Check if this module exists in the active modules list
                if (active_modules_json.getString(module_type.toString()).equals(filename)) {
                    // Alright, this module is active, add it to this active_modules hashmap
                    active_modules.put(module_type, module);
                }

            } catch (JSONException ignored) { } // We're gonna ignore the error, and go on
        }
    }

    public HashMap<OpenBlocksModule.Type, ArrayList<Module>> getModules() {
        return modules;
    }

    public Module importModule(Context context, String path) throws IOException, JSONException {
        Module module = new Module();
        OpenBlocksModule.Type module_type = null;
        File modules_dir = new File(context.getFilesDir(), "modules");
        File jar_file = null;

        FileInputStream fis;

        // Buffer for read and write data to file
        byte[] buffer = new byte[1024];

        fis = new FileInputStream(path);

        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {
            String fileName = ze.getName();

            if (fileName.matches("^\\w+\\.jar$")) {
                jar_file = new File(modules_dir, ze.getName());
                FileOutputStream fos = new FileOutputStream(jar_file);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
            } else if (fileName.equals("openblocks-module-manifest.json")) {
                // Source: https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
                String file_data;
                ByteArrayOutputStream result = new ByteArrayOutputStream();

                int length;
                while ((length = zis.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }

                file_data = result.toString("UTF-8");

                JSONObject manifest = new JSONObject(file_data);
                module.name = manifest.getString("name");
                module.description = manifest.getString("description");
                module.classpath = manifest.getString("classpath");
                module.version = manifest.getInt("version");
                module.lib_version = manifest.getInt("lib_version");

                module_type = OpenBlocksModule.Type.valueOf(manifest.getString("type"));
            }

            // Close this ZipEntry
            zis.closeEntry();

            // Ight, next!
            ze = zis.getNextEntry();
        }

        // Close last ZipEntry
        zis.closeEntry();
        zis.close();
        fis.close();

        // Check if there isn't any jar file
        if (jar_file == null) {
            throw new IOException("Jar file doesn't exists");
        }

        // ok, let's put the jar_file here
        module.jar_file = jar_file;

        // Check if this module_type hasn't been initialized
        if (!modules.containsKey(module_type)) {
            modules.put(module_type, new ArrayList<>());
        }

        // alright, let's put it on our modules list
        modules .get(module_type)
                .add(module);

        // Ight we can return the module
        return module;
    }
    
    public boolean removeModule(OpenBlocksModule.Type module_type, Module module) throws ModuleIsActiveException {
        if (!modules.containsKey(module_type))
            throw new IllegalArgumentException("Module type " + module_type.toString() + " doesn't exist in the modules list");

        if (active_modules.get(module_type).equals(module))
            throw new ModuleIsActiveException("You cannot remove an activated module");

        return modules.get(module_type).remove(module);
    }
}
