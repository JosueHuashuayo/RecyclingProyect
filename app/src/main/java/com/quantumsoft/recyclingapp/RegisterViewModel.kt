package com.quantumsoft.recyclingapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class RegisterViewModel : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean> = _registerSuccess

    fun setUsername(newUsername: String) {
        _username.value = newUsername
    }
    fun setEmail(newEmail: String) {
        _email.value = newEmail
    }
    fun setPassword(newPassword: String) {
        _password.value = newPassword
    }

    //Firebase
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    private val _usernameValid = MutableLiveData<Boolean>()
    val usernameValid: LiveData<Boolean> get() = _usernameValid
    fun validateUsername(username: String, onComplete: (Boolean) -> Unit) {
        firestore.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val querySnapshot = task.result
                    val isUsernameUnique = querySnapshot?.isEmpty ?: true
                    _usernameValid.value = isUsernameUnique
                    onComplete(isUsernameUnique)
                } else {
                    // Handle error
                    onComplete(false)
                }
            }
    }


    fun performLogin() {
        val email = _email.value
        val password = _password.value
        val username = _username.value


        validateUsername(username!!) { isUnique ->
            if (isUnique) {
                auth.createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("RegisterViewModel", "createUserWithEmail:success")
                            val user = auth.currentUser
                            _registerSuccess.value = true
                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("RegisterViewModel", "createUserWithEmail:failure", task.exception)
                            _registerSuccess.value = false
                            updateUI(null)
                        }
                    }
            }
            else{

            }
        }

    }
    private fun updateUI(user: FirebaseUser?) {
    }
    //Validacion del Password
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

    fun onInputTextUsernameChanged(text: CharSequence) {
        _username.value = text.toString()
        Log.i("loginViewModel", "Username Changed "+ _username.value)
    }

}
