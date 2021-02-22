package com.openblocks.android.modman.models;

import android.util.Pair;

import com.openblocks.moduleinterface.models.OpenBlocksFile;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class Module {
    public String name;
    public String description;
    public String classpath;

    public int version;
    public int lib_version;

    public File jar_file;

    public Module() { }

    public Module(String name, String description, String classpath, int version, int lib_version, File jar_file) {
        this.name = name;
        this.description = description;
        this.classpath = classpath;
        this.version = version;
        this.lib_version = lib_version;
        this.jar_file = jar_file;
    }
}
