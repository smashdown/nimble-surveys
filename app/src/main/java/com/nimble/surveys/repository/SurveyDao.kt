package com.nimble.surveys.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nimble.surveys.model.Survey
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface SurveyDao {
    @Query("SELECT * FROM surveys ORDER BY created_at ASC")
    fun findAll(): Observable<List<Survey>>

    @Query("SELECT * FROM surveys WHERE id = :id")
    fun findById(id: String): Single<Survey>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(surveyList: List<Survey>)

    @Query("DELETE FROM surveys")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM surveys")
    fun count(): Int
}
