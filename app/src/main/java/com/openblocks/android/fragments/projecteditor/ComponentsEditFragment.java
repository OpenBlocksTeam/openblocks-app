package com.openblocks.android.fragments.projecteditor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openblocks.android.R;

// Component is not planned yet, It's currently used just for a placeholder
public class ComponentsEditFragment extends Fragment {

    public ComponentsEditFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_components_edit, container, false);
    }
}