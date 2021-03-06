package com.openblocks.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openblocks.android.R;
import com.openblocks.moduleinterface.models.OpenBlocksProjectMetadata;

import java.util.ArrayList;

public class ProjectRecyclerViewAdapter extends RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder> {

    ArrayList<OpenBlocksProjectMetadata> projects_metadata;

    public ProjectRecyclerViewAdapter(ArrayList<OpenBlocksProjectMetadata> projects_metadata) {
        this.projects_metadata = projects_metadata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_project_item, parent)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OpenBlocksProjectMetadata metadata = projects_metadata.get(position);

        holder.project_title.setText(metadata.getName());
        holder.project_package.setText(metadata.getPackageName());
    }

    @Override
    public int getItemCount() {
        return projects_metadata.size() - 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView project_title;
        TextView project_package;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            project_title = itemView.findViewById(R.id.project_title);
            project_package = itemView.findViewById(R.id.project_package);
        }
    }
}
