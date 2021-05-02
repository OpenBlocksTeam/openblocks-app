package com.openblocks.android.helpers;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.core.util.Pair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Objects;

public class FileHelper {

    /**
     * This static function extracts raw resource into the specified folder with the specified name
     * @param resources The resources
     * @param res_id The raw resource id that wants to be extracted
     * @param dest The destination folder
     * @param name The filename
     * @throws IOException When something goes wrong
     */
    public static void extractRawResource(Resources resources, @RawRes int res_id, File dest, String name) throws IOException {
        InputStream raw_resource_is = resources.openRawResource(res_id);

        // Extract them to the specified directory, with the resource name
        writeFile(new File(dest, name), raw_resource_is);
    }

    public static void writeFile(File file, InputStream stream) throws IOException {
        FileOutputStream file_output = new FileOutputStream(file);
        byte[] buffer = new byte[1024];

        while (stream.read(buffer) != -1) {
            file_output.write(buffer);
        }

        file_output.flush();
        file_output.close();

        stream.close();
    }

    public static void writeFile(File file, byte[] data) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * This function moves every file inside the specified directory to
     * the destination directory
     * @param dir The directory
     * @param dest_dir The destination directory
     */
    public static void moveFiles(@NonNull File dir, @NonNull File dest_dir) {

        if (dir.listFiles() == null) {
            return;
        }

        for (File file : dir.listFiles()) {
            file.renameTo(new File(dest_dir, file.getName()));
        }
    }

    @NonNull
    public static byte[] readFile(File file) throws IOException {
        return readFile(file.getAbsolutePath());
    }
    
    @NonNull
    public static byte[] readFile(String path) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        if (inputStream.read(buffer) != -1) {
            outputStream.write(buffer);
        }

        return outputStream.toByteArray();
    }
}
