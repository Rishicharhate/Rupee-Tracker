package com.example.expensetracker

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat

class CategoryAdapter(
    private val context: Context,
    private val list: ArrayList<CategoryModel>,
    private val listener: OnCategoryClick
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    interface OnCategoryClick {
        fun onClick(category: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgCategory: ImageView = itemView.findViewById(R.id.imgCategory)
        val txtCategory: TextView = itemView.findViewById(R.id.txtCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_category, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = list[position]

        // Set category name
        holder.txtCategory.text = model.name

        // Set category image
        holder.imgCategory.setImageResource(model.image)

        // Set dynamic background color
        holder.imgCategory.backgroundTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(context, model.color)
            )

        // Click listener
        holder.itemView.setOnClickListener {
            listener.onClick(model.name)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}