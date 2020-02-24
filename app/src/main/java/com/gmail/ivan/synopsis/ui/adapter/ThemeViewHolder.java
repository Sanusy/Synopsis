package com.gmail.ivan.synopsis.ui.adapter;

import android.view.View;
import android.widget.TextView;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.data.entity.Theme;
import com.gmail.ivan.synopsis.mvp.presenter.ThemeListPresenter;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ThemeViewHolder extends BaseViewHolder<Theme> implements View.OnClickListener {

    @NonNull
    private final TextView themeName;

    @NonNull
    private final TextView thesisCount;

    @Nullable
    private Theme theme;

    @NonNull
    private final ThemeListPresenter presenter;

    public ThemeViewHolder(@NonNull View itemView, @NonNull ThemeListPresenter presenter) {
        super(itemView);

        this.presenter = presenter;
        themeName = itemView.findViewById(R.id.theme_name);
        thesisCount = itemView.findViewById(R.id.thesis_count);
        itemView.setOnClickListener(this);
    }

    @Override
    public void bind(@NonNull Theme entity) {
        theme = entity;
        themeName.setText(entity.getThemeName());

        thesisCount.setText(Integer.toString(theme.getThesisCount()));
    }

    @Override
    public void onClick(View view) {
        presenter.openTheme(Objects.requireNonNull(theme));
    }
}
