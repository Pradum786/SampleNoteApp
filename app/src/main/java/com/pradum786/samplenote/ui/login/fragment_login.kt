package com.pradum786.samplenote.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.pradum786.samplenote.R
import com.pradum786.samplenote.databinding.FragmentLoginBinding
import com.pradum786.samplenote.models.UserRequest
import com.pradum786.samplenote.utils.NetworkResult
import com.pradum786.samplenote.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class fragment_login : Fragment() {
    private var _binding: FragmentLoginBinding? = null;
    private val binding get() = _binding!!
    private val authViewModel by activityViewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.btnLogin.setOnClickListener {
            val validationResult = validateUserInput()
            if (validationResult.first){
                authViewModel.loginUser(getUserRequest())
            }else{
                showValidationErrors(validationResult.second)
            }
        }

        observer()
    }

    private fun observer() {

        authViewModel.userResponseLivedata.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_fragment_login_to_fragment_main)
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is NetworkResult.Error -> {
                    showValidationErrors(it.massage.toString())
                }

            }

        })
    }

    private fun getUserRequest(): UserRequest {
        return binding.run {
            UserRequest(
                binding.txtEmail.text.toString(),
                binding.txtPassword.text.toString(),
                "")
        }

    }
    private fun validateUserInput() :Pair<Boolean,String>{
      return  authViewModel.validateCredentials(getUserRequest().email,getUserRequest().password,getUserRequest().username,true)
    }

    private fun showValidationErrors(error: String) {
        binding.txtError.text =
            String.format(resources.getString(R.string.txt_error_message, error))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}