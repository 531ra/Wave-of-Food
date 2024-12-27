package com.example.waveoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waveoffood.databinding.BuyAgainItemBinding

class BuyAgainAdapter(
    private val buyAgainFoodName: MutableList<String>,
    private val buyAgainFoodPrice: MutableList<String>,
    private val buyAgainFoodImage: MutableList<String>,
  private val requireContext: Context
):RecyclerView.Adapter<BuyAgainAdapter.BuyagainViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyagainViewHolder {
        val binding=BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyagainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return buyAgainFoodName.size
    }

    override fun onBindViewHolder(holder: BuyagainViewHolder, position: Int) {
        holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position],buyAgainFoodImage[position],requireContext)
    }
   inner class BuyagainViewHolder(private val binding:BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
       fun bind(foodName: String, foodPrice: String, foodImage: String, requireContext: Context) {
           binding.buyAgainFoodName.text=foodName
           binding.buyAgainFoodPrice.text=foodPrice

           Glide.with(requireContext).load(Uri.parse(foodImage)).into(binding.buyAgainFoodImage)
       }

   }
}