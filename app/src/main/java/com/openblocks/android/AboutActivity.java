package com.openblocks.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.openblocks.android.adapters.ContributorsRecyclerViewAdapter;
import com.openblocks.android.databinding.ActivityAboutBinding;
import com.openblocks.android.models.ContributorItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbarAbout;
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }

        JSONArray contributors;
        List<ContributorItem> contributors_list;

        try {
            URL url = new URL("https://api.github.com/repos/OpenBlocksTeam/openblocks-app/contributors");
            HttpURLConnection url_connection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(url_connection.getInputStream());
            ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            while (in.read(buffer) != -1) {
                output_stream.write(buffer);
            }

            url_connection.disconnect();

            contributors = new JSONArray(output_stream.toString("utf-8"));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IOException: " + e.getMessage(), Toast.LENGTH_LONG).show();

            return;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Corrupted json: " + e.getMessage(), Toast.LENGTH_LONG).show();

            return;
        }

        contributors_list = new ArrayList<>();

        try {
            for (int i = 0; i < contributors.length(); i++) {
                JSONObject contributor = contributors.getJSONObject(i);

                contributors_list.add(
                        new ContributorItem(
                                contributor.getString("login"),
                                contributor.getString("avatar_url"),
                                contributor.getString("html_url")
                        )
                );
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unexpected json error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            return;
        }

        ContributorsRecyclerViewAdapter adapter = new ContributorsRecyclerViewAdapter(contributors_list, this);
        RecyclerView contributors_recycler_view = binding.contributors;
        contributors_recycler_view.setAdapter(adapter);
        contributors_recycler_view.setLayoutManager(new LinearLayoutManager(this));
    }
}