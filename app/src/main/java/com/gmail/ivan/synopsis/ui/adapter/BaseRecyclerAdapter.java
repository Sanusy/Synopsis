package com.gmail.ivan.synopsis.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerAdapter<E> extends RecyclerView.Adapter<BaseViewHolder<E>> {

    @NonNull
    private final List<E> entityList = new ArrayList<>();

    public void setEntityListData(List<E> entityList) {
        this.entityList.clear();
        this.entityList.addAll(entityList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder<E> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(getLayoutID(), parent, false);

        return createHolder(view);
    }

    @LayoutRes
    protected abstract int getLayoutID();

    @NonNull
    protected abstract BaseViewHolder<E> createHolder(@NonNull View view);

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<E> holder, int position) {
        holder.bind(entityList.get(position));
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }

    public E getItem(int position) {
        return entityList.get(position);
    }

    public abstract void deleteItem(int position);
}
