package com.gmail.ivan.synopsis.ui.fragment;

import android.os.Bundle;

import com.gmail.ivan.synopsis.mvp.contracts.BaseContract;
import com.gmail.ivan.synopsis.mvp.presenter.BasePresenter;
import com.gmail.ivan.synopsis.ui.activity.BaseActivity;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public abstract class BaseDialog<P extends BasePresenter> extends DialogFragment
        implements BaseContract.View {

    @Nullable
    private P presenter;

    @Nullable
    public P getPresenter() {
        return presenter;
    }

    protected abstract P createPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(presenter)
               .attach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        Objects.requireNonNull(presenter)
               .detach();
    }

    @Override
    public void onError(@Nullable Throwable error) {
        requireBaseActivity().onError(error);
    }

    @Override
    public void showProgress() {
        requireBaseActivity().showProgress();
    }

    @Override
    public void hideProgress() {
        requireBaseActivity().hideProgress();
    }

    @NonNull
    public BaseActivity requireBaseActivity() {
        return (BaseActivity) requireActivity();
    }
}
