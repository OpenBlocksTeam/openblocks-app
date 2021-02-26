package com.openblocks.android.modman;

// This exception will be thrown if the modules.json is corrupted
public class ModuleJsonCorruptedException extends Exception {
    public ModuleJsonCorruptedException(String message) { super(message); }
}
