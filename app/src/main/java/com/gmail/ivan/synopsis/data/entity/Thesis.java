package com.gmail.ivan.synopsis.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Thesis {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String themeName;

    @NonNull
    private String thesisName;

    @NonNull
    private String thesisDescription;

    @Ignore
    private boolean newThesis;

    @Ignore
    public Thesis(@NonNull String themeName) {
        this.themeName = themeName;
        thesisName = "New thesis";
        thesisDescription = "";
    }

    public Thesis(@NonNull String themeName,
                  @NonNull String thesisName,
                  @NonNull String thesisDescription) {
        this.themeName = themeName;
        this.thesisName = thesisName;
        this.thesisDescription = thesisDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getThesisName() {
        return thesisName;
    }

    public void setThesisName(@NonNull String thesisName) {
        this.thesisName = thesisName;
    }

    @NonNull
    public String getThesisDescription() {
        return thesisDescription;
    }

    public void setThesisDescription(@NonNull String thesisDescription) {
        this.thesisDescription = thesisDescription;
    }

    @NonNull
    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(@NonNull String themeName) {
        this.themeName = themeName;
    }

    public boolean isNewThesis() {
        return newThesis;
    }

    public void setNewThesis(boolean newThesis) {
        this.newThesis = newThesis;
    }
}
