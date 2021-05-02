package com.openblocks.android.modman.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.openblocks.moduleinterface.OpenBlocksModule;

import java.io.File;
import java.util.Objects;

public class Module implements Parcelable {
    public String name;
    public String description;
    public String classpath;

    public int version;
    public int lib_version;

    public File jar_file;

    public OpenBlocksModule.Type module_type;

    public Module() { }

    public Module(String name, String description, String classpath, int version, int lib_version, File jar_file, OpenBlocksModule.Type module_type) {
        this.name = name;
        this.description = description;
        this.classpath = classpath;
        this.version = version;
        this.lib_version = lib_version;
        this.jar_file = jar_file;
        this.module_type = module_type;
    }

    protected Module(Parcel in) {
        name = in.readString();
        description = in.readString();
        classpath = in.readString();
        version = in.readInt();
        lib_version = in.readInt();
        jar_file = new File(in.readString());
        module_type = OpenBlocksModule.Type.valueOf(in.readString());
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
        dest.writeString(module_type.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Module module = (Module) o;

        return version == module.version &&
                lib_version == module.lib_version &&
                name.equals(module.name) &&
                description.equals(module.description) &&
                classpath.equals(module.classpath) &&
                jar_file.equals(module.jar_file) &&
                module_type == module.module_type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, classpath, version, lib_version, jar_file, module_type);
    }
}
