package com.pradum786.samplenote.api

import com.pradum786.samplenote.models.UserRequest
import com.pradum786.samplenote.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {

    @POST("/api/users/singup")
   suspend fun signup(@Body userRequest: UserRequest) :Response<UserResponse>

    @POST("/api/users/singin")
   suspend fun signin(@Body userRequest: UserRequest) :Response<UserResponse>


}