package com.example.waveoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.core.snap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waveoffood.Model.CartItems
import com.example.waveoffood.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.childEvents

class CartAdapter(val context: Context,
    private val cartItems:MutableList<String>,
                  private val cartItemPrices:MutableList<String>,
                  private val cartImages:MutableList<String>,
                  private val cartDescription:MutableList<String>,
                  private val cartQuantity:MutableList<Int>,
                  private val cartIngredients:MutableList<String>

                  ):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
                             // initialize the auth
                          private val auth=FirebaseAuth.getInstance()
//init block to get database reference
    init {
        val database=FirebaseDatabase.getInstance()
        val userId=auth.currentUser?.uid?:""
        val cartItemNumber=cartItems.size
        itemQuantites=IntArray(cartItemNumber){1}
        cartItemsReference=database.reference.child("Users").child(userId).child("CartItems")


    }
    companion object{
        private var itemQuantites:IntArray= intArrayOf()
        private lateinit var cartItemsReference:DatabaseReference
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
       val binding=CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
return cartItems.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
       holder.bind(position)
    }
   inner class CartViewHolder(private val binding:CartItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity=itemQuantites[position]
                cartfoodName.text=cartItems[position]
                cartItemprice.text=cartItemPrices[position]
                val uriString=Uri.parse(cartImages[position])

                Glide.with(context).load(uriString).into(cartImage)


                cartItemquantity.text=quantity.toString()
                minusbtn.setOnClickListener{
                   decreaseQuantity(position)

                }
                plusbtn.setOnClickListener{
                    increaseQuantity(position)

                }
                deletebtn.setOnClickListener{
                    val itemPosition=adapterPosition
                    if(itemPosition!=RecyclerView.NO_POSITION){
                        deleteQuantity(itemPosition)
                   }

                }

            }


        }
        private  fun decreaseQuantity(position: Int){
           if(itemQuantites[position]>1){
               itemQuantites[position]--
               cartQuantity[position]= itemQuantites[position]
               binding.cartItemquantity.text=itemQuantites[position].toString()
           }
       }
       private fun increaseQuantity(position: Int){
           if(itemQuantites[position]<10){
               itemQuantites[position]++
               cartQuantity[position]= itemQuantites[position]
               binding.cartItemquantity.text=itemQuantites[position].toString()
           }

       }
       private fun deleteQuantity(position: Int){
           val positionRetrieve=position
           getUniqueKeyAtPOsition(positionRetrieve){uniqueKey->
               if(uniqueKey!=null){
                   removeItem(position,uniqueKey)
               }
           }

       }

    }

    private fun removeItem(position: Int, uniqueKey: String) {
        if(uniqueKey!=null){
            cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                cartItems.removeAt(position)
                cartImages.removeAt(position)
                cartDescription.removeAt(position)
                cartItemPrices.removeAt(position)
                cartQuantity.removeAt(position)
                cartIngredients.removeAt(position)
                Toast.makeText(context, "Item removed successfully", Toast.LENGTH_SHORT).show()

                //update itemquantity
                itemQuantites= itemQuantites.filterIndexed {  index,i-> index!=position }.toIntArray()
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,cartItems.size)
            }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
        }


    }

    private fun getUniqueKeyAtPOsition(positionRetrieve: Int,onComplete:(String?)->Unit) {
        cartItemsReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey:String?=null
                snapshot.children.forEachIndexed{index,dataSnapshot->
                    if(index==positionRetrieve){
                        uniqueKey=dataSnapshot.key
                        return@forEachIndexed

                    }
                }
                onComplete(uniqueKey)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
//get update quantity
    fun getUpdatedItemsQuantities(): MutableList<Int>{
            val itemQuantity= mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity


    }
}