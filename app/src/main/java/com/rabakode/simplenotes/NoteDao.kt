package com.rabakode.simplenotes

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun allNotes(): LiveData<List<ModelNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(modelNote: ModelNote?)

    @Delete
    suspend fun delete(modelNote: ModelNote?)

    @Update
    suspend fun update(modelNote: ModelNote?)
}