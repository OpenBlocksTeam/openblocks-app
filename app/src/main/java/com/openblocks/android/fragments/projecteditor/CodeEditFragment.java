package com.openblocks.android.fragments.projecteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.openblocks.android.R;
import com.openblocks.android.modman.ModuleLoader;
import com.openblocks.android.modman.ModuleManager;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;
import com.openblocks.moduleinterface.callbacks.SaveCallback;
import com.openblocks.moduleinterface.models.OpenBlocksProjectMetadata;
import com.openblocks.moduleinterface.projectfiles.OpenBlocksCode;
import com.openblocks.moduleinterface.projectfiles.OpenBlocksLayout;

public class CodeEditFragment extends Fragment {

    OpenBlocksCode code;
    OpenBlocksLayout layout;
    OpenBlocksProjectMetadata metadata;
    SaveCallback<OpenBlocksCode> code_save;
    OpenBlocksModule.ProjectCodeGUI module_instance;

    public CodeEditFragment(OpenBlocksCode code, OpenBlocksLayout layout, OpenBlocksProjectMetadata metadata, SaveCallback<OpenBlocksCode> code_save) {
        this.code = code;
        this.layout = layout;
        this.metadata = metadata;
        this.code_save = code_save;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ModuleManager moduleManager = ModuleManager.getInstance();
        Module code_ui_module = moduleManager.getActiveModule(OpenBlocksModule.Type.PROJECT_CODE_GUI);
        module_instance = ModuleLoader.load(requireContext(), code_ui_module, OpenBlocksModule.ProjectCodeGUI.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_code_edit, container, false);
        module_instance.show(requireContext(), root.findViewById(R.id.code_edit_parent), code, layout, metadata, code_save);
        return root;
    }
}