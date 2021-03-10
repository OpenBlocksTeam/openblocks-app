package com.openblocks.android.fragments.projecteditor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openblocks.android.R;
import com.openblocks.android.modman.ModuleLoader;
import com.openblocks.android.modman.ModuleManager;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;
import com.openblocks.moduleinterface.callbacks.SaveCallback;
import com.openblocks.moduleinterface.projectfiles.OpenBlocksCode;
import com.openblocks.moduleinterface.projectfiles.OpenBlocksLayout;

public class LayoutEditFragment extends Fragment {

    OpenBlocksCode code;
    OpenBlocksLayout layout;
    SaveCallback<OpenBlocksLayout> layout_save;
    OpenBlocksModule.ProjectLayoutGUI module_instance;

    public LayoutEditFragment(OpenBlocksLayout layout, OpenBlocksCode code, SaveCallback<OpenBlocksLayout> code_save) {
        this.layout = layout;
        this.code = code;
        this.layout_save = code_save;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ModuleManager moduleManager = ModuleManager.getInstance();
        Module layout_ui_module = moduleManager.getActiveModule(OpenBlocksModule.Type.PROJECT_LAYOUT_GUI);
        module_instance = ModuleLoader.load(requireContext(), layout_ui_module, OpenBlocksModule.ProjectLayoutGUI.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_code_edit, container, false);
        module_instance.show(requireContext(), root.findViewById(R.id.code_edit_parent), code, layout, layout_save);
        return root;
    }
}