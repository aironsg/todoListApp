package com.example.taskapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.data.db.auth.UserManager
import com.example.taskapp.databinding.FragmentRegisterBinding
import com.example.taskapp.ui.BaseFragment
import com.example.taskapp.util.showBottomSheet
import kotlinx.coroutines.launch

class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.isVisible = false
        userManager = UserManager(requireContext())
        onListener()
    }

    private fun onListener(){
        binding.btnCreateAccount.setOnClickListener {
                binding.progressBar.isVisible = true
            validate()
        }
    }

    private fun validate() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (email.isNotBlank()) {
            if (password.isNotBlank()) {
                userRegister(email, password)
            } else {
                showBottomSheet(message = getString(R.string.password_empty))
            }
        } else {
            showBottomSheet(message = getString(R.string.email_empty))
        }
    }

    private  fun userRegister(email: String, password: String){
        hideKeyboard()
        lifecycleScope.launch {
            userManager.saveUserData(email, password)
        }
        binding.progressBar.isVisible = false
        Toast.makeText(requireContext(), R.string.text_create_account_success_fragment, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_global_taksFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}