package com.rabakode.simplenotes

import androidx.lifecycle.LiveData


//abstract class to access multiple data source.
//Deciding whether we get data from network or database
class NoteRepository(private val  noteDao: NoteDao) {

    val allNotes: LiveData<List<ModelNote>> = noteDao.allNotes()

    suspend fun insert(note: ModelNote){
        noteDao.insert(note)
    }

    suspend fun delete(note: ModelNote){
        noteDao.delete(note)
    }

    suspend fun update(note: ModelNote){
        noteDao.update(note)
    }
}