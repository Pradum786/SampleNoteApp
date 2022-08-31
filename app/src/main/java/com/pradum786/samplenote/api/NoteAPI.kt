package com.pradum786.samplenote.api

import com.pradum786.samplenote.models.NoteRequest
import com.pradum786.samplenote.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NoteAPI {

    @GET("/api/notes")
    suspend fun getNotes():Response<List<NoteResponse>>

    @POST("/api/notes")
    suspend fun createNote(@Body noteRequest: NoteRequest):Response<NoteResponse>

    @DELETE("/api/notes/{id}")
    suspend fun deleteNote(@Path ("id") noteID:String) : Response<NoteResponse>

    @PUT("api/notes/{id}")
    suspend fun updateNote(@Path("id") noteId: String, @Body noteRequest: NoteRequest): Response<NoteResponse>
}