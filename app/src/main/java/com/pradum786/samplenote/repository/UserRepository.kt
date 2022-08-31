package com.pradum786.samplenote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pradum786.samplenote.api.UserAPI
import com.pradum786.samplenote.models.UserRequest
import com.pradum786.samplenote.models.UserResponse
import com.pradum786.samplenote.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseLivedata = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>> get() = _userResponseLivedata


    suspend fun registerUser(userRequest: UserRequest) {
        val response = userAPI.signup(userRequest)
        handleResponse(response)

    }

    suspend fun loginUser(userRequest: UserRequest) {
        val response = userAPI.signin(userRequest)
        handleResponse(response)

    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLivedata.postValue(NetworkResult.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            val error = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLivedata.postValue(NetworkResult.Error(error.getString("message")))
        } else {
            _userResponseLivedata.postValue(NetworkResult.Error("SomeThing Went Wrong"))
        }
    }




}