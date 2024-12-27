package com.example.waveoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.waveoffood.Model.OrderDetails
import com.example.waveoffood.adapter.RecentBuyAdapter
import com.example.waveoffood.databinding.ActivityRecentOrderItemsBinding

class recentOrderItems : AppCompatActivity() {
    private lateinit var binding:ActivityRecentOrderItemsBinding
    private lateinit var allFoodNames:ArrayList<String>
    private lateinit var allFoodPrice:ArrayList<String>
    private lateinit var allFoodImages:ArrayList<String>
    private lateinit var allFoodQuantities:ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recent_order_items)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityRecentOrderItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backbtn.setOnClickListener {
            finish()
        }

        val recentOrderItems=intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>

        recentOrderItems?.let {  orderDetails ->
            if(orderDetails.isNotEmpty()){
                val recentOrderItem=orderDetails[0]
                allFoodNames=recentOrderItem.foodNames as ArrayList<String>
                allFoodPrice=recentOrderItem.foodPrices as ArrayList<String>
                allFoodImages=recentOrderItem.foodImages as ArrayList<String>
                allFoodQuantities=recentOrderItem.foodQuantities as ArrayList<Int>
            }
        }
        setAdapter()

    }

    private fun setAdapter() {
        val  rv=binding.recyclerViewRecentBuy
        rv.layoutManager=LinearLayoutManager(this)
        val adapter=RecentBuyAdapter(this,allFoodNames,allFoodPrice,allFoodImages,allFoodQuantities)
        rv.adapter=adapter
    }
}