package com.openblocks.android.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.openblocks.android.ModuleConfigActivity;
import com.openblocks.android.R;
import com.openblocks.android.modman.ModuleManager;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class ModulesRecyclerViewAdapter extends RecyclerView.Adapter<ModulesRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ModuleRVAdapter";
    WeakReference<Activity> activity;
    // TODO: REPLACE THIS WITH JUST MODULE MANAGER
    private HashMap<OpenBlocksModule.Type, ArrayList<Module>> data = new HashMap<>();
    private ArrayList<Module> modules;
    private ModuleManager moduleManager;

    public ModulesRecyclerViewAdapter(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public ModulesRecyclerViewAdapter(HashMap<OpenBlocksModule.Type, ArrayList<Module>> data, Activity activity) {
        this.data = data;
        this.activity = new WeakReference<>(activity);

        moduleManager = ModuleManager.getInstance();
        modules = moduleManager.getModulesAsList();
    }

    public void updateView(HashMap<OpenBlocksModule.Type, ArrayList<Module>> data) {
        this.data = data;
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
        Module item = modules.get(position);

        holder.name.setText(item.name);
        holder.description.setText(item.description);

        // Check if this module is active
        if (item.equals(moduleManager.getActiveModule(item.module_type))) {
            // Nop it's inactive, red text with NOT ACTIVE text
            holder.active_status.setText("NOT ACTIVE");
            holder.active_status.setTextColor(0xFFE61212);
        }

        Context context = activity.get();
        Resources resources = context.getResources();
        Resources.Theme theme = context.getTheme();

        switch (item.module_type) {
            case PROJECT_MANAGER:
                holder.module_type.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_project_manager, theme));
                break;
            case PROJECT_PARSER:
                holder.module_type.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_project_parser, theme));
                break;
            case PROJECT_LAYOUT_GUI:
                holder.module_type.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_layout, theme));
                break;
            case PROJECT_CODE_GUI:
                holder.module_type.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_code, theme));
                break;
            case PROJECT_COMPILER:
                holder.module_type.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_compiler, theme));
                break;
        }

        holder.body.setOnClickListener(v -> {
            Intent open_config = new Intent(activity.get(), ModuleConfigActivity.class);
            open_config.putExtra("module", item);
            activity.get().startActivity(open_config);
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;

        int sum = 0;

        for (ArrayList<Module> modules : data.values()) {
            if (modules == null) continue;
            sum += modules.size();
        }

        return sum;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView description;
        TextView active_status;

        ImageView module_type;

        View body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.module_title);
            description = itemView.findViewById(R.id.module_description);
            module_type = itemView.findViewById(R.id.module_type);
            active_status = itemView.findViewById(R.id.module_active_status);

            body = itemView.findViewById(R.id.module_body);
        }
    }
}
