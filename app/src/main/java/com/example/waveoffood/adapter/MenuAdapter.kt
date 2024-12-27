package com.example.waveoffood.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.waveoffood.DetailsActivity
import com.example.waveoffood.databinding.MenuItemBinding

class MenuAdapter(private val menuItems: List<com.example.waveoffood.Model.MenuItem>, private val requirecontext:Context):RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding=MenuItemBinding.inflate(LayoutInflater.from( parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
     holder.bind(position)
    }
  inner  class MenuViewHolder(private val binding:MenuItemBinding): RecyclerView.ViewHolder(binding.root) {
      init {
          binding.root.setOnClickListener{
              val position= adapterPosition
              if(position!=RecyclerView.NO_POSITION){

                  openDetailsActivity(position)

              }

          }
      }

      private fun openDetailsActivity(position: Int) {
          val menuItem=menuItems[position]
          //a intent to open detail activityand pass data
          val intent=Intent(requirecontext,DetailsActivity::class.java)
              .apply {
                  putExtra("MenuItemName",menuItem.foodName)
                  putExtra("MenuItemPrice",menuItem.foodPrice)
                  putExtra("MenuItemImage",menuItem.foodImage)
                  putExtra("MenuItemDescription",menuItem.foodDescription)
                  putExtra("MenuItemIngredients",menuItem.foodIngredients)
              }
          requirecontext.startActivity(intent)

      }

      //set data on recycler view ->name,price and image
      fun bind(position: Int) {
          val menuItem=menuItems[position]
          binding.apply {

              menuFoodName.text=menuItem.foodName
                  menuPrice.text="₹"+menuItem.foodPrice
            val Uri= Uri.parse(menuItem.foodImage)
              Glide.with(requirecontext).load(Uri).into(menuImage)



          }

      }

  }



}

