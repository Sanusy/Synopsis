package com.gmail.ivan.synopsis.ui.router;

import com.gmail.ivan.synopsis.mvp.contracts.ThesisDetailsContract;
import com.gmail.ivan.synopsis.ui.fragment.BaseFragment;
import com.gmail.ivan.synopsis.ui.fragment.SaveEditDialog;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class ThesisDetailsRouter implements ThesisDetailsContract.Router {

    private static final String TAG = ThesisDetailsRouter.class.getSimpleName();

    @NonNull
    private final BaseFragment fragment;

    public ThesisDetailsRouter(@NonNull BaseFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void showConfirmChanges() {
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        SaveEditDialog.newInstance()
                      .show(Objects.requireNonNull(fragmentManager), TAG);
    }

    @Override
    public void back() {
        fragment.requireBaseActivity()
                .finish();
    }
}
