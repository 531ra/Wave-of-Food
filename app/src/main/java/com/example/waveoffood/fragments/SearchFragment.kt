package com.example.waveoffood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waveoffood.adapter.MenuAdapter
import com.example.waveoffood.databinding.FragmentSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var adapter:MenuAdapter
    private lateinit var auth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val originalMenuItems= mutableListOf<com.example.waveoffood.Model.MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=FragmentSearchBinding.inflate(inflater,container,false)
        //retrieve menu item from database
        retrieveMenuItem()


        //setup for search view
        setupSearchView()



        return binding.root
    }

    private fun retrieveMenuItem() {
        //get database reference
        database=FirebaseDatabase.getInstance()
        //reference to the data node
        val foodReference=database.reference.child("menu")
        foodReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuItem=foodSnapshot.getValue(com.example.waveoffood.Model.MenuItem::class.java)
                    menuItem?.let {
                        originalMenuItems?.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun showAllMenu() {
        val filterdMenuItem=ArrayList(originalMenuItems)
        setAdapter(filterdMenuItem)
    }

    private fun setAdapter(filterdMenuItem: List<com.example.waveoffood.Model.MenuItem>) {
        adapter=MenuAdapter(filterdMenuItem,requireContext())
        binding.menuRecyclerView.adapter=adapter
        binding.menuRecyclerView.layoutManager=LinearLayoutManager(requireContext())
    }


    private fun setupSearchView() {
        binding.search.setOnQueryTextListener(object :android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
             filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
           filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        val filteredMenuItems=originalMenuItems.filter {
            it.foodName?.contains(query,ignoreCase = true)==true

        }
        setAdapter(filteredMenuItems)
        }
    companion object{
        
    }


    }


