package com.gmail.ivan.synopsis.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.data.database.AppDataBaseSingleton;
import com.gmail.ivan.synopsis.mvp.contracts.NewThemeDialogContract;
import com.gmail.ivan.synopsis.mvp.presenter.NewThemePresenter;
import com.gmail.ivan.synopsis.ui.activity.ThemeListActivity;
import com.gmail.ivan.synopsis.ui.router.NewThemeRouter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewThemeDialog extends BaseDialog<NewThemePresenter>
        implements NewThemeDialogContract.View {

    private static final String TAG = NewThemeDialog.class.getSimpleName();

    @Nullable
    private NewThemeDialogListener newThemeDialogListener;

    @Nullable
    private TextInputLayout themeTitle;

    @Nullable
    private TextView createButton;

    @Nullable
    private TextView cancelButton;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            newThemeDialogListener = (ThemeListActivity) context;
        } catch (ClassCastException exception) {
            Log.e(TAG, "onAttach: ", exception);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_theme, container, false);

        themeTitle = view.findViewById(R.id.new_theme_title);

        cancelButton = view.findViewById(R.id.new_topic_cancel_button);
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        createButton = view.findViewById(R.id.new_topic_create_button);
        createButton.setOnClickListener(v -> {
            Objects.requireNonNull(getPresenter())
                   .addTheme(Objects.requireNonNull(Objects.requireNonNull(themeTitle)
                                                           .getEditText())
                                    .getText()
                                    .toString());
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow()
                       .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow()
                       .requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @Override
    public void themeAdded() {
        Objects.requireNonNull(newThemeDialogListener)
               .onDialogPositiveClick(this);
        dismiss();
    }

    @Override
    public void emptyTheme() {
        Objects.requireNonNull(themeTitle)
               .setError(getString(R.string.empty_theme_title));
    }

    @Override
    public void alreadyExist() {
        Objects.requireNonNull(themeTitle)
               .setError(getString(R.string.already_such_title));
    }

    @Override
    protected NewThemePresenter createPresenter() {
        NewThemeRouter router = new NewThemeRouter();
        return new NewThemePresenter(router,
                                     AppDataBaseSingleton.get(getContext())
                                                         .getDataBase().themeRepository());
    }

    public interface NewThemeDialogListener {

        void onDialogPositiveClick(@NonNull BaseDialog dialog);
    }

    @NonNull
    public static NewThemeDialog newInstance() {
        return new NewThemeDialog();
    }
}
