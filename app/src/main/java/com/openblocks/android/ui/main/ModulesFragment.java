package com.openblocks.android.ui.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.openblocks.android.R;
import com.openblocks.android.adapters.ModulesRecyclerViewAdapter;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class ModulesFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "modules";

    private ModulesViewModel pageViewModel;

    public static ModulesFragment newInstance(HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules) {
        ModulesFragment fragment = new ModulesFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_SECTION_NUMBER, (Parcelable) modules);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageViewModel = new ViewModelProvider(this).get(ModulesViewModel.class);

        HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules = new HashMap<>();
        if (getArguments() != null) {
            modules = getArguments().getParcelable(ARG_SECTION_NUMBER);
        }

        pageViewModel.setModules(modules);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_modules, container, false);

        final RecyclerView modules_list = root.findViewById(R.id.modules_list);

        // StaggeredGridLayoutManager with 3 rows and horizontal orientation
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.HORIZONTAL);
        modules_list.setLayoutManager(staggeredGridLayoutManager);

        final ModulesRecyclerViewAdapter adapter = new ModulesRecyclerViewAdapter(getActivity());

        // Set the adapter
        modules_list.setAdapter(adapter);

        // Update the adapter if the variable "modules" is changed
        pageViewModel.getModules().observe(getViewLifecycleOwner(), adapter::updateView);

        return root;
    }
}