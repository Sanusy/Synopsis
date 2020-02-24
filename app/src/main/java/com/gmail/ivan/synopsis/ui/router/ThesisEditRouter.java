package com.gmail.ivan.synopsis.ui.router;

import com.gmail.ivan.synopsis.mvp.contracts.ThesisEditContract;
import com.gmail.ivan.synopsis.ui.activity.BaseActivity;

import androidx.annotation.NonNull;

public class ThesisEditRouter implements ThesisEditContract.Router {

    @NonNull
    private final BaseActivity activity;

    public ThesisEditRouter(@NonNull BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void back() {
        //none
    }
}
