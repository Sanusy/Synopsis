package com.gmail.ivan.synopsis.ui.adapter;

import android.view.ViewGroup;

import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.ui.fragment.BaseFragment;
import com.gmail.ivan.synopsis.ui.fragment.ThesisDetailsFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ThesisPagerAdapter extends BasePagerAdaper<Thesis> {

    @Nullable
    private Fragment currentFragment;

    public ThesisPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    protected BaseFragment createFragment(int position) {
        Thesis thesis = entityList.get(position);

        return ThesisDetailsFragment.newInstance(thesis.getId(), thesis.isNewThesis());
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (currentFragment != object) {
            currentFragment = (Fragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    @Nullable
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
