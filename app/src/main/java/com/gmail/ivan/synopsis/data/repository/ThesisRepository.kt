package com.gmail.ivan.synopsis.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.gmail.ivan.synopsis.data.entity.Thesis

@Dao
interface ThesisRepository {

    @Insert fun addThesis(thesis: Thesis)

    @Query("SELECT * FROM thesis Where id = :thesisId") fun getThesis(thesisId: Int): Thesis

    @Update fun updateThesis(thesis: Thesis)

    @Delete fun deleteThesis(thesis: Thesis)

    @Query("SELECT * FROM thesis WHERE themeName = :themeName")
    fun getThesisList(themeName: String): List<Thesis>
}