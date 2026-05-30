package com.example.expensetracker

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(
    private val context: Context,
    private val list: ArrayList<TransactionModel>
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)
        val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        val modeOfPayment: TextView = itemView.findViewById(R.id.modeOfPayment)
        val transactionDate: TextView = itemView.findViewById(R.id.transactionDate)
        val transactionAmount: TextView = itemView.findViewById(R.id.transactionAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.row_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.categoryName.text = model.category
        holder.transactionDate.text = model.date
        holder.modeOfPayment.text = model.mode
        
        // Format amount and set color based on type
        if (model.type == "Expense") {
            holder.transactionAmount.text = "- ${model.amount}"
            holder.transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.Red))
        } else {
            holder.transactionAmount.text = "+ ${model.amount}"
            holder.transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.Green))
        }
        
        holder.categoryImage.setImageResource(model.image)
    }

    override fun getItemCount(): Int = list.size
}