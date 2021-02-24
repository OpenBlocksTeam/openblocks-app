package com.openblocks.android.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.openblocks.android.R;
import com.openblocks.android.modman.models.Module;
import com.openblocks.moduleinterface.OpenBlocksModule;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.tab_text_projects, R.string.tab_text_modules};
    private final Context mContext;

    private final HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules;

    public SectionsPagerAdapter(Context context, FragmentManager fm, HashMap<OpenBlocksModule.Type, ArrayList<Module>> modules) {
        super(fm);
        mContext = context;
        this.modules = modules;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (position) {
            case 0:
                // Projects
                return ProjectsFragment.newInstance();
            case 1:
                // Modules
                return ModulesFragment.newInstance(modules);
            default:
                // Unknown, return an empty fragment instead
                return new Fragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}