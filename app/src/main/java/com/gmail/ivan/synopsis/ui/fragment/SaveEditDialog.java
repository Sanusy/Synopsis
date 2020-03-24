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
import com.gmail.ivan.synopsis.mvp.contracts.SaveEditContract;
import com.gmail.ivan.synopsis.mvp.presenter.SaveEditPresenter;
import com.gmail.ivan.synopsis.ui.router.SaveEditRouter;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SaveEditDialog extends BaseDialog<SaveEditPresenter> implements SaveEditContract.View {

    @Nullable
    private SaveEditDialogListener saveEditDialogListener;

    @Nullable
    private TextView saveButton;

    @Nullable
    private TextView discardButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saveEditDialogListener = (SaveEditDialogListener) getParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_save_edit, container, false);

        discardButton = view.findViewById(R.id.discard_edit_button);
        discardButton.setOnClickListener(v -> {
            Objects.requireNonNull(getPresenter())
                   .discardChanges();
        });

        saveButton = view.findViewById(R.id.save_edit_button);
        saveButton.setOnClickListener(v -> {
            Objects.requireNonNull(getPresenter())
                   .saveChanges();
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
    public void saveChanges() {
        Objects.requireNonNull(saveEditDialogListener)
               .onDialogPositiveClick(this);
    }

    @Override
    public void discardChanges() {
        Objects.requireNonNull(saveEditDialogListener)
               .onDialogNegativeClick(this);
    }

    @Override
    protected SaveEditPresenter createPresenter() {
        SaveEditRouter router = new SaveEditRouter();
        return new SaveEditPresenter(router);
    }

    public static SaveEditDialog newInstance() {
        return new SaveEditDialog();
    }

    public interface SaveEditDialogListener {

        void onDialogPositiveClick(@NonNull BaseDialog dialog);

        void onDialogNegativeClick(@NonNull BaseDialog dialog);
    }
}
