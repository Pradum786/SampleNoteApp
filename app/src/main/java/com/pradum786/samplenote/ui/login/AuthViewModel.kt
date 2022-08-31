package com.pradum786.samplenote.ui.login

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import com.pradum786.samplenote.models.UserRequest
import com.pradum786.samplenote.repository.UserRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pradum786.samplenote.models.UserResponse
import com.pradum786.samplenote.utils.NetworkResult
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userResponseLivedata :LiveData<NetworkResult<UserResponse>> get()= userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }
    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }
    fun validateCredentials(email: String, password: String, username: String, isLogin:Boolean):Pair<Boolean,String>{
        var result = Pair(true, "")
        if(TextUtils.isEmpty(email) || (!isLogin && TextUtils.isEmpty(username)) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please provide the credentials")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false, "Email is invalid")
        }
        else if(!TextUtils.isEmpty(password) && password.length <= 5){
            result = Pair(false, "Password length should be greater than 5")
        }
        return result

    }

}