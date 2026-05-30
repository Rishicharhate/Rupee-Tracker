package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_category, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerCategory)

        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)

        val list = arrayListOf(
            CategoryModel("Food", R.drawable.food, R.color.category1),
            CategoryModel("Shopping", R.drawable.shopping, R.color.category2),
            CategoryModel("Education", R.drawable.education,R.color.category3),
            CategoryModel("Entertainment", R.drawable.entertenment, R.color.category4),
            CategoryModel("Workout", R.drawable.workout, R.color.category5),
            CategoryModel("Travel", R.drawable.travel, R.color.category1),
            CategoryModel("Health", R.drawable.health, R.color.category2),
            CategoryModel("Fuel", R.drawable.fuel, R.color.category3),
            CategoryModel("Bill", R.drawable.billing, R.color.category4),
            CategoryModel("Othert", R.drawable.other, R.color.category5)
        )

        val adapter = CategoryAdapter(requireContext(), list,
            object : CategoryAdapter.OnCategoryClick {
                override fun onClick(category: String) {
                    // Find the image resource for the selected category
                    val selectedModel = list.find { it.name == category }
                    val imageRes = selectedModel?.image ?: R.drawable.accounts

                    // Send result back to the calling fragment
                    parentFragmentManager.setFragmentResult(
                        "categoryRequest",
                        bundleOf(
                            "selectedCategory" to category,
                            "selectedImage" to imageRes
                        )
                    )
                    dismiss()
                }
            })

        recyclerView.adapter = adapter
        return view
    }
}