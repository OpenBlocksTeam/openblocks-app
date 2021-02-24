package com.openblocks.android.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

import java.util.ArrayList;
import java.util.HashMap;

public class ModulesViewModel extends ViewModel {

    private final MutableLiveData<
            HashMap<OpenBlocksModule.Type, ArrayList<Module>>
        > modules = new MutableLiveData<>();

    public void setModules(HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules) {
        this.modules.setValue(modules);
    }

    public MutableLiveData<HashMap<OpenBlocksModule.Type, ArrayList<Module>>> getModules() {
        return modules;
    }
}