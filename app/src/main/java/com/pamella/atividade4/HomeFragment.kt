package com.pamella.atividade4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.pamella.atividade4.R
import com.pamella.atividade4.TodoFragment
import com.pamella.atividade4.databinding.FragmentHomeBinding
import com.pamella.atividade4.ui.adapter.ViewPagerAdapter
import com.pamella.atividade4.util.showBottonSheet


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        initListeners()
        initTabs()
    }

    private fun initListeners(){
        binding.btnLogout.setOnClickListener {
            showBottonSheet(
                titleButton = R.string.text_button_dialog_confirm_logout,
                titleDialog = R.string.text_title_dialog_confira_logout,
                message = R.string.text_message_dialog_confirm_logout,
                onClick = {
                    auth.signOut()
                    findNavController().navigate(R.id.action_homeFragment_to_autentication)
                })
        }
    }

    private fun initTabs() {
        val pagerAdapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = pagerAdapter
        pagerAdapter.addFragment(TodoFragment(), R.string.status_task_todo)
        pagerAdapter.addFragment(TodoFragment(), R.string.status_task_doing)
        pagerAdapter.addFragment(TodoFragment(), R.string.status_task_done)

        binding.viewPager.offscreenPageLimit = pagerAdapter.itemCount

        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
            tab.text = getString(pagerAdapter.getTitle(position))
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}