package com.gmail.ivan.synopsis.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.gmail.ivan.synopsis.mvp.contracts.BaseContract;
import com.gmail.ivan.synopsis.mvp.presenter.BasePresenter;

import java.util.Objects;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<P extends BasePresenter>
        extends AppCompatActivity
        implements BaseContract.View {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @Nullable
    private P presenter;

    @NonNull
    protected abstract P createPresenter();

    @NonNull
    public P getPresenter() {
        return Objects.requireNonNull(presenter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        setTitle(getToolbarTitle());
        presenter = createPresenter();
        getPresenter().attach(this);
    }

    @LayoutRes
    public abstract int getLayoutRes();

    public abstract String getToolbarTitle();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().detach();
    }

    @Override
    public void onError(@Nullable Throwable error) {
        Log.e(TAG, "onError: ", error);
    }
}
