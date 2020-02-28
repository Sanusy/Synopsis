package com.gmail.ivan.synopsis.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.database.AppDataBaseSingleton;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisEditContract;
import com.gmail.ivan.synopsis.mvp.presenter.ThesisEditPresenter;
import com.gmail.ivan.synopsis.ui.router.ThesisEditRouter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ThesisEditActivity extends BaseActivity<ThesisEditPresenter>
        implements ThesisEditContract.View {

    private static final String THESIS_ID = "thesis_id";

    private static final String THEME_NAME = "theme_name";

    @Nullable
    private EditText thesisTitle;

    @Nullable
    private EditText thesisDescription;

    @Nullable
    private ProgressBar progressBar;

    @Nullable
    private FloatingActionButton saveEditFab;

    @Nullable
    private Thesis thesis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBar = findViewById(R.id.thesis_edit_progressbar);

        thesisTitle = findViewById(R.id.thesis_edit_title);

        thesisDescription = findViewById(R.id.thesis_edit_description);

        saveEditFab = findViewById(R.id.save_edit_fab);
        saveEditFab.setOnClickListener(view -> {
            saveAndQuit();
        });
    }

    private void saveAndQuit() {
        Objects.requireNonNull(thesis)
               .setThesisName(Objects.requireNonNull(thesisTitle)
                                     .getText()
                                     .toString());

        Objects.requireNonNull(thesis)
               .setThesisDescription(Objects.requireNonNull(thesisDescription)
                                            .getText()
                                            .toString());

        getPresenter().saveThesis(thesis);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        int thesisId = getIntent().getIntExtra(THESIS_ID, 0);
        if (thesisId > 0) {
            getPresenter().loadThesis(thesisId);
        } else {
            getPresenter().loadNewThesis(Objects.requireNonNull(getIntent().getStringExtra(
                    THEME_NAME)));
        }
    }

    @Override
    public void onBackPressed() {
        saveAndQuit();
    }

    @Override
    public void showThesis(@NonNull Thesis thesis) {
        this.thesis = thesis;
        Objects.requireNonNull(thesisTitle)
               .setText(thesis.getThesisName());
        Objects.requireNonNull(thesisDescription)
               .setText(thesis.getThesisDescription());
    }

    @NonNull
    @Override
    protected ThesisEditPresenter createPresenter() {
        ThesisEditRouter router = new ThesisEditRouter(this);
        AppDataBase dataBase = AppDataBaseSingleton.get(this)
                                                   .getDataBase();
        return new ThesisEditPresenter(router, dataBase);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_thesis_edit;
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

    public static Intent newIntent(@NonNull Context packageContext,
                                   int thesisId,
                                   @NonNull String themeName) {
        Intent intent = new Intent(packageContext, ThesisEditActivity.class);
        intent.putExtra(THESIS_ID, thesisId);
        intent.putExtra(THEME_NAME, themeName);

        return intent;
    }
}
