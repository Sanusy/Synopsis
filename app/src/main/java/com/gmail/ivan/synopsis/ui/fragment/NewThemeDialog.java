package com.gmail.ivan.synopsis.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.data.database.AppDataBaseSingleton;
import com.gmail.ivan.synopsis.mvp.contracts.NewThemeDialogContract;
import com.gmail.ivan.synopsis.mvp.presenter.NewThemePresenter;
import com.gmail.ivan.synopsis.ui.activity.ThemeListActivity;
import com.gmail.ivan.synopsis.ui.router.NewThemeRouter;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewThemeDialog extends BaseDialog<NewThemePresenter> implements NewThemeDialogContract.View {

    private static final String TAG = NewThemeDialog.class.getSimpleName();

    @Nullable
    private NewThemeDialogListener newThemeDialogListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            newThemeDialogListener = (ThemeListActivity) context;
        }catch (ClassCastException exception){
            Log.e(TAG, "onAttach: ", exception);
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        EditText themeTitle = new EditText(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_theme_title)
                .setView(themeTitle)
                .setPositiveButton(android.R.string.ok, (dialog, width) -> {
                    Objects.requireNonNull(getPresenter())
                           .addTheme(themeTitle.getText().toString());
                    Objects.requireNonNull(newThemeDialogListener)
                           .onDialogPositiveClick(this);
                });

        return builder.create();
    }

    @Override
    public void emptyTheme() {
        Toast.makeText(getContext(), R.string.empty_theme_title, Toast.LENGTH_LONG).show();
    }

    @Override
    protected NewThemePresenter createPresenter() {
        NewThemeRouter router = new NewThemeRouter();
        return new NewThemePresenter(router, AppDataBaseSingleton.get(getContext()).getDataBase());
    }

    public interface NewThemeDialogListener {

        void onDialogPositiveClick(@NonNull BaseDialog dialog);
    }

    @NonNull
    public static NewThemeDialog newInstance() {
        return new NewThemeDialog();
    }
}
