package com.openblocks.android.modman;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openblocks.android.helpers.FileHelper;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dalvik.system.DexClassLoader;

/**
 * This singleton class is used to add, remove, and import (manage) modules
 */
public class ModuleManager {

    private static ModuleManager single_instance = null;

    // HashMap<ModuleType: OpenBlocksModule.Type, Modules: ArrayList<Module>>
    HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules;

    // NOTE: modules in active_modules also exists in the variable modules
    HashMap<OpenBlocksModule.Type, Module> active_modules;

    // A list of modules with their jar
    ArrayList<Pair<File, ArrayList<Module>>> modules_with_jar;

    private ModuleManager() { }

    // Yes this is a singleton class
    public static ModuleManager getInstance() {
        if (single_instance == null)
            single_instance = new ModuleManager();

        return single_instance;
    }

    public HashMap<OpenBlocksModule.Type, ArrayList<Module>> getModules() {
        return modules;
    }

    public ArrayList<Module> getModulesAsList() {
        ArrayList<Module> result = new ArrayList<>();

        for (OpenBlocksModule.Type type : modules.keySet()) {
            result.addAll(Objects.requireNonNull(modules.get(type)));
        }

        return result;
    }

    @Nullable
    public Module getActiveModule(OpenBlocksModule.Type type) {
        return active_modules.get(type);
    }

    /**
     * This function loads every defined modules on the local modules path and in modules.json
     *
     * @param context The context
     * @throws IOException When an IO error occurs
     * @throws ModuleJsonCorruptedException When the modules.json is corrupted / malformed
     */
    public void fetchAllModules(Context context) throws IOException, ModuleJsonCorruptedException {
        // Modules are stored on the internal directory /modules/
        File modules_directory = new File(context.getFilesDir(), "modules");

        JSONObject modules_information = null;
        JSONObject active_modules_json = null;
        ArrayList<File> jar_files = new ArrayList<>();

        // Clear everything first
        modules = new HashMap<>();
        active_modules = new HashMap<>();
        modules_with_jar = new ArrayList<>();

        /* The module folder should contain something like this:
         *
         * modules
         *  L IyxanProjectManager.jar
         *  L BOFACompiler.jar
         *  L OpenModule-CodeEditor.jar
         *  ...
         *  L modules.json <- Contains information about these jars, like description, name, classpath, etc
         */

        /* Old modules.json should be something like this:
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

        /* New modules.json structure, enabling multiple modules in a single jar
         * https://github.com/OpenBlocksTeam/openblocks-module-interface/wiki/Multiple-modules-in-a-single-jar
         *
         * {
         *   "active_modules": {
         *     "PROJECT_MANAGER": {"jar": "IyxanBofaModules.jar", "name": "IyxanProjectManager"},
         *     "PROJECT_COMPILER": {"jar": "IyxanBofaModules.jar", "name": "BofaProjectParser"},
         *     ...
         *   },
         *   "modules": {
         *     "IyxanBofaModules.jar": [
         *       {
         *         "name": "IyxanProjectManager",
         *         "description": "Iyxan's personal project manager",
         *         "classpath": "com.openblocks.modules.IyxanProjectManager",
         *         "version": 1,
         *         "lib_version": 1,
         *         "type": "PROJECT_MANAGER"
         *       },
         *       {
         *         "name": "BofaProjectParser",
         *         "description": "Airplane-optimized project parser ✈️",
         *         "classpath": "com.openblocks.modules.BofaProjectParser",
         *         "version": 1.1,
         *         "lib_version": 1,
         *         "type": "PROJECT_PARSER"
         *       }
         *     ]
         *   }
         * }
         */

        // Create the module directory if it doesn't exist
        if (!modules_directory.exists()) {
            if (!modules_directory.mkdir()) {
                throw new IOException("Failed to create the modules directory for unknown reason");
            }
        }

        // Loop per files, extract their information, and add them to the modules HashMap<ArrayList>
        for (File module: Objects.requireNonNull(modules_directory.listFiles())) {
            if (module.getName().equals("modules.json")) {

                try {
                    JSONObject modules_json = new JSONObject(new String(FileHelper.readFile(module), StandardCharsets.UTF_8));

                    modules_information = modules_json.getJSONObject("modules");
                    active_modules_json = modules_json.getJSONObject("active_modules");
                } catch (JSONException e) {
                    throw new ModuleJsonCorruptedException(e.getMessage());
                }

            } else {
                // This is a jar file
                jar_files.add(module);
            }
        }

        // just return empty handed if there is no modules.json
        if (modules_information == null) {
            return;
        }

        // Loop per every jar files
        for (File jar_file : jar_files) {
            try {
                String filename = jar_file.getName();

                // Because there might be multiple modules inside the jar, we should loop
                JSONArray modules_inside_jar = modules_information.getJSONArray(filename);
                ArrayList<Module> modules_inside_jar_list = new ArrayList<>();

                for (int i = 0; i < modules_inside_jar.length(); i++) {
                    JSONObject current_module_info = modules_inside_jar.getJSONObject(i);

                    OpenBlocksModule.Type module_type = OpenBlocksModule.Type.valueOf(current_module_info.getString("type"));
                    String module_name = current_module_info.getString("name");

                    Module module = new Module(
                            module_name,
                            current_module_info.getString("description"),
                            current_module_info.getString("classpath"),
                            current_module_info.getInt("version"),
                            current_module_info.getInt("lib_version"),
                            jar_file,
                            module_type
                    );

                    modules_inside_jar_list.add(module);

                    // ohk, add the module
                    // Check if the current module type is empty / uninitialized
                    if (modules.get(module_type) == null) {
                        modules.put(module_type, new ArrayList<>());
                    }

                    // Then add the module
                    Objects.requireNonNull(
                            modules.get(module_type)
                    ).add(module);

                    // Check if this module is defined in the active modules child
                    JSONObject current_module_type_active_module = active_modules_json.getJSONObject(module_type.toString());

                    // Check both the jar and the name
                    if (current_module_type_active_module.getString("jar").equals(jar_file.getName()) &&
                        current_module_type_active_module.getString("name").equals(module_name)) {
                        // Alright, this module is active, add it to this active_modules hashmap
                        active_modules.put(module_type, module);
                    }
                }

                modules_with_jar.add(new Pair<>(jar_file, modules_inside_jar_list));

            } catch (JSONException ignored) {
                ignored.printStackTrace();
            } // We're gonna ignore the error, and go on (because the jar we're parsing isn't defied in modules.json)
        }
    }

