package com.gmail.ivan.synopsis.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.ivan.synopsis.mvp.contracts.BaseContract;
import com.gmail.ivan.synopsis.mvp.presenter.BasePresenter;
import com.gmail.ivan.synopsis.ui.activity.BaseActivity;

import java.util.Objects;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<P extends BasePresenter>
        extends Fragment
        implements BaseContract.View {

    private static final String TAG = BaseFragment.class.getSimpleName();

    @Nullable
    private P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        getPresenter().attach(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    @NonNull
    protected abstract P createPresenter();

    @NonNull
    public P getPresenter() {
        return Objects.requireNonNull(presenter);
    }

    public BaseActivity requireBaseActivity() {
        return (BaseActivity) requireActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().detach();
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
}
