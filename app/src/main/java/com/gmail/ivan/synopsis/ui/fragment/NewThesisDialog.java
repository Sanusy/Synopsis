package com.gmail.ivan.synopsis.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.database.AppDataBaseSingleton;
import com.gmail.ivan.synopsis.mvp.contracts.NewThesisContract;
import com.gmail.ivan.synopsis.mvp.presenter.NewThesisPresenter;
import com.gmail.ivan.synopsis.ui.router.NewThesisRouter;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewThesisDialog extends BaseDialog<NewThesisPresenter>
        implements NewThesisContract.View {

    private static final String THEME_NAME = "theme_name";

    @Nullable
    private TextInputLayout thesisTitle;

    @Nullable
    private TextView createButton;

    @Nullable
    private TextView cancelButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_new_thesis, container, false);

        thesisTitle = view.findViewById(R.id.new_thesis_title);

        cancelButton = view.findViewById(R.id.new_thesis_cancel_button);
        cancelButton.setOnClickListener(v -> {
            dismiss();
        });

        createButton = view.findViewById(R.id.new_thesis_create_button);
        createButton.setOnClickListener(v -> {
            Objects.requireNonNull(getPresenter())
                   .addThesis(Objects.requireNonNull(Objects.requireNonNull(thesisTitle)
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
    public void thesisAdded() {
        dismiss();
    }

    @Override
    public void emptyTitle() {
        Objects.requireNonNull(thesisTitle)
               .setError("Empty field!");
    }

    @Override
    protected NewThesisPresenter createPresenter() {
        NewThesisRouter router = new NewThesisRouter(requireBaseActivity());
        AppDataBase dataBase = AppDataBaseSingleton.get(
                requireContext())
                                                   .getDataBase();
        return new NewThesisPresenter(router,
                                      dataBase.thesisRepository(),
                                      Objects.requireNonNull(Objects.requireNonNull(
                                                                      getArguments())
                                                                                            .getString(
                                                                                                    THEME_NAME)));
    }

    public static NewThesisDialog newInstance(@NonNull String themeName) {
        Bundle args = new Bundle();
        args.putString(THEME_NAME, themeName);

        NewThesisDialog fragment = new NewThesisDialog();

        fragment.setArguments(args);
        return fragment;
    }
}
