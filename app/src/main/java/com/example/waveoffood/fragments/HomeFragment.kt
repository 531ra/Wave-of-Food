package com.example.waveoffood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.AnimationTypes
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.waveoffood.MneuBottomSheetFragment
import com.example.waveoffood.Model.MenuItem
import com.example.waveoffood.adapter.MenuAdapter
import com.example.waveoffood.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems:MutableList<MenuItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        binding.ViewallMenu.setOnClickListener {
            val bottomSheetDialog=MneuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"test")
        }
        //retreive data
        retreiveData()
        return binding.root



 }

    private fun retreiveData() {
        database=FirebaseDatabase.getInstance()
        menuItems= mutableListOf()
        val foodRef:DatabaseReference=database.reference.child("menu")
        foodRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                menuItems.clear()
                for(foodsnapshot in snapshot.children){
                    val menuItem=foodsnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                    //display random popular item
                randomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun randomPopularItems() {
        //create shuffle list of menu list
        val index=menuItems.indices.toList().shuffled()
        val numItemsToShow=6
        val subsetMenuItems=index.take(numItemsToShow).map { menuItems[it] }
        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
        val adapter=MenuAdapter(subsetMenuItems,requireContext())
        binding.PopularRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.PopularRecyclerView.adapter=adapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList=ArrayList<SlideModel>()
        imageList.add(SlideModel("https://images.pexels.com/photos/1640777/pexels-photo-1640777.jpeg", "Get your Favourite meal",ScaleTypes.FIT))
        imageList.add(SlideModel("https://images.pexels.com/photos/842571/pexels-photo-842571.jpeg", "50% OFF",ScaleTypes.FIT))
        imageList.add(SlideModel("https://images.pexels.com/photos/70497/pexels-photo-70497.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Khana ka baad Kuch Meetha hojie.",ScaleTypes.FIT))

        val imageSlider=binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setSlideAnimation(AnimationTypes.ZOOM_OUT)
        // Set the delay
        imageSlider.startSliding(4000)
        // with new period
        imageSlider.startSliding()
        //imageSlider.stopSliding()
        imageSlider.setItemClickListener(object :ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {
               val itemPosition=imageList[position]
                val itemMessage="Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }}
            )



    }

    companion object {

    }
}