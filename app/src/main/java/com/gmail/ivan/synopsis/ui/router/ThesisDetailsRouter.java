package com.gmail.ivan.synopsis.ui.router;

import android.content.Intent;

import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisDetailsContract;
import com.gmail.ivan.synopsis.ui.activity.BaseActivity;
import com.gmail.ivan.synopsis.ui.activity.ThesisEditActivity;

import androidx.annotation.NonNull;

public class ThesisDetailsRouter implements ThesisDetailsContract.Router {

    @NonNull
    private final BaseActivity activity;

    public ThesisDetailsRouter(@NonNull BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void showEditThesis(@NonNull Thesis thesis) {
        Intent intent =
                ThesisEditActivity.newIntent(activity, thesis.getId(), thesis.getThemeName());

        activity.startActivity(intent);
    }

    @Override
    public void back() {
        activity.finish();
    }
}
