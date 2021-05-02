package com.openblocks.android.fragments.projecteditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.openblocks.android.R;
import com.openblocks.android.modman.ModuleLogger;

// Component is not planned yet, It's currently used just for a placeholder
public class LogFragment extends Fragment {

    ModuleLogger logger = ModuleLogger.getInstance();

    public LogFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_log, container, false);

        // Every time the log is updated, our log text should also be updated
        logger.setLiveLog(log -> {
            TextView log_text = root.findViewById(R.id.log_text);
            log_text.setText(log);
        });

        root.findViewById(R.id.clear_log_button).setOnClickListener(v -> logger.clearLog());

        return root;
    }
}