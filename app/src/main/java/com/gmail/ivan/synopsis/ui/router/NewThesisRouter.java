package com.gmail.ivan.synopsis.ui.router;

import android.content.Intent;

import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.NewThesisContract;
import com.gmail.ivan.synopsis.ui.activity.BaseActivity;
import com.gmail.ivan.synopsis.ui.activity.ThesisPagerActivity;

import androidx.annotation.NonNull;

public class NewThesisRouter implements NewThesisContract.Router {

    @NonNull
    private final BaseActivity activity;

    public NewThesisRouter(@NonNull BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void openNewThesis(@NonNull Thesis thesis) {
        Intent intent =
                ThesisPagerActivity.newIntent(activity, thesis.getId(), thesis.getThemeName());

        activity.startActivity(intent);
    }

    @Override
    public void back() {
        //none
    }
}
