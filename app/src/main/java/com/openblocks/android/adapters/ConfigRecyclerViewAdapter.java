package com.openblocks.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.openblocks.android.ModuleConfigActivity;
import com.openblocks.android.R;
import com.openblocks.moduleinterface.models.config.OpenBlocksConfig;
import com.openblocks.moduleinterface.models.config.OpenBlocksConfigItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class ConfigRecyclerViewAdapter extends RecyclerView.Adapter<ConfigRecyclerViewAdapter.ViewHolder> {

    OpenBlocksConfig config;

    // Why WeakReference? Because setting this as plain activity can cause memory leaks, the java
    //                    garbage collector doesn't collect variables that have strong reference
    WeakReference<Activity> activity;

    // TAG: VALUE
    HashMap<String, Object> saved_items = new HashMap<>();

    public ConfigRecyclerViewAdapter(OpenBlocksConfig configItems, Activity activity) {
        this.config = configItems;
        this.activity = new WeakReference<>(activity);
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

    public OpenBlocksConfig getConfig() {
        for (String tag: saved_items.keySet()) {
            config.setConfigValue(tag, saved_items.get(tag));
        }

        return config;
    }

    public void onResult(int requestCode, Intent data) {
        // Set the value as the path
        saved_items.put(config.getTAGs()[requestCode], data.getData().getPath());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OpenBlocksConfigItem item = config.getConfigs().get(position);
        String[] tags = config.getTAGs();

        Context context = holder.title.getContext();
        View input = null;

        holder.title.setText(item.text);

        switch (item.config_type) {
            case SWITCH:
                SwitchCompat switch_ = new SwitchCompat(context);
                switch_.setShowText(false);
                switch_.setChecked((Boolean) item.value);

                switch_.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    saved_items.put(tags[position], isChecked);
                });

                input = switch_;
                break;

            case DROPDOWN:
                Spinner spinner = new Spinner(context);
                spinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, (ArrayList<String>) item.extra));

                spinner.setOnItemClickListener((parent, view, position1, id) ->
                        saved_items.put(
                                tags[position],
                                ((String[]) item.extra)[position1]
                        )
                );

                input = spinner;
                break;

            case OPEN_FILE:
                MaterialButton button = new MaterialButton(context);
                button.setText("Open File");

                button.setOnClickListener(v -> {
                    Intent i = new Intent();
                    i   .setAction(Intent.ACTION_OPEN_DOCUMENT)
                        .addCategory(Intent.CATEGORY_OPENABLE);

                    activity.get().startActivityForResult(i, position); // Use the position as the request code
                });

                input = button;
                break;

            case INPUT_TEXT:
                EditText editText = new EditText(context);
                editText.setText((String) item.value);
                editText.setHint("Input here");

                editText.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override public void afterTextChanged(Editable s) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        saved_items.put(tags[position], s.toString());
                    }
                });

                input = editText;
                break;

            case INPUT_NUMBER:
                EditText editText_ = new EditText(context);
                editText_.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                editText_.setText((String) item.value);
                editText_.setHint("Input here");

                editText_.addTextChangedListener(new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                    @Override public void afterTextChanged(Editable s) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        saved_items.put(tags[position], Integer.parseInt(s.toString()));
                    }
                });

                input = editText_;
                break;

            case MULTIPLE_CHOICE:
                // https://stackoverflow.com/questions/40700140/how-to-initialize-a-multiple-choice-alert-dialog-with-every-option-selected
                String[] items = (String[]) item.value;
                ArrayList<String> results = new ArrayList<>();

                boolean[] checkedItems = new boolean[items.length];

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMultiChoiceItems(items, checkedItems, (dialog, which, isChecked) -> {
                    if (isChecked && !results.contains(items[which])) {
                        results.add(items[which]);

                    } else if (results.contains(items[which])) {
                        results.remove(which);
                    }

                }).setPositiveButton("OK", (dialog, id) ->
                        saved_items.put(tags[position], results)

                ).setNegativeButton("Cancel", (dialog, id) ->
                        dialog.dismiss()
                );

                break;

            default:
                break;
        }

        holder.holder.addView(input);
    }

    @Override
    public int getItemCount() {
        if (config.getConfigs() == null || config.getConfigs().isEmpty()) {
            return 0;
        }
        return config.getConfigs().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        FrameLayout holder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.config_title);
            holder = itemView.findViewById(R.id.input_holder);
        }
    }
}
