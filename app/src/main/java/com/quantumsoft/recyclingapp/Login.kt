package com.quantumsoft.recyclingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import com.quantumsoft.recyclingapp.databinding.FragmentLoginBinding
import android.widget.Toast

class Login : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //binding.buttonLogin.setOnClickListener {
         //   loginViewModel.performLogin()
       // }

        loginViewModel.loginSuccess.observe(viewLifecycleOwner, Observer { loginSuccess ->
            if (!loginSuccess) {
                val errorMessage = "Usuario o contraseña incorrectos"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
        loginViewModel.passwordValid.observe(viewLifecycleOwner, Observer { isValid ->
            if (!isValid) {
                val errorMessage = "La contraseña debe tener al menos 8 caracteres , una Mayuscula y un Numero"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        loginViewModel.password.observe(viewLifecycleOwner, Observer { password ->
            loginViewModel.validatePassword(password)
        })

        return binding.root
    }
}
