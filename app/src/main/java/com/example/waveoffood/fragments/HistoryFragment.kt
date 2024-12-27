package com.example.waveoffood.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.waveoffood.Model.OrderDetails
import com.example.waveoffood.adapter.BuyAgainAdapter
import com.example.waveoffood.databinding.FragmentHistoryBinding
import com.example.waveoffood.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.Serializable


class HistoryFragment : Fragment() {
    private lateinit var  binding:FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var auth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userID:String
    private var listofOrderItems:MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHistoryBinding.inflate(layoutInflater,container,false)

        //initialize the firebase objects
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        //retrive and display  the user order history
        retrieveBuyHistory()

        binding.recentbutitem.setOnClickListener {
            seeItemsRecentBuy()
        }
        binding.receivedButton.setOnClickListener{
            updateOrderStatus()
        }


        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey=listofOrderItems[0].itemPushKey
        val completOrderReference=database.reference.child("CompletedOrder").child(itemPushKey!!)
        completOrderReference.child("paymentReceived").setValue(true)


    }

    private fun seeItemsRecentBuy() {
        listofOrderItems.firstOrNull()?.let {recentBuy->
            val intent= Intent(requireContext(),recentOrderItems::class.java)
            intent.putExtra("RecentBuyOrderItem",listofOrderItems as Serializable)
            startActivity(intent)
        }


    }

    private fun retrieveBuyHistory() {

        userID=auth.currentUser?.uid?:""
        //getting reference to the location where ower buy history data has been stored
        val buyItemReference=database.reference.child("Users").child(userID).child("BuyHistory")
        val shortingQuery=buyItemReference.orderByChild(" currentTime")
        shortingQuery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(buySnapshot in snapshot.children){
                    val buyHistoryItem=buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listofOrderItems?.add(it)
                    }

                }

                listofOrderItems.reverse()
                if(listofOrderItems.isNotEmpty()){
                    setDataInRecentBuy()
                    setPreviousBuyItemsRecyclerView()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun setDataInRecentBuy() {

        val recentOrderItem=listofOrderItems.firstOrNull()
    recentOrderItem?.let {
        binding.apply {
            buyAgainFoodName.text=it.foodNames?.firstOrNull()?:""
            buyAgainFoodPrice.text=it.foodPrices?.firstOrNull()?:""
            val image=it.foodImages?.firstOrNull()?:""
            val uri= Uri.parse(image)
            Glide.with(requireContext()).load(uri).into(buyAgainFoodImage)
            val isOrderIsAccepted=listofOrderItems[0].orderAccepted
            if(isOrderIsAccepted == true){
                orderStatus.background.setTint(Color.GRAY)
                receivedButton.visibility=View.VISIBLE

            }





        }
    }}

    private fun setPreviousBuyItemsRecyclerView() {
        val buyAgainFoodName= mutableListOf<String>()
        val buyAgainFoodPrice= mutableListOf<String>()
        val buyAgainFoodImage= mutableListOf<String>()
        for(i in 1 until listofOrderItems.size){
            listofOrderItems[i].foodNames?.firstOrNull()?.let {
                buyAgainFoodName?.add(it)
            }
            listofOrderItems[i].foodPrices?.firstOrNull()?.let {
                buyAgainFoodPrice?.add(it)
            }
            listofOrderItems[i].foodImages?.firstOrNull()?.let {
                buyAgainFoodImage?.add(it)
            }

        }
        val rv=binding.BuyAgainRecyclerView
        rv.layoutManager=LinearLayoutManager(requireContext())
        buyAgainAdapter=
            BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage,requireContext())
        rv.adapter=buyAgainAdapter


    }



    companion object {

    }
}