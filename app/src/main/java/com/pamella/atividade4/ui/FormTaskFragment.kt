package com.pamella.atividade4.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pamella.atividade4.R
import com.pamella.atividade4.data.model.Status
import com.pamella.atividade4.data.model.Task
import com.pamella.atividade4.databinding.FragmentFormTaskBinding
import com.pamella.atividade4.util.initToolbar
import com.pamella.atividade4.util.showBottonSheet

class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!
    private var task: Task = Task()
    private var newTask: Boolean = true
    private var status: Status = Status.TODO
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        reference = Firebase.database.reference
        auth = Firebase.auth

        initListener()
    }

    private fun initListener(){
        binding.btnSave.setOnClickListener {
            validateData()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            status = when(id){
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun validateData(){
        val description = binding.editTextDescricao.text.toString().trim()

        if (description.isNotBlank()) {
            binding.progressBar.isVisible = true

            if (newTask) {
                task = Task()
            }

            task.id = reference.push().key ?: ""
            task.description = description
            task.status = status

            saveTask()

        } else {
            showBottonSheet(message = R.string.description_empty_from_task_fragment)
        }
    }

    private fun saveTask(){
        val userId = auth.currentUser?.uid ?: return

        reference
            .child("task")
            .child(userId) // Usando a variável limpa
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                binding.progressBar.isVisible = false
                if(result.isSuccessful){
                    Toast.makeText(
                        requireContext(),
                        R.string.text_save_sucess_form_task_fragment,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (newTask){
                        findNavController().popBackStack()
                    }
                } else {
                    showBottonSheet(message = R.string.error_generic)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}