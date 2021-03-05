package com.openblocks.android.fragments.projecteditor;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openblocks.android.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LayoutEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LayoutEditFragment extends Fragment {

    public LayoutEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LayoutEditFragment.
     */
    public static LayoutEditFragment newInstance(String param1, String param2) {
        LayoutEditFragment fragment = new LayoutEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_layout_edit, container, false);
    }
}