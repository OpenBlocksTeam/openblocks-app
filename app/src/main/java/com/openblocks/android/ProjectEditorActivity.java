package com.openblocks.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.openblocks.android.adapters.ProjectRecyclerViewAdapter;
import com.openblocks.android.fragments.main.ModulesFragment;
import com.openblocks.android.fragments.main.ProjectsFragment;
import com.openblocks.android.fragments.projecteditor.CodeEditFragment;
import com.openblocks.android.fragments.projecteditor.ComponentsEditFragment;
import com.openblocks.android.fragments.projecteditor.LayoutEditFragment;
import com.openblocks.android.modman.ModuleLoader;
import com.openblocks.android.modman.ModuleManager;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;
import com.openblocks.moduleinterface.callbacks.SaveCallback;
import com.openblocks.moduleinterface.models.OpenBlocksProjectMetadata;
import com.openblocks.moduleinterface.models.OpenBlocksRawProject;
import com.openblocks.moduleinterface.projectfiles.OpenBlocksCode;
import com.openblocks.moduleinterface.projectfiles.OpenBlocksLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectEditorActivity extends AppCompatActivity {

    ModuleManager moduleManager = ModuleManager.getInstance();

    OpenBlocksCode code;
    OpenBlocksLayout layout;
    OpenBlocksProjectMetadata metadata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_editor);

        // TODO: 3/10/21 Run these on an another thread / asynchronously, Project Parser might take some time to parse the project

        // Read the project
        String project_id = getIntent().getStringExtra("project_id");

        // Get project manager and project parser
        Module project_manager = moduleManager.getActiveModule(OpenBlocksModule.Type.PROJECT_MANAGER);
        Module project_parser = moduleManager.getActiveModule(OpenBlocksModule.Type.PROJECT_PARSER);
        OpenBlocksModule.ProjectManager project_manager_instance = ModuleLoader.load(this, project_manager, OpenBlocksModule.ProjectManager.class);
        OpenBlocksModule.ProjectParser project_parser_instance = ModuleLoader.load(this, project_parser, OpenBlocksModule.ProjectParser.class);

        // Get the project
        OpenBlocksRawProject project = project_manager_instance.getProject(project_id);

        // Parse it
        code = project_parser_instance.parseCode(project);
        layout = project_parser_instance.parseLayout(project);
        metadata = project_parser_instance.parseMetadata(project);

        // Create some save callbacks
        // TODO: 3/10/21 Create a reverse of parse for PROJECT_PARSER module
        SaveCallback<OpenBlocksCode> code_save = code -> {

        };

        SaveCallback<OpenBlocksLayout> layout_save = layout -> {

        };

        // And bind UI elements I guess
        Toolbar toolbar = findViewById(R.id.project_editor_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton run_fab = findViewById(R.id.project_editor_run);
        run_fab.setOnClickListener(view -> {

        });

        ViewPager viewPager = findViewById(R.id.viewPager);

        ProjectEditorViewPagerAdapter viewPagerAdapter = new ProjectEditorViewPagerAdapter(getSupportFragmentManager(), code, layout);
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static class ProjectEditorViewPagerAdapter extends FragmentPagerAdapter {

        // TODO LOW_PRIORITY: 3/10/21 mvvm
        
        OpenBlocksCode code;
        OpenBlocksLayout layout;

        SaveCallback<OpenBlocksCode> code_save;
        SaveCallback<OpenBlocksLayout> layout_save;

        public ProjectEditorViewPagerAdapter(
                FragmentManager fm, OpenBlocksCode code, OpenBlocksLayout layout,
                SaveCallback<OpenBlocksCode> code_save, SaveCallback<OpenBlocksLayout> layout_save) {
            super(fm);
            this.code = code;
            this.layout = layout;
            this.code_save = code_save;
            this.layout_save = layout_save;
        }

        @Override
        public int getCount(){
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int _position) {
            switch (_position) {
                case 0:
                    return "Layout";
                case 1:
                    return "Code";
                case 2:
                    return "Components";
                default:
                    return null;
            }
        }

        @NonNull
        @Override
        public Fragment getItem(int _position) {
            switch (_position) {
                case 0:
                    return new LayoutEditFragment(layout, code, layout_save);
                case 1:
                    return new CodeEditFragment(code, layout, code_save);
                case 2:
                    // Components is not planned yet, might be removed
                    return new ComponentsEditFragment();
                default:
                    return new Fragment();
            }
        }
    }
}