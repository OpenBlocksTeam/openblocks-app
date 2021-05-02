package com.openblocks.android.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openblocks.android.R;
import com.openblocks.android.adapters.ProjectRecyclerViewAdapter;
import com.openblocks.moduleinterface.models.OpenBlocksProjectMetadata;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment {

    ArrayList<OpenBlocksProjectMetadata> projectMetadata;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectsFragment.
     */
    public static ProjectsFragment newInstance(ArrayList<OpenBlocksProjectMetadata> projectMetadata) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("projects_metadata", projectMetadata);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.projectMetadata = getArguments().getParcelableArrayList("projects_metadata");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_projects, container, false);

        RecyclerView projects = root.findViewById(R.id.projectRecyclerView);
        ProjectRecyclerViewAdapter adapter = new ProjectRecyclerViewAdapter(projectMetadata);
        projects.setLayoutManager(new LinearLayoutManager(requireContext()));
        projects.setAdapter(adapter);

        return root;
    }
}