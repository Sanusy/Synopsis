package com.gmail.ivan.synopsis.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.database.AppDataBaseSingleton;
import com.gmail.ivan.synopsis.data.entity.Theme;
import com.gmail.ivan.synopsis.mvp.contracts.ThemeListContract;
import com.gmail.ivan.synopsis.mvp.presenter.ThemeListPresenter;
import com.gmail.ivan.synopsis.ui.adapter.SwipeToDeleteCallback;
import com.gmail.ivan.synopsis.ui.adapter.ThemeRecyclerAdapter;
import com.gmail.ivan.synopsis.ui.fragment.BaseDialog;
import com.gmail.ivan.synopsis.ui.fragment.NewThemeDialog;
import com.gmail.ivan.synopsis.ui.router.ThemeListRouter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ThemeListActivity extends BaseActivity<ThemeListPresenter> implements
                                                                        ThemeListContract.View,
                                                                        NewThemeDialog.NewThemeDialogListener {

    @Nullable
    private ProgressBar progressBar;

    @Nullable
    private TextView emptyListText;

    @Nullable
    private RecyclerView recyclerView;

    @Nullable
    private ThemeRecyclerAdapter recyclerAdapter;

    @Nullable
    private FloatingActionButton fab;

    @NonNull
    @Override
    protected ThemeListPresenter createPresenter() {
        ThemeListRouter router = new ThemeListRouter(this);
        AppDataBase dataBase = AppDataBaseSingleton.get(this)
                                                   .getDataBase();
        return new ThemeListPresenter(router,
                                      dataBase.themeRepository(), dataBase.thesisRepository());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar = findViewById(R.id.theme_progress_bar);

        emptyListText = findViewById(R.id.empty_theme_list_text);

        recyclerView = findViewById(R.id.theme_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new ThemeRecyclerAdapter(getPresenter());
        recyclerView.setAdapter(recyclerAdapter);
        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new SwipeToDeleteCallback(recyclerAdapter,
                                                              this,
                                                              new ColorDrawable(
                                                                      ContextCompat.getColor(this,
                                                                                             R.color.secondaryColor))));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        fab = findViewById(R.id.theme_list_fab_add);
        fab.setOnClickListener(v -> {
            getPresenter().newTheme();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().loadThemeList();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_theme_list;
    }

    @Override
    public String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public void showThemeList(@Nullable List<Theme> themeList) {
        Objects.requireNonNull(recyclerAdapter)
               .setEntityListData(themeList);
        Objects.requireNonNull(emptyListText)
               .setVisibility(View.GONE);
    }

    @Override
    public void showEmptyList() {
        Objects.requireNonNull(emptyListText)
               .setVisibility(View.VISIBLE);
        Objects.requireNonNull(recyclerAdapter)
               .setEntityListData(Collections.EMPTY_LIST);
    }

    @Override
    public void showUndoDelete() {
        Snackbar.make(Objects.requireNonNull(fab), R.string.undo_delete_text, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_text, v -> {
                    getPresenter().addRecentlyDeleted();
                })
                .show();
    }

    @Override
    public void showProgress() {
        Objects.requireNonNull(progressBar)
               .setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        Objects.requireNonNull(progressBar)
               .setVisibility(View.GONE);
    }

    @Override
    public void onDialogPositiveClick(@NonNull BaseDialog dialog) {
        getPresenter().loadThemeList();
    }
}
