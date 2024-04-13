package com.harush.david.discoapp.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.harush.david.discoapp.R
import com.harush.david.discoapp.databinding.FragmentFirstPageBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class FirstPageFragment : Fragment() {

    private var binding: FragmentFirstPageBinding? = null
    private val sharedDataViewModel: SharedDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstPageBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDateTimeEveryMinute()
        binding?.button?.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_first_page_to_navigation_travel)
        }

        sharedDataViewModel.lastVisitedFeedTitle.observe(viewLifecycleOwner) { title ->
            binding?.textViewLastVisited?.text = title ?: ""
        }
    }

    private fun updateDateTimeEveryMinute() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                binding?.textViewDateTime?.text =
                    SimpleDateFormat("dd/MM - HH:mm:ss", Locale.getDefault()).format(
                        Date()
                    )
                handler.postDelayed(this, 800)
            }
        }
        runnable.run()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
