package com.openblocks.android.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.openblocks.android.R;
import com.openblocks.android.models.ContributorItem;

import java.lang.ref.WeakReference;
import java.nio.file.StandardWatchEventKinds;
import java.util.List;

public class ContributorsRecyclerViewAdapter extends RecyclerView.Adapter<ContributorsRecyclerViewAdapter.ViewHolder> {

    List<ContributorItem> contributors;
    WeakReference<Activity> activity;

    public ContributorsRecyclerViewAdapter(List<ContributorItem> contributors, Activity activity) {
        this.contributors = contributors;
        this.activity = new WeakReference<>(activity);
    }

    public void updateView(List<ContributorItem> contributors) {
        this.contributors = contributors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.rv_contributor_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContributorItem current_item = contributors.get(position);
        holder.name.setText(current_item.name);

        holder.github_link.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(current_item.github_link));

            activity.get().startActivity(i);
        });

        ShapeAppearanceModel sam = new ShapeAppearanceModel();
        holder.profile_picture.setShapeAppearanceModel(
                sam
                    .toBuilder()
                    .setAllCorners(CornerFamily.ROUNDED, 100)
                    .build()
        );
    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        View github_link;
        ShapeableImageView profile_picture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            github_link = itemView.findViewById(R.id.github_link);
            profile_picture = itemView.findViewById(R.id.profile_picture);
        }
    }
}
