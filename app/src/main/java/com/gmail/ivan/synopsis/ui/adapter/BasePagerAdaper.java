package com.gmail.ivan.synopsis.ui.adapter;

import com.gmail.ivan.synopsis.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public abstract class BasePagerAdaper<E> extends FragmentStatePagerAdapter {

    @NonNull
    protected List<E> entityList;

    public BasePagerAdaper(@NonNull FragmentManager fm) {
        super(fm);

        entityList = new ArrayList<>();
    }

    public void setEntityList(@NonNull List<E> entityList) {
        this.entityList = entityList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseFragment getItem(int position) {
        return createFragment(position);
    }

    protected abstract BaseFragment createFragment(int position);

    @Override
    public int getCount() {
        return entityList.size();
    }
}
