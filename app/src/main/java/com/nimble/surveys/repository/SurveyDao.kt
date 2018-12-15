package com.nimble.surveys.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nimble.surveys.model.Survey
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface SurveyDao {
    @Query("SELECT * FROM surveys")
    fun findAll(): Flowable<List<Survey>>

    @Query("SELECT * FROM surveys WHERE id = :id")
    fun findById(id: String): Single<Survey>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(surveyList: List<Survey>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(vararg survey: Survey)

    @Query("DELETE FROM surveys")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM surveys")
    fun count(): Integer
}
