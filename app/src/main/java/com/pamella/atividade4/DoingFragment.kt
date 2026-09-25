package com.pamella.atividade4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pamella.atividade4.data.model.Task
import com.pamella.atividade4.databinding.FragmentDoingBinding
import com.pamella.atividade4.databinding.FragmentHomeBinding
import com.pamella.atividade4.ui.adapter.TaskAdapter

class DoingFragment : Fragment() {
    private var _binding: FragmentDoingBinding? = null
    private val binding get() = _binding!!

    private lateinit var TaskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

        initRecyclerViewTask(getTask())
    }

    private fun initListeners(){
        binding.floatingActionButton2.setOnClickListener {
            findNavController().navigate((R.id.action_homeFragment_to_fromTaskFragment))
        }
    }

    private fun initRecyclerViewTask(taskList: List<Task>){

        TaskAdapter = TaskAdapter(taskList)
        binding.recyclerViewTask.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTask.setHasFixedSize(true)

        binding.recyclerViewTask.adapter = TaskAdapter
    }

    private fun getTask() = listOf(
        Task("0", "Criar nova tela do app"),
        Task("1", "Validar informações na tela de login"),
        Task("2", "Adcionar nova funionalidade no app"),
        Task("3", "Salvar token localmente"),
        Task("4", "Criar funcionalidade de logout no app"),
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}