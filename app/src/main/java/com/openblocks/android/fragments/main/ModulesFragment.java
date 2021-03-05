package com.openblocks.android.fragments.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openblocks.android.R;
import com.openblocks.android.adapters.ModulesRecyclerViewAdapter;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModulesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModulesFragment extends Fragment {

    // TODO: MVVM

    // Every modules (unsorted), this is used to display the recyclerview
    HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules;

    public ModulesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param modules List of Modules
     * @return A new instance of fragment ModulesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModulesFragment newInstance(HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules) {
        ModulesFragment fragment = new ModulesFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("PROJECT_MANAGER", modules.get(OpenBlocksModule.Type.PROJECT_MANAGER));
        args.putParcelableArrayList("PROJECT_PARSER", modules.get(OpenBlocksModule.Type.PROJECT_PARSER));
        args.putParcelableArrayList("PROJECT_CODE_GUI", modules.get(OpenBlocksModule.Type.PROJECT_CODE_GUI));
        args.putParcelableArrayList("PROJECT_LAYOUT_GUI", modules.get(OpenBlocksModule.Type.PROJECT_LAYOUT_GUI));
        args.putParcelableArrayList("PROJECT_COMPILER", modules.get(OpenBlocksModule.Type.PROJECT_COMPILER));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modules = new HashMap<>();

            modules.put(OpenBlocksModule.Type.PROJECT_MANAGER, getArguments().getParcelableArrayList("PROJECT_MANAGER"));
            modules.put(OpenBlocksModule.Type.PROJECT_PARSER, getArguments().getParcelableArrayList("PROJECT_PARSER"));
            modules.put(OpenBlocksModule.Type.PROJECT_CODE_GUI, getArguments().getParcelableArrayList("PROJECT_CODE_GUI"));
            modules.put(OpenBlocksModule.Type.PROJECT_LAYOUT_GUI, getArguments().getParcelableArrayList("PROJECT_LAYOUT_GUI"));
            modules.put(OpenBlocksModule.Type.PROJECT_COMPILER, getArguments().getParcelableArrayList("PROJECT_COMPILER"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_modules, container, false);

        RecyclerView modules_list = root.findViewById(R.id.modules_list);
        modules_list.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        ModulesRecyclerViewAdapter adapter = new ModulesRecyclerViewAdapter(modules, requireActivity());
        modules_list.setAdapter(adapter);

        return root;
    }
}