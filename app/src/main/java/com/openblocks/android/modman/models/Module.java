package com.openblocks.android.modman.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class Module implements Parcelable {
    public String name;
    public String description;
    public String classpath;
    public String filename;

    public int version;
    public int lib_version;

    public File jar_file;

    public Module() { }

    public Module(String filename, String name, String description, String classpath, int version, int lib_version, File jar_file) {
        this.filename = filename;
        this.name = name;
        this.description = description;
        this.classpath = classpath;
        this.version = version;
        this.lib_version = lib_version;
        this.jar_file = jar_file;
    }

    protected Module(Parcel in) {
        name = in.readString();
        description = in.readString();
        classpath = in.readString();
        version = in.readInt();
        lib_version = in.readInt();
        jar_file = new File(in.readString());
    }

    public static final Creator<Module> CREATOR = new Creator<Module>() {
        @Override
        public Module createFromParcel(Parcel in) {
            return new Module(in);
        }

        @Override
        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(classpath);
        dest.writeInt(version);
        dest.writeInt(lib_version);
        dest.writeString(jar_file.getAbsolutePath());
    }
}
