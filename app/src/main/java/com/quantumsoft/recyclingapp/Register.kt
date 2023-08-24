package com.quantumsoft.recyclingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.databinding.DataBindingUtil
import com.quantumsoft.recyclingapp.databinding.FragmentRegisterBinding
import android.widget.Toast

class Register : Fragment() {
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        binding.viewModel = registerViewModel
        binding.lifecycleOwner = viewLifecycleOwner



        binding.buttonLogin.setOnClickListener {
            registerViewModel.performLogin()
        }

        registerViewModel.usernameValid.observe(viewLifecycleOwner) { isvalid ->
            if (!isvalid) {
                val errorMessage = "Username ya existe"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.registerSuccess.observe(viewLifecycleOwner) {
            if (!it) {
                val errorMessage = "Usuario no creado incorrectos"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }else{
                val errorMessage = "Usuario creado satisfactoriamente"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        registerViewModel.passwordValid.observe(viewLifecycleOwner) { isValid ->
            if (!isValid) {
                val errorMessage =
                    "La contraseña debe tener al menos 8 caracteres , una Mayuscula y un Numero"
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        registerViewModel.password.observe(viewLifecycleOwner) { password ->
            registerViewModel.validatePassword(password)
        }


        return binding.root
    }
}