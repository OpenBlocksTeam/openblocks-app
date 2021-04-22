package com.openblocks.android.helpers;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.core.util.Pair;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class FileHelper {

    /**
     * This static function extracts raw resource into the specified folder
     * the name of the file will depends on the name of the resource name
     * @param resources The resources
     * @param res_id The raw resource id that wants to be extracted
     * @param dest The destination folder
     * @throws IOException When something goes wrong
     */
    public static void extractRawResource(Resources resources, @RawRes int res_id, File dest) throws IOException {
        if (!dest.exists()) {
            dest.mkdirs();
        }

        InputStream raw_resource_is = resources.openRawResource(res_id);

        String name = resources.getResourceEntryName(res_id);

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

    @NonNull
    public static String readFile(File file) throws IOException {
        return readFile(file.getAbsolutePath());
    }

    // Copied from: https://www.journaldev.com/9400/android-external-storage-read-write-save-file
    @NonNull
    public static String readFile(String path) throws IOException {
        StringBuilder output = new StringBuilder();
        FileInputStream fis = new FileInputStream(path);
        DataInputStream in = new DataInputStream(fis);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;

        while ((strLine = br.readLine()) != null) {
            output.append(strLine);
        }

        in.close();
        return output.toString();
    }
}
