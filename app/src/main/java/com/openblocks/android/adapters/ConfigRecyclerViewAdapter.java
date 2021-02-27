package com.openblocks.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.openblocks.android.R;
import com.openblocks.moduleinterface.models.config.OpenBlocksConfig;
import com.openblocks.moduleinterface.models.config.OpenBlocksConfigItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ConfigRecyclerViewAdapter extends RecyclerView.Adapter<ConfigRecyclerViewAdapter.ViewHolder> {

    ArrayList<OpenBlocksConfigItem> configItems;

    public ConfigRecyclerViewAdapter(ArrayList<OpenBlocksConfigItem> configItems) {
        this.configItems = configItems;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OpenBlocksConfigItem item = configItems.get(position);

        Context context = holder.title.getContext();
        View input = null;

        holder.title.setText(item.text);

        switch (item.config_type) {
            case SWITCH:
                SwitchCompat switch_ = new SwitchCompat(context);
                switch_.setShowText(false);
                switch_.setChecked((Boolean) item.value);

                input = switch_;
                break;

            case DROPDOWN:
                Spinner spinner = new Spinner(context);
                spinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, (ArrayList<String>) item.extra));

                input = spinner;
                break;

            case OPEN_FILE:
                MaterialButton button = new MaterialButton(context);
                button.setText("Open File");
                button.setOnClickListener(v -> {
                    // TODO
                });

                input = button;
                break;

            case INPUT_TEXT:
                EditText editText = new EditText(context);
                editText.setText((String) item.value);
                editText.setHint("Input here");

                input = editText;
                break;

            case INPUT_NUMBER:
                EditText editText_ = new EditText(context);
                editText_.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                editText_.setText((String) item.value);
                editText_.setHint("Input here");

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
                }).setPositiveButton("OK", (dialog, id) -> {
                    // TODO
                }).setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

                break;

            default:
                break;
        }

        holder.holder.addView(input);
    }

    @Override
    public int getItemCount() {
        return configItems.size();
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
