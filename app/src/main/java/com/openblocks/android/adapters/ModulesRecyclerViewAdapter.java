package com.openblocks.android.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openblocks.android.R;
import com.openblocks.android.modman.models.Module;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ModulesRecyclerViewAdapter extends RecyclerView.Adapter<ModulesRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ModuleRVAdapter";

    // Should be the database's commit objects
    private ArrayList<Module> datas = new ArrayList<>();
    // final private Activity activity;
    WeakReference<Activity> activity;

    public ModulesRecyclerViewAdapter(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public ModulesRecyclerViewAdapter(ArrayList<Module> datas, Activity activity) {
        this.datas = datas;
        this.activity = new WeakReference<>(activity);
    }

    public void updateView(ArrayList<Module> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.rv_module_item, parent, false)
        );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        Module item = datas.get(position);

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;

        ImageView module_type;

        View body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
