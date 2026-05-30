package com.example.expensetracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionFragment : Fragment() {

    private lateinit var transactionAdapter: TransactionAdapter

    private val transactionList =
        ArrayList<TransactionModel>()

    private lateinit var tvExpense: TextView
    private lateinit var tvIncome: TextView
    private lateinit var tvTotal: TextView

    private lateinit var dateText: TextView
    private lateinit var previousDate: ImageView
    private lateinit var nextDate: ImageView

    private lateinit var tabLayout: TabLayout

    private val calendar =
        Calendar.getInstance()

    private var currentMode = "Daily"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view =
            inflater.inflate(
                R.layout.fragment_transaction,
                container,
                false
            )

        // Views

        val recyclerView =
            view.findViewById<RecyclerView>(
                R.id.transactionRecyclerView
            )

        val addButton =
            view.findViewById<FloatingActionButton>(
                R.id.addButtonMain
            )

        tabLayout =
            view.findViewById(R.id.tabLayout)

        tvExpense =
            view.findViewById(R.id.expense)

        tvIncome =
            view.findViewById(R.id.income)

        tvTotal =
            view.findViewById(R.id.total)

        dateText =
            view.findViewById(R.id.Date)

        previousDate =
            view.findViewById(R.id.previousDate)

        nextDate =
            view.findViewById(R.id.nextDate)

        // RecyclerView

        transactionAdapter =
            TransactionAdapter(
                requireContext(),
                transactionList
            )

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerView.adapter =
            transactionAdapter

        // Observe transactions

        TransactionRepository
            .filteredTransactionsLiveData
            .observe(viewLifecycleOwner) { transactions ->

                transactionList.clear()

                transactionList.addAll(transactions)

                transactionAdapter.notifyDataSetChanged()

                updateSummary(transactions)
            }

        // Add button

        addButton.setOnClickListener {

            startActivity(
                Intent(
                    requireContext(),
                    AddActivity::class.java
                )
            )
        }

        // Tabs

        tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(
                    tab: TabLayout.Tab?
                ) {

                    when (tab?.position) {

                        0 ->
                            currentMode = "Daily"

                        1 ->
                            currentMode = "Monthly"

                        2 ->
                            currentMode = "Yearly"
                    }

                    updateDate()
                }

                override fun onTabUnselected(
                    tab: TabLayout.Tab?
                ) {
                }

                override fun onTabReselected(
                    tab: TabLayout.Tab?
                ) {
                }
            }
        )

        // Previous button

        previousDate.setOnClickListener {

            when (currentMode) {

                "Daily" ->
                    calendar.add(
                        Calendar.DAY_OF_MONTH,
                        -1
                    )

                "Monthly" ->
                    calendar.add(
                        Calendar.MONTH,
                        -1
                    )

                "Yearly" ->
                    calendar.add(
                        Calendar.YEAR,
                        -1
                    )
            }

            updateDate()
        }

        // Next button

        nextDate.setOnClickListener {

            when (currentMode) {

                "Daily" ->
                    calendar.add(
                        Calendar.DAY_OF_MONTH,
                        1
                    )

                "Monthly" ->
                    calendar.add(
                        Calendar.MONTH,
                        1
                    )

                "Yearly" ->
                    calendar.add(
                        Calendar.YEAR,
                        1
                    )
            }

            updateDate()
        }

        updateDate()

        return view
    }

    override fun onResume() {
        super.onResume()

        updateDate()
    }

    private fun updateSummary(
        transactions: List<TransactionModel>
    ) {

        var totalExpense = 0.0

        var totalIncome = 0.0

        for (transaction in transactions) {

            val amount =
                transaction.amount.toDoubleOrNull()
                    ?: 0.0

            if (transaction.type == "Income") {

                totalIncome += amount

            } else {

                totalExpense += amount
            }
        }

        tvExpense.text =
            String.format("%.1f", totalExpense)

        tvIncome.text =
            String.format("%.1f", totalIncome)

        tvTotal.text =
            String.format(
                "%.1f",
                totalIncome - totalExpense
            )
    }

    private fun updateDate() {

        when (currentMode) {

            "Daily" -> {

                val displayFormat =
                    SimpleDateFormat(
                        "dd MMM yyyy",
                        Locale.getDefault()
                    )

                dateText.text =
                    displayFormat.format(calendar.time)

                val day =
                    calendar.get(
                        Calendar.DAY_OF_MONTH
                    )

                val month =
                    calendar.get(
                        Calendar.MONTH
                    ) + 1

                val year =
                    calendar.get(
                        Calendar.YEAR
                    )

                val queryDate =
                    "$day/$month/$year"

                TransactionRepository
                    .getTransactionsDaily(queryDate)
            }

            "Monthly" -> {

                val displayFormat =
                    SimpleDateFormat(
                        "MMMM yyyy",
                        Locale.getDefault()
                    )

                dateText.text =
                    displayFormat.format(calendar.time)

                val month =
                    calendar.get(
                        Calendar.MONTH
                    ) + 1

                val year =
                    calendar.get(
                        Calendar.YEAR
                    )

                val queryMonth =
                    "/$month/$year"

                TransactionRepository
                    .getTransactionsMonthly(
                        queryMonth
                    )
            }

            "Yearly" -> {

                val displayFormat =
                    SimpleDateFormat(
                        "yyyy",
                        Locale.getDefault()
                    )

                dateText.text =
                    displayFormat.format(calendar.time)

                val year =
                    calendar.get(
                        Calendar.YEAR
                    )

                TransactionRepository
                    .getTransactionsYearly(
                        year.toString()
                    )
            }
        }
    }
}