package com.gmail.ivan.synopsis.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.data.database.AppDataBaseSingleton;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisPagerContract;
import com.gmail.ivan.synopsis.mvp.presenter.ThesisPagerPresenter;
import com.gmail.ivan.synopsis.ui.adapter.ThesisPagerAdapter;
import com.gmail.ivan.synopsis.ui.adapter.ThesisViewPager;
import com.gmail.ivan.synopsis.ui.fragment.ThesisDetailsFragment;
import com.gmail.ivan.synopsis.ui.router.ThesisPagerRouter;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ThesisPagerActivity extends BaseActivity<ThesisPagerPresenter>
        implements ThesisPagerContract.View, ThesisDetailsFragment.Callbacks {

    private static final String THESIS_ID = "thesis_id";

    private static final String THEME_NAME = "theme_name";

    @Nullable
    private ProgressBar progressBar;

    @Nullable
    private ThesisViewPager viewPager;

    @Nullable
    private ThesisPagerAdapter thesisPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar())
               .setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progressBar = findViewById(R.id.thesis_pager_proressbar);

        viewPager = findViewById(R.id.thesis_viewpager);
        thesisPagerAdapter = new ThesisPagerAdapter(getSupportFragmentManager());
        Objects.requireNonNull(viewPager)
               .setAdapter(thesisPagerAdapter);
        getPresenter().loadThesisList();
    }

    @Override
    public void setThesisList(List<Thesis> thesisList) {
        Objects.requireNonNull(thesisPagerAdapter)
               .setEntityList(thesisList);
        int thesisId = getIntent().getIntExtra(THESIS_ID, 0);

        if (thesisId == 0) {
            Thesis thesis = thesisList.get(thesisList.size() - 1);
            thesis.setNewThesis(true);
            thesisId = thesis.getId();
        }
        for (int i = 0; i < thesisList.size(); i++) {
            if (thesisList.get(i)
                          .getId() == (thesisId)) {
                Objects.requireNonNull(viewPager)
                       .setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    protected ThesisPagerPresenter createPresenter() {
        ThesisPagerRouter router = new ThesisPagerRouter();

        return new ThesisPagerPresenter(router,
                                        AppDataBaseSingleton.get(this)
                                                            .getDataBase()
                                                            .thesisRepository(),
                                        Objects.requireNonNull(getIntent()
                                                                       .getStringExtra(
                                                                               THEME_NAME)));
    }

    @Override
    public void onBackPressed() {
        ThesisDetailsFragment currentFragment =
                (ThesisDetailsFragment) Objects.requireNonNull(thesisPagerAdapter)
                                               .getCurrentFragment();

        Objects.requireNonNull(currentFragment)
               .onBackPressed();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_thesis_pager;
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
        Intent intent = new Intent(packageContext, ThesisPagerActivity.class);
        intent.putExtra(THESIS_ID, thesisId);
        intent.putExtra(THEME_NAME, themeName);

        return intent;
    }

    @Override
    public void disableSwipe() {
        Objects.requireNonNull(viewPager)
               .setSwipeEnabled(false);
    }

    @Override
    public void enableSwipe() {
        Objects.requireNonNull(viewPager)
               .setSwipeEnabled(true);
    }
}
