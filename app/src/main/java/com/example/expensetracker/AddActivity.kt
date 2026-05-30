package com.example.expensetracker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)

        val addExpenseButton = findViewById<Button>(R.id.addExpenseButton)
        addExpenseButton.setOnClickListener {
            val bottomSheetFragment = AddExpenseSheet()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        val addIncomeButton = findViewById<Button>(R.id.addIncomeButton)
        addIncomeButton.setOnClickListener { // Fixed: was using addExpenseButton
            val bottomSheetFragment = AddIncomeSheet()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        //Back Button
        val backButton =
            findViewById<ImageView>(
                R.id.backButtonAddActivity
            )

        backButton.setOnClickListener {

            finish()
        }
    }
}