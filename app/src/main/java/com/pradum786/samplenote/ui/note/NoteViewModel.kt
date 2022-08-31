package com.pradum786.samplenote.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pradum786.samplenote.models.NoteRequest
import com.pradum786.samplenote.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    val noteLiveData get() = noteRepository.notesLiveData
    val statusLiveData get() = noteRepository.statusLiveData

     fun getnotes(){
         viewModelScope.launch {
             noteRepository.getNotes()
         }
    }

    fun createNote(noteRequest : NoteRequest){
        viewModelScope.launch {
        noteRepository.createNote(noteRequest)
    }}

    fun updateNote(id : String , noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.updateNote(id, noteRequest)
        }
    }

    fun deleteNote(id: String){
        viewModelScope.launch {
            noteRepository.deleteNote(id)
        }
    }

}