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
import com.openblocks.moduleinterface.OpenBlocksModule;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class ModulesRecyclerViewAdapter extends RecyclerView.Adapter<ModulesRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ModuleRVAdapter";

    // Should be the database's commit objects
    private HashMap<OpenBlocksModule.Type, ArrayList<Module>> data = new HashMap<>();
    // final private Activity activity;
    WeakReference<Activity> activity;

    public ModulesRecyclerViewAdapter(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public ModulesRecyclerViewAdapter(HashMap<OpenBlocksModule.Type, ArrayList<Module>> data, Activity activity) {
        this.data = data;
        this.activity = new WeakReference<>(activity);
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
        // Module item = datas.get(position);

    }

    @Override
    public int getItemCount() {
        int sum = 0;

        for (ArrayList<Module> modules: data.values()) {
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
