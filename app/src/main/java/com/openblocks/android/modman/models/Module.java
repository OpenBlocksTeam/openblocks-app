package com.openblocks.android.modman.models;

import java.io.File;

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
