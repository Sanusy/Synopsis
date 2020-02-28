package com.gmail.ivan.synopsis.mvp.contracts;

import com.gmail.ivan.synopsis.data.entity.Thesis;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ThesisListContract {

    interface View extends BaseContract.View {

        void showThesisList(@Nullable List<Thesis> thesisList);

        void showEmptyThesisList();

        void showUndoDelete();
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void loadThesisList();

        void newThesis();

        void openThesis(@NonNull Thesis thesis);

        void deleteThesis(@NonNull Thesis thesis);

        void addRecentlyDeleted();
    }

    interface Router extends BaseContract.Router {

        void openThesis(@NonNull Thesis thesis);
    }
}