    /**
     * This function fetches the module's class into a class that we can invoke functions to
     *
     * @param context The context
     * @param module The module that is to be loaded
     * @return The loaded class of the module given
     * @throws ClassNotFoundException When the module's classpath is wrong / corrupted
     */
    public Class<Object> fetchModule(Context context, Module module) throws ClassNotFoundException {
        if (module != null && module.jar_file != null) {
            final DexClassLoader classloader =
                    new DexClassLoader(
                            module.jar_file.getAbsolutePath(),
                            context.getCodeCacheDir().getAbsolutePath(),
                            null,
                            this.getClass().getClassLoader()
                    );
            return (Class<Object>) classloader.loadClass(module.classpath);
        }

        return null;
    }

    /**
     * This function imports a module from a path, a wrapper of {@link #importModule(Context, FileInputStream)}
     *
     * Note: The imported module will be automatically added to the modules list
     *
     * @param context The context
     * @param path The path where the module is located
     * @return The imported module
     * @throws IOException When an IO error occurs
     * @throws JSONException When the module is corrupted / malformed
     */
    public ArrayList<Module> importModule(Context context, String path) throws IOException, JSONException {
        FileInputStream fis = new FileInputStream(path);
        return importModule(context, fis);
    }

    /**
     * This function imports a module from an InputStream
     *
     * Note: The imported module will be automatically added to the modules list
     *
     * @param context The context
     * @param inputStream The path where the module is located
     * @return The imported module
     * @throws IOException When an IO error occurs
     * @throws JSONException When the module is corrupted / malformed
     */
    public ArrayList<Module> importModule(Context context, FileInputStream inputStream) throws IOException, JSONException {
        ArrayList<Module> modules_inside_jar = new ArrayList<>();
        File modules_dir = new File(context.getFilesDir(), "modules");
        File modules_resources_dir = new File(context.getFilesDir(), "modules_resources");

        // This folder is used to extract any resources inside the module, then will be moved to
        // modules_resources
        File modules_resources_tmp_dir = new File(context.getFilesDir(), "tmp_res");

        File jar_file = null;

        JSONObject manifest = null;

        FileInputStream zip_fileInputStream;

        // Buffer for read and write data to file
        byte[] buffer = new byte[1024];

        zip_fileInputStream = inputStream;

        ZipInputStream zipInputStream = new ZipInputStream(zip_fileInputStream);
        ZipEntry zipEntry = zipInputStream.getNextEntry();

        while (zipEntry != null) {
            String fileName = zipEntry.getName();

            if (fileName.matches("^\\w+\\.jar$")) {
                jar_file = new File(modules_dir, zipEntry.getName());
                FileOutputStream fos = new FileOutputStream(jar_file);

                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();

            } else if (fileName.equals("openblocks-module-manifest.json")) {
                // Source: https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
                String file_data;
                ByteArrayOutputStream result = new ByteArrayOutputStream();

                int length;
                while ((length = zipInputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }

                file_data = result.toString("UTF-8");

                // Read the manifest
                manifest = new JSONObject(file_data);

            } else if (fileName.startsWith("res/") && !zipEntry.isDirectory()) {
                // This is a file on the res folder, extract them to modules_resources_tmp_dir
                FileOutputStream extract_file = new FileOutputStream(new File(modules_resources_tmp_dir, fileName));

                int length;
                while ((length = zipInputStream.read(buffer)) != -1) {
                    extract_file.write(buffer, 0, length);
                }
            }

            // Close this ZipEntry
            zipInputStream.closeEntry();

            // Ight, next!
            zipEntry = zipInputStream.getNextEntry();
        }

        // No need to close the last ZipEntry, it's been closed already
        // Let's just close the ZIP file instead.
        zipInputStream.close();
        zip_fileInputStream.close();

        // Check if there isn't any jar file
        if (jar_file == null) {
            throw new IOException("JAR file doesn't exist!");
        }

        if (manifest == null) {
            throw new IOException("Manifest file doesn't exists");
        }

        // Because the jar file isn't null, let's move the resources to modules_resources

        FileHelper.moveFiles(modules_resources_tmp_dir, new File(modules_resources_dir, jar_file.getName()));

        // Then parse the manifest of the module

        // Parse the manifest
        ArrayList<Module> modules_inside_manifest = parseManifest(jar_file, manifest);

        // Add it to this arraylist
        modules_inside_jar.addAll(modules_inside_manifest);

        // Loop for each modules
        for (Module module : modules_inside_manifest) {
            // Then add them to the modules collection
            OpenBlocksModule.Type module_type = module.module_type;

            // Check if this module_type hasn't been initialized
            if (!modules.containsKey(module_type)) {
                modules.put(module_type, new ArrayList<>());
            }

            // alright, let's put it on our modules list
            modules .get(module_type)
                    .add(module);
        }

        // Save it to the global modules_with_jar variable
        modules_with_jar.add(new Pair<>(jar_file, modules_inside_jar));

        // Then save the module to modules.json
        saveModules(context);

        // Ight we can return the module
        return modules_inside_jar;
    }

    /**
     * This private function parses a manifest and returns a list of modules that it defines
     *
     * @param jar_file The jar file the manifest came with
     * @param manifest The manifest itself
     * @return A list of modules defined in the manifest
     * @throws JSONException If this manifest is corrupted
     */
    private ArrayList<Module> parseManifest(File jar_file, JSONObject manifest) throws JSONException {
        ArrayList<Module> result = new ArrayList<>();
        JSONArray modules = manifest.getJSONArray("modules");

        for (int i = 0; i < modules.length(); i++) {
            JSONObject current_module = modules.getJSONObject(i);

            Module module = new Module();
            module.name = current_module.getString("name");
            module.description = current_module.getString("description");
            module.classpath = current_module.getString("classpath");
            module.version = current_module.getInt("version");
            module.lib_version = current_module.getInt("lib_version");
            module.module_type = OpenBlocksModule.Type.valueOf(current_module.getString("type"));

            // Set the jar metadata
            module.jar_file = jar_file;

            result.add(module);
        }

        return result;
    }

    /**
     * This function removes a module from the modules list
     *
     * @param module_type The module type of the module that is to be removed
     * @param module The module itself
     * @return true if success, false if failed
     */
    public boolean removeModule(OpenBlocksModule.Type module_type, Module module) {
        if (!modules.containsKey(module_type))
            throw new IllegalArgumentException("Module type " + module_type.toString() + " doesn't exist in the modules list");

        if (Objects.equals(active_modules.get(module_type), module))
            throw new IllegalArgumentException("You cannot remove an activated module");

        return modules.get(module_type).remove(module);
    }

    /**
     * This function activates a module
     *
     * @param module_type The module type of the module that is to be activated
     * @param module The module that is to be activated
     */
    public void activateModule(@NonNull OpenBlocksModule.Type module_type, @NonNull Module module) {
        if (!modules.containsKey(module_type))
            throw new IllegalArgumentException("Module type " + module_type.toString() + " doesn't exist in the modules list");

        active_modules.put(module_type, module);
    }

    /**
     * This function saves the activated modules into the modules.json file
     *
     * @param context The context
     * @throws IOException When an IO error occurs
     * @throws JSONException When the modules.json is corrupted / malformed
     */
    private void saveActiveModules(Context context) throws IOException, JSONException {
        // Get the modules directory
        File modules_directory = new File(context.getFilesDir(), "modules");

        // Then parse it
        File modules_json = new File(modules_directory, "modules.json");
        JSONObject modules_info = new JSONObject(new String(FileHelper.readFile(modules_json), StandardCharsets.UTF_8));

        // Loop per each type
        for (OpenBlocksModule.Type type: active_modules.keySet()) {
            Module current_module = active_modules.get(type);

            if (current_module == null)
                continue;

            JSONObject active_module_json = new JSONObject();
            active_module_json.put("jar", current_module.jar_file);
            active_module_json.put("name", current_module.name);

            // Update the module
            modules_info.getJSONObject("active_modules").put(type.toString(), active_module_json);
        }

        // Then put it back
        FileHelper.writeFile(modules_json, modules_info.toString().getBytes());
    }

    /**
     * This function saves every modules that has been loaded (and edited) into the modules.json
     * file
     *
     * @param context The context
     * @throws IOException When an IO error occurs
     * @throws JSONException When the modules.json is corrupted / malformed
     */
    private void saveModules(Context context) throws IOException, JSONException {
        if (modules == null)
            return;

        File modules_directory = new File(context.getFilesDir(), "modules");

        // Then parse it
        File modules_json = new File(modules_directory, "modules.json");
        JSONObject modules_info = new JSONObject(new String(FileHelper.readFile(modules_json), StandardCharsets.UTF_8));

        // Check if modules key is set, if not, set it
        if (!modules_info.has("modules")) {
            modules_info.put("modules", new JSONObject());
        }

        // Loop per each modules
        for (Pair<File, ArrayList<Module>> jar_modules_pair : modules_with_jar) {

            JSONArray modules_inside_jar = new JSONArray();

            for (Module module : jar_modules_pair.second) {
                // Put the module into a JSONObject
                JSONObject module_json = new JSONObject();
                module_json.put("name", module.name);
                module_json.put("description", module.description);
                module_json.put("classpath", module.classpath);
                module_json.put("type", module.module_type);
                module_json.put("version", module.version);
                module_json.put("lib_version", module.lib_version);

                // Finally, update the module
                modules_inside_jar.put(module_json);
            }

            modules_info.getJSONObject("modules").put(jar_modules_pair.first.getName(), modules_inside_jar);
        }

        // Then put it back
        FileHelper.writeFile(modules_json, modules_info.toString().getBytes());
    }

    /**
     * This function is used to find a module with the specific name
     * @param type The module type
     * @param name The module name
     * @see #findModule(OpenBlocksModule.Type, Finder) 
     * @return The module with the specified type and name, will return null if none found
     */
    @Nullable
    public Module findModule(OpenBlocksModule.Type type, String name) {
        return findModule(type, module -> name.equals(module.name));
    }

    /**
     * This function is used to find a module with the specified finder
     * @param type The module type
     * @param finder The finder, will check for each modules
     * @see #findModule(OpenBlocksModule.Type, String) 
     * @return The module that checked correctly on the finder, will return null if none found
     */
    @Nullable
    public Module findModule(OpenBlocksModule.Type type, Finder finder) {
        // Check if the modules list has any modules with the type
        if (!modules.containsKey(type))
            // None, return null instead
            return null;

        // Loop over each modules and check if their name is the same
        for (Module module : modules.get(type)) {
            if (finder.check(module)) {
                return module;
            }
        }

        // If none, return null
        return null;
    }

    /**
     * A simple interface used to find a module used in {@link #findModule(OpenBlocksModule.Type, Finder)}
     */
    public interface Finder {
        boolean check(Module module);
    }
}
