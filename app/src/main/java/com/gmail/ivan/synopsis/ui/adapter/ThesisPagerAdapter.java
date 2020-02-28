package com.gmail.ivan.synopsis.ui.adapter;

import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.ui.fragment.BaseFragment;
import com.gmail.ivan.synopsis.ui.fragment.ThesisDetailsFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class ThesisPagerAdapter extends BasePagerAdaper<Thesis> {

    public ThesisPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    protected BaseFragment createFragment(int position) {
        Thesis thesis = entityList.get(position);

        return ThesisDetailsFragment.newInstance(thesis.getId(), thesis.isNewThesis());
    }
}
