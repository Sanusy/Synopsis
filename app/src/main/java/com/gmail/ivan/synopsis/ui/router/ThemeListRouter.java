package com.gmail.ivan.synopsis.ui.router;

import android.content.Intent;

import com.gmail.ivan.synopsis.data.entity.Theme;
import com.gmail.ivan.synopsis.mvp.contracts.ThemeListContract;
import com.gmail.ivan.synopsis.ui.activity.BaseActivity;
import com.gmail.ivan.synopsis.ui.activity.ThesisListActivity;
import com.gmail.ivan.synopsis.ui.fragment.NewThemeDialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class ThemeListRouter implements ThemeListContract.Router {

    private static final String TAG = ThemeListRouter.class.getSimpleName();

    @NonNull
    private final BaseActivity activity;

    public ThemeListRouter(@NonNull BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void openTheme(@NonNull Theme theme) {
        Intent intent = ThesisListActivity.newIntent(activity, theme.getThemeName());
        activity.startActivity(intent);
    }

    @Override
    public void openNewTheme() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        NewThemeDialog.newInstance()
                      .show(fragmentManager, TAG);
    }

    @Override
    public void back() {
        //none
    }
}
