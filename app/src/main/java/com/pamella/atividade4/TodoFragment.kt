package com.pamella.atividade4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.pamella.atividade4.data.model.Status
import com.pamella.atividade4.data.model.Task
import com.pamella.atividade4.databinding.FragmentTodoBinding
import com.pamella.atividade4.ui.adapter.TaskAdapter
import com.pamella.atividade4.util.showBottonSheet

class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reference = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        initListeners()
        initRecyclerViewTask()
        getTask()
    }

    private fun initListeners(){
        binding.floatingActionButton2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_fromTaskFragment)
        }
    }

    private fun initRecyclerViewTask(){
        taskAdapter = TaskAdapter(requireContext()) { task, option -> optionSelected(task, option) }

        with(binding.recyclerViewTask){
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
    }

    private fun optionSelected(task: Task, option: Int){
        when (option){
            TaskAdapter.SELECT_REMOVER -> {
                showBottonSheet(
                    titleDialog = R.string.text_title_dialog_delete,
                    message = R.string.text_message_dialog_delete,
                    titleButton = R.string.text_button_dialog_confirm,
                    onClick = {
                        deleteTask(task)
                    }
                )

                Toast.makeText(requireContext(), "Removendo ${task.description}", Toast.LENGTH_SHORT).show()
            }
            TaskAdapter.SELECT_EDIT -> {
                Toast.makeText(requireContext(), "Editando ${task.description}", Toast.LENGTH_SHORT).show()
            }
            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT).show()
            }
            TaskAdapter.SELECT_NEXT -> {
                Toast.makeText(requireContext(), "Próximo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTask() {
        val userId = auth.currentUser?.uid ?: return
        reference
            .child("tasks")
            .child(userId)
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val taskList = mutableListOf<Task>()

                    for (ds in snapshot.children){
                        val task = ds.getValue(Task::class.java)
                        task?.let { nonNullTask ->
                            if (nonNullTask.status == Status.TODO) {
                                taskList.add(nonNullTask)
                            }
                        }
                    }

                    binding.progressBar.isVisible = false
                    listEmpty(taskList)

                    taskList.reverse()
                    taskAdapter.submitList(taskList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun deleteTask(task: Task){
        reference
            .child("tasks")
            .child(auth.currentUser?.uid ?:"")
            .child(task.id)
            .removeValue().addOnCompleteListener { result ->
                if (result.isSuccessful){
                    Toast.makeText(requireContext(), R.string.text_delete_sucess_task, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), R.string.error_generic, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun listEmpty(taskList: List<Task>){
        binding.textInfo.text = if (taskList.isEmpty()){
            getString(R.string.text_list_task_empty)
        }else{
            ""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}