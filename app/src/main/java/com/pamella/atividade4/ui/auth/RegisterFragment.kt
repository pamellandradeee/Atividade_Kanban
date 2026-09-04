package com.pamella.atividade4.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pamella.atividade4.R
import com.pamella.atividade4.databinding.FragmentRegisterBinding
import com.pamella.atividade4.util.initToolbar
import com.pamella.atividade4.util.showBottonSheet

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListener()
    }

    private fun initListener(){
        binding.btnRegister.setOnClickListener{
            validateData()
        }
    }

    private fun validateData(){
        val email = binding.edittextemail.text.toString().trim()
        val senha = binding.edittextsenha.text.toString().trim()

        if (email.isNotBlank()){
            if(senha.isNotBlank()){
                Toast.makeText(requireContext(), "Tudo OK!", Toast.LENGTH_SHORT).show()

            } else{
                showBottonSheet(message = R.string.password_empty_register_fragment)
            }
        } else {
            showBottonSheet(message = R.string.email_empty_register_fragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}