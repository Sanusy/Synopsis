package com.gmail.ivan.synopsis.data.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gmail.ivan.synopsis.data.entity.Theme

@Dao
interface ThemeRepository {

    @Insert fun addTheme(theme: Theme)

    @Query("SELECT * FROM theme") fun getAllThemes(): List<Theme>

    @Delete fun deleteTheme(theme: Theme)
}