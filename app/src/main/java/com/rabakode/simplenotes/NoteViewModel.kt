package com.rabakode.simplenotes

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {

    val repository: NoteRepository
    val allNotes: LiveData<List<ModelNote>>

    init {
        val dao = NoteDB.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }

    fun updateNote(note: ModelNote) = viewModelScope.launch {
        repository.update(note)
    }

    fun deleteNote(note: ModelNote) = viewModelScope.launch {
        repository.delete(note)
    }

    fun insertNote(note: ModelNote) = viewModelScope.launch {
        repository.insert(note)
    }
}