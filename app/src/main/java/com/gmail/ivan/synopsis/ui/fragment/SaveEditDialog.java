package com.gmail.ivan.synopsis.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.mvp.contracts.SaveEditContract;
import com.gmail.ivan.synopsis.mvp.presenter.SaveEditPresenter;
import com.gmail.ivan.synopsis.ui.router.SaveEditRouter;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class SaveEditDialog extends BaseDialog<SaveEditPresenter> implements SaveEditContract.View {

    @Nullable
    private SaveEditDialogListener saveEditDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        saveEditDialogListener = (ThesisDetailsFragment) getParentFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.save_edit_title)
                .setPositiveButton(R.string.save_changes, (dialog, width) -> {
                    Objects.requireNonNull(getPresenter())
                           .saveChanges();
                })
                .setNegativeButton(R.string.discard_changes, (dialog, width) -> {
                    Objects.requireNonNull(getPresenter())
                           .discardChanges();
                })
                .setNeutralButton(android.R.string.cancel, (dialog, width) -> {
                    //none
                });

        return builder.create();
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
