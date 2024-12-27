package com.example.waveoffood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waveoffood.Model.CartItems
import com.example.waveoffood.PayoutActivity
import com.example.waveoffood.adapter.CartAdapter
import com.example.waveoffood.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var  foodName:MutableList<String>
    private lateinit var foodPrice:MutableList<String>
    private lateinit var foodImage:MutableList<String>
    private lateinit var foodDescription:MutableList<String>
    private lateinit var foodIngredients:MutableList<String>
    private lateinit var quantity:MutableList<Int>
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCartBinding.inflate(inflater, container, false)
        auth=FirebaseAuth.getInstance()
        retrieveCartItems()

        binding.proceedBtn.setOnClickListener {
            //get order item detail for proceeding
            getOrderItemsDetails()

            startActivity(Intent(requireContext(),PayoutActivity::class.java))
        }
        return binding.root

    }

    private fun getOrderItemsDetails() {
        val orderIdReference:DatabaseReference=database.reference.child("Users").child(userId).child("CartItems")
        val foodName= mutableListOf<String>()
        val foodImage= mutableListOf<String>()
        val foodPrice= mutableListOf<String>()
        val foodDescription= mutableListOf<String>()
        val foodIngredients= mutableListOf<String>()
        //get item quantity
        val foodQuantities=cartAdapter.getUpdatedItemsQuantities()
        orderIdReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val orderItems=foodSnapshot.getValue(CartItems::class.java)
                    //add item in to list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodIngredients?.let { foodIngredients.add(it) }

                }
                orderNow(foodName,foodPrice,foodDescription,foodImage,foodIngredients,foodQuantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "order making failed", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImage: MutableList<String>,
        foodIngredients: MutableList<String>,
        foodQuantities: MutableList<Int>
    ) {
        if(isAdded && context!=null){
            val intent=Intent(requireContext(),PayoutActivity::class.java)
            intent.apply {
                putExtra("FoodItemName",foodName as ArrayList<String>)
                putExtra("FoodItemPrice",foodPrice as ArrayList<String>)
                putExtra("FoodItemImage",foodImage as ArrayList<String>)
                putExtra("FoodItemDescription",foodDescription as ArrayList<String>)
                putExtra("FoodItemIngredient",foodIngredients as ArrayList<String>)
                putExtra("FoodItemQuantity",foodQuantities as ArrayList<Int>)
                startActivity(intent)

            }

        }


    }

    private fun retrieveCartItems() {
        //database reference to the firebase
        database=FirebaseDatabase.getInstance()
        userId=auth.currentUser?.uid?:""
        val foodRef:DatabaseReference=database.reference.child("Users").child(userId).child("CartItems")

        //list to store cart items
        foodName= mutableListOf()
        foodPrice= mutableListOf()
        foodImage= mutableListOf()
        foodDescription= mutableListOf()
        foodIngredients= mutableListOf()
        quantity= mutableListOf()
        //fetch data from database
        foodRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val cartItems=foodSnapshot.getValue(CartItems::class.java)
                    cartItems?.foodName?.let { foodName.add(it) }
                    cartItems?.foodPrice?.let { foodPrice.add(it) }
                    cartItems?.foodImage?.let { foodImage.add(it) }
                    cartItems?.foodDescription?.let { foodDescription.add(it) }
                    cartItems?.foodIngredients?.let { foodIngredients.add(it) }
                    cartItems?.foodQuantity?.let {quantity.add(it) }

                }
                setAdapter()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "data not fetched", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setAdapter() {
        cartAdapter=CartAdapter(requireContext(),foodName,foodPrice,foodImage,foodDescription,quantity,foodIngredients)

        binding.cartRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.cartRecyclerView.adapter=cartAdapter
    }

    companion object {

    }
}