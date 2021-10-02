package com.gmail.ivan.synopsis.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gmail.ivan.synopsis.R;
import com.gmail.ivan.synopsis.data.database.AppDataBaseSingleton;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisDetailsContract;
import com.gmail.ivan.synopsis.mvp.presenter.ThesisDetailsPresenter;
import com.gmail.ivan.synopsis.ui.router.ThesisDetailsRouter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ThesisDetailsFragment extends BaseFragment<ThesisDetailsPresenter>
        implements ThesisDetailsContract.View, SaveEditDialog.SaveEditDialogListener {

    private static final String THESIS_ID = "thesis_id";

    private static final String IS_NEW_THESIS = "is_thesis_new";

    @Nullable
    private EditText thesisTitle;

    @Nullable
    private EditText thesisDescription;

    @Nullable
    private FloatingActionButton editFab;

    @Nullable
    private View layout;

    @Nullable
    private Callbacks callbacks;

    @Nullable
    private Thesis thesis;

    private boolean editMode = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        callbacks = (Callbacks) context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        thesisTitle = view.findViewById(R.id.thesis_title);
        thesisTitle.setOnClickListener(v -> {
            getPresenter().showEdit();
        });

        thesisDescription = view.findViewById(R.id.thesis_details_description);
        thesisDescription.setOnClickListener(v -> {
            getPresenter().showEdit();
        });

        layout = view.findViewById(R.id.thesis_details_layout);
        layout.setOnClickListener(v -> {
            getPresenter().showEdit();
        });

        editFab = view.findViewById(R.id.edit_thesis_fab);
        editFab.setOnClickListener(v -> {
            refreshThesis();

            getPresenter().saveChanges();
        });
    }

    private void refreshThesis() {
        Objects.requireNonNull(thesis)
               .setThesisName(Objects.requireNonNull(thesisTitle)
                                     .getText()
                                     .toString());

        thesis.setThesisDescription(Objects.requireNonNull(thesisDescription)
                                           .getText()
                                           .toString());
    }

    @Override
    public void onStart() {
        super.onStart();

        boolean newThesis = Objects.requireNonNull(getArguments())
                                   .getBoolean(IS_NEW_THESIS);

        getPresenter().loadThesis(Objects.requireNonNull(getArguments())
                                         .getInt(THESIS_ID), newThesis);
    }

    @Override
    public void loadThesis(@NonNull Thesis thesis) {
        this.thesis = thesis;
        Objects.requireNonNull(thesisTitle)
               .setText(thesis.getThesisName());
        Objects.requireNonNull(thesisDescription)
               .setText(thesis.getThesisDescription());
    }

    @Override
    public void showThesisDetails() {
        editMode = false;
        Objects.requireNonNull(editFab)
               .hide();
        Objects.requireNonNull(callbacks)
               .enableSwipe();

        Objects.requireNonNull(thesisTitle)
               .setEnabled(false);
        Objects.requireNonNull(thesisDescription)
               .setEnabled(false);
    }

    @Override
    public void showEditThesis() {
        editMode = true;
        Objects.requireNonNull(editFab)
               .show();
        Objects.requireNonNull(callbacks)
               .disableSwipe();

        Objects.requireNonNull(thesisTitle)
               .setEnabled(true);
        Objects.requireNonNull(thesisDescription)
               .setEnabled(true);
    }

    public void onBackPressed() {
        String actualTitle = Objects.requireNonNull(thesisTitle)
                                    .getText()
                                    .toString();
        String actualDescription = Objects.requireNonNull(thesisDescription)
                                          .getText()
                                          .toString();

        boolean thesisChanged = !(actualTitle.equals(Objects.requireNonNull(thesis)
                                                            .getThesisName())
                && actualDescription.equals(thesis.getThesisDescription()));

        if (editMode && thesisChanged) {
            getPresenter().showConfirmChanges();
        } else {
            getPresenter().back();
        }
    }

    @Override
    public void onDialogPositiveClick(@NonNull BaseDialog dialog) {
        refreshThesis();

        getPresenter().saveChanges();
        getPresenter().back();
    }

    @Override
    public void onDialogNegativeClick(@NonNull BaseDialog dialog) {
        getPresenter().back();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_thesis_details;
    }

    @NonNull
    @Override
    protected ThesisDetailsPresenter createPresenter() {
        ThesisDetailsRouter router = new ThesisDetailsRouter(this);
        return new ThesisDetailsPresenter(router,
                                          AppDataBaseSingleton.get(
                                                  requireContext())
                                                              .getDataBase()
                                                              .thesisRepository());
    }

    public static ThesisDetailsFragment newInstance(int thesisId, boolean newThesis) {
        Bundle args = new Bundle();
        args.putInt(THESIS_ID, thesisId);
        args.putBoolean(IS_NEW_THESIS, newThesis);

        ThesisDetailsFragment fragment = new ThesisDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface Callbacks {

        void disableSwipe();

        void enableSwipe();
    }
}
