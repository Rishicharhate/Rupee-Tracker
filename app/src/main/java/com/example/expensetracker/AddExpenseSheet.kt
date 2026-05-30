package com.example.expensetracker

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class AddExpenseSheet : BottomSheetDialogFragment() {

    private var selectedCategoryImage: Int = R.drawable.accounts

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_add_expense_sheet,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateEditText = view.findViewById<EditText>(R.id.dateEditText)
        val amountEditText = view.findViewById<EditText>(R.id.amountTextText)
        val categoryEditText = view.findViewById<TextInputEditText>(R.id.categoryEditTextText)
        val modeEditText = view.findViewById<EditText>(R.id.modeEditTextText)
        val saveButton = view.findViewById<Button>(R.id.saveExpenseButton)

        // Category Picker
        childFragmentManager.setFragmentResultListener("categoryRequest", viewLifecycleOwner) { _, bundle ->
            val result = bundle.getString("selectedCategory")
            selectedCategoryImage = bundle.getInt("selectedImage", R.drawable.accounts)
            categoryEditText.setText(result)
        }

        categoryEditText.setOnClickListener {
            val dialog = CategoryDialogFragment()
            dialog.show(childFragmentManager, "CategoryDialog")
        }

        // Date Picker
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    dateEditText.setText(date)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Mode of Payment Picker
        modeEditText.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.payment_mode_dialog, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            dialogView.findViewById<LinearLayout>(R.id.cashLayout).setOnClickListener {
                modeEditText.setText("Cash")
                dialog.dismiss()
            }

            dialogView.findViewById<LinearLayout>(R.id.onlineLayout).setOnClickListener {
                modeEditText.setText("Online")
                dialog.dismiss()
            }
            dialog.show()
        }

        // Save Button Click
        saveButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val amount = amountEditText.text.toString()
            val category = categoryEditText.text.toString()
            val mode = modeEditText.text.toString()

            if (date.isNotEmpty() && amount.isNotEmpty() && category.isNotEmpty() && mode.isNotEmpty()) {
                val transaction = TransactionModel(
                    category = category,
                    amount = amount,
                    date = date,
                    mode = mode,
                    image = selectedCategoryImage,
                    type = "Expense" // Distinguish as an expense
                )

                TransactionRepository.addTransaction(transaction)
                dismiss()

                requireActivity().finish()
            }
        }
    }
}