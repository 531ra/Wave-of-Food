package com.example.waveoffood

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.waveoffood.Model.CartItems
import com.example.waveoffood.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private  var  foodName:String?=null
    private  var  foodImage:String?=null
    private  var  foodDescription:String?=null
    private  var  foodIngredients:String?=null
    private var foodPrice:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
         foodName=intent.getStringExtra("MenuItemName")
         foodImage=intent.getStringExtra("MenuItemImage")
         foodDescription=intent.getStringExtra("MenuItemDescription")
         foodIngredients=intent.getStringExtra("MenuItemIngredients")

        binding.apply {  detailFoodName.text=foodName
        Description.text=foodDescription
        Ingredients.text=foodIngredients
        val Uri= Uri.parse(foodImage)

        Glide.with(this@DetailsActivity).load(foodImage).into(FoodImage)
        }
foodPrice=intent.getStringExtra("MenuItemPrice")

        binding.imageButton.setOnClickListener { finish() }
        auth=FirebaseAuth.getInstance()
        binding.AddToCart.setOnClickListener{
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        database=FirebaseDatabase.getInstance().reference
        val userId=auth.currentUser?.uid?:""

        //create a cart item object
        val cartItem=CartItems(foodName, foodPrice ,foodDescription,foodImage,foodIngredients,1)
        //save data to realtime database to cartmenu
        database.child("Users").child(userId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener { task ->
                Toast.makeText(this, "Item added to your cart", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add Item in your cart", Toast.LENGTH_SHORT).show()
            }



    }
}