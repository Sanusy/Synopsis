package com.gmail.ivan.synopsis.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.data.database.AppDataBaseSingleton;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisListContract;
import com.gmail.ivan.synopsis.mvp.presenter.ThesisListPresenter;
import com.gmail.ivan.synopsis.ui.adapter.SwipeToDeleteCallback;
import com.gmail.ivan.synopsis.ui.adapter.ThesisRecyclerAdapter;
import com.gmail.ivan.synopsis.ui.router.ThesisListRouter;
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

public class ThesisListActivity extends BaseActivity<ThesisListPresenter>
        implements ThesisListContract.View {

    private static final String THEME_NAME = "theme_name";

    private static final String TAG = ThesisListActivity.class.getSimpleName();

    @Nullable
    private ProgressBar progressBar;

    @Nullable
    private TextView emptyListText;

    @Nullable
    private RecyclerView recyclerView;

    @Nullable
    private ThesisRecyclerAdapter recyclerAdapter;

    @Nullable
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar = findViewById(R.id.thesis_progress_bar);
        emptyListText = findViewById(R.id.empty_thesislist_text);

        recyclerView = findViewById(R.id.thesis_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter = new ThesisRecyclerAdapter(getPresenter());
        recyclerView.setAdapter(recyclerAdapter);

        ItemTouchHelper itemTouchHelper =
                new ItemTouchHelper(new SwipeToDeleteCallback(recyclerAdapter, this,
                                                              Objects.requireNonNull(ContextCompat.getDrawable(
                                                                      this,
                                                                      R.drawable.round_cornered_delete_background))));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        fab = findViewById(R.id.thesis_list_fab_add);
        fab.setOnClickListener(v -> {
            getPresenter().newThesis();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().loadThesisList();
    }

    @Override
    public void showThesisList(@Nullable List<Thesis> thesisList) {
        Objects.requireNonNull(recyclerAdapter)
               .setEntityListData(thesisList);
        Objects.requireNonNull(emptyListText)
               .setVisibility(View.GONE);
    }

    @Override
    public void showEmptyThesisList() {
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

    @NonNull
    @Override
    protected ThesisListPresenter createPresenter() {
        ThesisListRouter router = new ThesisListRouter(this);
        return new ThesisListPresenter(router,
                                       AppDataBaseSingleton.get(this)
                                                           .getDataBase().thesisRepository(),
                                       (Objects.requireNonNull(getIntent().getStringExtra(THEME_NAME))));
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_thesis_list;
    }

    @Override
    public String getToolbarTitle() {
        return getIntent().getStringExtra(THEME_NAME);
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

    public static Intent newIntent(@NonNull Context packageContext, @NonNull String themeTitle) {
        Intent intent = new Intent(packageContext, ThesisListActivity.class);
        intent.putExtra(THEME_NAME, themeTitle);
        return intent;
    }
}
