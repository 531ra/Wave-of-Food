package com.example.waveoffood

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.waveoffood.Model.OrderDetails
import com.example.waveoffood.databinding.ActivityPayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.razorpay.BuildConfig
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PayoutActivity : AppCompatActivity(),PaymentResultListener {
    private lateinit var  binding:ActivityPayoutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var name:String
    private lateinit var address:String
    private lateinit var phone:String
    private lateinit var totalamoount:String
    private lateinit var foodItemName:ArrayList<String>
    private lateinit var foodItemPrice:ArrayList<String>
    private lateinit var foodItemImage:ArrayList<String>
    private lateinit var foodItemDescription:ArrayList<String>
    private lateinit var foodItemIngredient:ArrayList<String>
    private lateinit var foodItemQuantity:ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //preload Razorpay activity
        Checkout.preload(this@PayoutActivity)
        binding=ActivityPayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
           //initiaze auth and user detail
        auth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().reference
         //set user data
        SetUserData()

        //get user detail from firebase
        val intent=intent
        foodItemName = intent.getStringArrayListExtra("FoodItemName") ?: arrayListOf()
        foodItemPrice = intent.getStringArrayListExtra("FoodItemPrice") ?: arrayListOf()
        foodItemImage = intent.getStringArrayListExtra("FoodItemImage") ?: arrayListOf()
        foodItemDescription = intent.getStringArrayListExtra("FoodItemDescription") ?: arrayListOf()
        foodItemIngredient = intent.getStringArrayListExtra("FoodItemIngredient") ?: arrayListOf()
        foodItemQuantity = intent.getIntegerArrayListExtra("FoodItemQuantity") ?: arrayListOf()

        totalamoount= "₹"+calculateTotalAmount().toString()

        binding.totalAmount.setText(totalamoount)

        binding.button3.setOnClickListener {
            startActivity(Intent(this,MainActivity2::class.java))
        }
        binding.PlacemyOrder.setOnClickListener {
                    //get data from text view
            name=binding.name.text.toString().trim()
            address=binding.Address.text.toString().trim()
            phone=binding.phone.text.toString().trim()
            if(name.isNotEmpty()&&address.isNotEmpty()&&phone.isNotEmpty()){
                placeOrder()


            }
            else{
                Toast.makeText(this, "enter all detail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun placeOrder() {
        userId=auth.currentUser?.uid?:""
        val time=System.currentTimeMillis()
        val itemPushKey=databaseReference.child("OrderDetails").push().key
        val orderDetails=OrderDetails(userId,name,foodItemName,foodItemImage,foodItemPrice,foodItemQuantity,address,totalamoount,phone,false,false,itemPushKey,time)
        val orderReference=databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            // initialize razorpay
                razorPayInitialization()





            removeItemFromCart()


            addOrderToHistory(orderDetails)
        }
            .addOnFailureListener { Toast.makeText(this, "failed to Order", Toast.LENGTH_SHORT).show() }
    }

    private fun razorPayInitialization() {
        val checkout=Checkout()
        checkout.setKeyID(com.example.waveoffood.BuildConfig.razerpay_id)
        try{
            val options= JSONObject()
            options.put("name","Wave Of Food")
            options.put("description","Payment of Your order")
            options.put("theme.color","#FF03DAC5")
            options.put("currency","INR")
            options.put("amount",(totalamoount.removePrefix("₹" ).trim().toInt())*100)

            val prefill = JSONObject()
            prefill.put("name",name)

            prefill.put("address",address)
            prefill.put("email","raghav21076@gmail.com")
            prefill.put("contact",phone)
            options.put("prefill",prefill)

            val retryObj= JSONObject()
            retryObj.put("enableed",true)
            retryObj.put("max_count",4)
            options.put("retry",retryObj)
            checkout.open(this@PayoutActivity,options)}
        catch (e:Exception){
            Toast.makeText(applicationContext, "Error in payment:${e.message.toString()}", Toast.LENGTH_SHORT).show()
            e.printStackTrace()}}


    private fun addOrderToHistory(orderDetails: OrderDetails){
        databaseReference.child("Users").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }

    }

    private fun removeItemFromCart() {
        val cartItemsReference=databaseReference.child("Users").child(userId).child("CartItems")
        cartItemsReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalamount=0
        for(i in 0 until foodItemPrice.size){
            val priceString = foodItemPrice[i]
            val priceIntValue = priceString.replace("$", "").toIntOrNull() ?: 0

            // Get quantity of the item
            val quantity = foodItemQuantity.getOrNull(i) ?: 1 // Default to 1 if quantity is missing

            // Add to total amount
            totalamount += priceIntValue * quantity

        }
        return totalamount


    }

    private fun SetUserData() {
        val user=auth.currentUser
        if(user!=null){
            val userId=user.uid
            val userReference=databaseReference.child("Users").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val names=snapshot.child("name").getValue(String::class.java)?:""
                        val addresss=snapshot.child("address").getValue(String::class.java)?:""
                        val phones=snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            name.setText(names)
                            Address.setText(addresss)
                            phone.setText(phones)


                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Successfully Done", Toast.LENGTH_SHORT).show()
        val bottomsheetDialog=CongratsBottomSheet()
        bottomsheetDialog.show(supportFragmentManager,"test")
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show()

    }
}