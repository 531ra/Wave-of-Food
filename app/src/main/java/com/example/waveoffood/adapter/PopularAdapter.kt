//package com.example.waveoffood.adapter
//
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.waveoffood.DetailsActivity
//import com.example.waveoffood.Model.MenuItem
//import com.example.waveoffood.databinding.PopularItemBinding

//class PopularAdapter(private val menuItems: List<MenuItem>, private val requirecontext:Context):RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
//        return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
//    }
//
//    override fun getItemCount(): Int {
//        return menuItems.size
//    }
//
//    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
//       val item=items[position]
//        val images= image[position]
//        val price=price[position]
//
//        holder.bind(item,images,price)
//        holder.itemView.setOnClickListener{
//            //set on click listener to open details
//            val intent= Intent(requirecontext, DetailsActivity::class.java)
//            intent.putExtra("MenuItemName",items.get(position))
//            intent.putExtra("MenuItemImage",image.get(position))
//            requirecontext.startActivity(intent)

//        }
//    }
//    inner class PopularViewHolder(private val binding:PopularItemBinding):RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: String, images: Int,price: String) {
//            binding.FoodNamePopular.text=item
//           binding.PricePopular.text=price
//            binding.foodimage.setImageResource(images)
//        }
//
//    }
//}