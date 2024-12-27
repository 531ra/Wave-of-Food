package com.example.waveoffood

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waveoffood.Model.MenuItem
import com.example.waveoffood.adapter.MenuAdapter
import com.example.waveoffood.databinding.FragmentMneuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MneuBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMneuBottomSheetBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase
private lateinit var menuItems:MutableList<MenuItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentMneuBottomSheetBinding.inflate(inflater,container,false)
        retreiveMenuItem()


        binding.buttonBack.setOnClickListener { dismiss() }
        return binding.root
    }

    private fun retreiveMenuItem() {
        database=FirebaseDatabase.getInstance()
        val  foodRef:DatabaseReference=database.reference.child("menu")
        menuItems= mutableListOf()
        foodRef.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for(foodSnapShoot in snapshot.children){
                    val menuItem=foodSnapShoot.getValue(MenuItem::class.java)
                    menuItem?.let { it->
                        menuItems.add(it) }

                }
                Log.d("Items","ondatachange:Data received")
                //once the data fetch , set on adapter
                setAdapter()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error fetching data from database: ${error.message}")
            }

        })
    }

    private fun setAdapter() {
        if(menuItems.isNotEmpty()){
        val adapter= MenuAdapter(menuItems,requireContext())
        binding.menuRecyclerView.layoutManager= LinearLayoutManager(requireContext())
         binding.menuRecyclerView.adapter=adapter
            Log.d("Items","set adapter:data set")
        }
        else{
            Log.d("Items","data not set")
        }
    }

    companion object {

    }
}