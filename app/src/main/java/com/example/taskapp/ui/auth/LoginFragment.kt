package com.example.taskapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.data.db.auth.UserManager
import com.example.taskapp.databinding.FragmentLoginBinding
import com.example.taskapp.ui.BaseFragment
import com.example.taskapp.util.showBottomSheet
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressBar.isVisible = false
        userManager = UserManager(requireContext())
        onListener()
    }

    private fun onListener() {

        binding.btnLogin.setOnClickListener {
            validate()
            clearFields()
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun clearFields(){
        binding.edtEmail.setText("")
        binding.edtPassword.setText("")
    }

    private fun checkAuth() {
    }

    private fun validate() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (email.isNotBlank()) {
            if (password.isNotBlank()) {
                hideKeyboard()
                binding.progressBar.isVisible = true
                checkAuth(email, password)
            } else {
                showBottomSheet(message = getString(R.string.password_empty))

            }
        } else {
            showBottomSheet(message = getString(R.string.email_empty))
        }
    }

    private fun checkAuth(email: String, password: String) {
        lifecycleScope.launch {
            val user = userManager.getUserData()
            val userEmail = user.email
            val userPassword = user.password
            if (userEmail.equals(email) && userPassword.equals(password)) {
                binding.progressBar.isVisible = false
                findNavController().navigate(R.id.action_global_taksFragment)

            } else {
                binding.progressBar.isVisible = false
                Toast.makeText(
                    requireContext(),
                    R.string.text_error_login_fragment,
                    Toast.LENGTH_SHORT
                )
                    .show()

            }
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}