package com.openblocks.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.openblocks.android.fragments.main.ModulesFragment;
import com.openblocks.android.fragments.main.ProjectsFragment;
import com.openblocks.android.modman.ModuleLoader;
import com.openblocks.android.modman.ModuleManager;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectEditorActivity extends AppCompatActivity {

    ModuleManager moduleManager = ModuleManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_editor);

        Module project_manager = moduleManager.getActiveModule(OpenBlocksModule.Type.PROJECT_MANAGER);
        OpenBlocksModule.ProjectManager project_manager_instance = ModuleLoader.load(this, project_manager, OpenBlocksModule.ProjectManager.class);

    }

    public static class FragmentAdapter extends FragmentStatePagerAdapter {
        Context context;
        int tabCount;

        HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules;

        public FragmentAdapter(Context context, FragmentManager fm, int tabCount, HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules) {
            super(fm);
            this.context = context;
            this.tabCount = tabCount;
            this.modules = modules;
        }

        @Override
        public int getCount(){
            return tabCount;
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
                    return new ProjectsFragment();
                case 1:
                    return ModulesFragment.newInstance(modules);
                default:
                    return new Fragment();
            }
        }
    }
}