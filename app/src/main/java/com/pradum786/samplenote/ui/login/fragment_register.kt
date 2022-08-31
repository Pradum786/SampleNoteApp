package com.pradum786.samplenote.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pradum786.samplenote.R
import com.pradum786.samplenote.databinding.FragmentRegisterBinding
import com.pradum786.samplenote.models.UserRequest
import com.pradum786.samplenote.utils.NetworkResult
import com.pradum786.samplenote.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class fragment_register : Fragment() {
    private var _binding: FragmentRegisterBinding? = null;
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager:  TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false);
        if (tokenManager.getToken()!=null){
           findNavController().navigate(R.id.action_fragment_register_to_fragment_main)
        }
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnSignUp.setOnClickListener {
            val validationResult = validateUserInput();
            if (validationResult.first) {
                authViewModel.registerUser(getUserRequest())
            } else {
                showValidationErrors(validationResult.second)
            }
        }
        binding.btnLogin.setOnClickListener{
            findNavController().navigate(R.id.action_fragment_register_to_fragment_login)
        }

        bindObserver()


    }

    private fun showValidationErrors(error: String) {
        binding.txtError.text =
            String.format(resources.getString(R.string.txt_error_message, error))
    }

    private fun validateUserInput(): Pair<Boolean, String> {

        return authViewModel.validateCredentials(
            getUserRequest().email,
            getUserRequest().password,
            getUserRequest().username,
            false
        )
    }

    private fun getUserRequest(): UserRequest {
        return binding.run {
            UserRequest(
                txtEmail.text.toString(),
                txtPassword.text.toString(),
                txtUsername.text.toString()
            )
        }
    }

    private fun bindObserver() {
        authViewModel.userResponseLivedata.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.GONE
            when (it) {
                is NetworkResult.Success -> {

                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_fragment_register_to_fragment_main)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.massage
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}