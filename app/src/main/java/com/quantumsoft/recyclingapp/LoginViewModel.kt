package com.quantumsoft.recyclingapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {
    
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess
    
    fun setEmail(newEmail: String) {
        _email.value = newEmail
    }
    fun setPassword(newPassword: String) {
        _password.value = newPassword
    }

    //Firebase
    private val auth = FirebaseAuth.getInstance()
    fun performLogin() {
        val email = _email.value
        val password = _password.value

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Inicio de sesión exitoso
                        _loginSuccess.value = true
                    } else {
                        // Inicio de sesión fallido
                        _loginSuccess.value = false
                    }
                }
        } else {
            // Manejo de campos vacíos
            _loginSuccess.value = false
        }

    }

    private val _passwordValid = MutableLiveData<Boolean>()
    val passwordValid: LiveData<Boolean> get() = _passwordValid
    fun validatePassword(password: String) {
        val minLength = 8
        val pattern = "^(?=.*[A-Z])(?=.*\\d).+$".toRegex() // Al menos una mayúscula y un número
        val isValid = password.length >= minLength && pattern.matches(password)
        _passwordValid.value = isValid
    }

    fun onInputTextChanged(text: CharSequence) {
        _email.value = text.toString()
        Log.i("loginViewModel", "EmailChanged "+ _email.value)
    }
    fun onInputTextPassChanged(text: CharSequence) {
        _password.value = text.toString()
        Log.i("loginViewModel", "Pass Changed "+ _password.value)
    }
}
