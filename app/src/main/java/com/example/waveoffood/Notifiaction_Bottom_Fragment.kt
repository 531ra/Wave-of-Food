package com.example.waveoffood

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waveoffood.adapter.NotificationAdapter
import com.example.waveoffood.databinding.FragmentNotifiactionBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Notifiaction_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentNotifiactionBottomBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentNotifiactionBottomBinding.inflate(layoutInflater,container,false)
        val notification= arrayListOf("your order has been Cancelled Successfully","order has been taken by the driver","Congrats your Order placed")
        val notificationImages= arrayListOf(R.drawable.ic_launcher_foreground,R.drawable.icon,R.drawable.baseline_arrow_circle_left_24)
        val adapter:NotificationAdapter= NotificationAdapter(notification,notificationImages)
        binding.NotificationRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.NotificationRecyclerView.adapter=adapter

        return binding.root
    }

    companion object {

    }
}