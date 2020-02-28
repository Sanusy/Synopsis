package com.gmail.ivan.synopsis.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        implements ThesisDetailsContract.View {

    private static final String THESIS_ID = "thesis_id";

    private static final String IS_NEW_THESIS = "is_thesis_new";

    @Nullable
    private TextView thesisTitle;

    @Nullable
    private TextView thesisDescription;

    @Nullable
    private FloatingActionButton editFab;

    @Nullable
    private Thesis thesis;

    private boolean newThesis;

    private boolean onCreateCalled;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newThesis = Objects.requireNonNull(getArguments())
                           .getBoolean(IS_NEW_THESIS);
        onCreateCalled = true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        thesisTitle = view.findViewById(R.id.thesis_title);

        thesisDescription = view.findViewById(R.id.thesis_details_description);

        editFab = view.findViewById(R.id.edit_thesis_fab);
        editFab.setOnClickListener(v -> {
            getPresenter().showEditThesis(Objects.requireNonNull(thesis));
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getPresenter().loadThesis(Objects.requireNonNull(getArguments())
                                         .getInt(THESIS_ID));

        if (newThesis && onCreateCalled) {
            onCreateCalled = false;
            getPresenter().showEditThesis(Objects.requireNonNull(thesis));
        }
    }

    @Override
    public void showThesis(@NonNull Thesis thesis) {
        this.thesis = thesis;
        Objects.requireNonNull(thesisTitle)
               .setText(thesis.getThesisName());
        Objects.requireNonNull(thesisDescription)
               .setText(thesis.getThesisDescription());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_thesis_details;
    }

    @NonNull
    @Override
    protected ThesisDetailsPresenter createPresenter() {
        ThesisDetailsRouter router = new ThesisDetailsRouter(requireBaseActivity());
        ThesisDetailsPresenter presenter = new ThesisDetailsPresenter(router,
                                                                      AppDataBaseSingleton.get(
                                                                              requireContext())
                                                                                          .getDataBase());
        return presenter;
    }

    public static ThesisDetailsFragment newInstance(int thesisId, boolean newThesis) {

        Bundle args = new Bundle();
        args.putInt(THESIS_ID, thesisId);
        args.putBoolean(IS_NEW_THESIS, newThesis);

        ThesisDetailsFragment fragment = new ThesisDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
