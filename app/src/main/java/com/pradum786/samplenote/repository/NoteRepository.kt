package com.pradum786.samplenote.repository

import androidx.lifecycle.MutableLiveData
import com.pradum786.samplenote.api.NoteAPI
import com.pradum786.samplenote.models.NoteRequest
import com.pradum786.samplenote.models.NoteResponse
import com.pradum786.samplenote.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteAPI: NoteAPI) {
    private val _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData get() = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<Pair<Boolean, String>>>()
    val statusLiveData get() = _statusLiveData

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteAPI.createNote(noteRequest)
        handleResponse(response, "Note Created")
    }

    suspend fun getNotes(){
        _notesLiveData.postValue(NetworkResult.Loading())
       val response= noteAPI.getNotes()
        if (response.isSuccessful && response.body() != null){
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))

        }else if (response.errorBody() != null){
            val error= JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(error.getString("message")))
        }else{
            _notesLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun updateNote(id: String, noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteAPI.updateNote(id, noteRequest)
        handleResponse(response,"Note Updated")
    }

    suspend fun deleteNote(noteId:String){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteAPI.deleteNote(noteId)
        handleResponse(response, "Note Deleted" )
    }

    private fun handleResponse(response: Response<NoteResponse>, msg: String) {

        if (response.isSuccessful && response.body() != null){
            _statusLiveData.postValue(NetworkResult.Success(Pair(true,msg)))
        }else{
            _statusLiveData.postValue(NetworkResult.Success(Pair(false,"Something Went Wrong")))
        }

    }


}