package com.example.expensetracker

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StatsFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var dateText: TextView
    private lateinit var previousDate: ImageView
    private lateinit var nextDate: ImageView
    private lateinit var tabLayout: TabLayout

    private lateinit var expenseButton: Button
    private lateinit var incomeButton: Button

    private lateinit var emptyImage: ImageView

    private val calendar = Calendar.getInstance()

    private var currentChartType = "Expense"
    private var currentTransactions: List<TransactionModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pieChart = view.findViewById(R.id.pieChart)

        dateText = view.findViewById(R.id.Date)
        previousDate = view.findViewById(R.id.previousDate)
        nextDate = view.findViewById(R.id.nextDate)
        tabLayout = view.findViewById(R.id.tabLayout)

        expenseButton = view.findViewById(R.id.expenseButton)
        incomeButton = view.findViewById(R.id.incomeButton)

        emptyImage = view.findViewById(R.id.emptyImage)

        setupChart()

        TransactionRepository.filteredTransactionsLiveData.observe(viewLifecycleOwner) {
                transactions ->

            currentTransactions = transactions
            updateChartData()
        }

        expenseButton.setOnClickListener {

            currentChartType = "Expense"

            updateButtonVisuals()

            updateChartData()
        }

        incomeButton.setOnClickListener {

            currentChartType = "Income"

            updateButtonVisuals()

            updateChartData()
        }

        previousDate.setOnClickListener {

            updatePeriod(-1)

            refreshData()
        }

        nextDate.setOnClickListener {

            updatePeriod(1)

            refreshData()
        }

        tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(tab: TabLayout.Tab?) {

                    refreshData()
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            }
        )

        updateButtonVisuals()

        refreshData()
    }

    private fun setupChart() {

        pieChart.setUsePercentValues(true)

        pieChart.description.isEnabled = false

        pieChart.isDrawHoleEnabled = true

        pieChart.setHoleColor(Color.TRANSPARENT)

        pieChart.setEntryLabelColor(Color.WHITE)

        pieChart.setCenterTextColor(Color.WHITE)

        pieChart.legend.textColor = Color.WHITE

        pieChart.animateY(1000)
    }

    private fun updateChartData() {

        val entries = ArrayList<PieEntry>()

        if (currentChartType == "Expense") {

            val expenses = currentTransactions
                .filter { it.type == "Expense" }
                .groupBy { it.category }
                .mapValues {
                        entry ->

                    entry.value.sumOf {
                        it.amount.toDoubleOrNull() ?: 0.0
                    }
                }

            for ((category, amount) in expenses) {

                entries.add(
                    PieEntry(
                        amount.toFloat(),
                        category
                    )
                )
            }

            pieChart.centerText = "Expenses"
        }

        else {

            val incomes = currentTransactions
                .filter { it.type == "Income" }
                .groupBy { it.category }
                .mapValues {
                        entry ->

                    entry.value.sumOf {
                        it.amount.toDoubleOrNull() ?: 0.0
                    }
                }

            for ((category, amount) in incomes) {

                entries.add(
                    PieEntry(
                        amount.toFloat(),
                        category
                    )
                )
            }

            pieChart.centerText = "Income"
        }

        //if empty transaction then show empty image
        if (entries.isEmpty()) {

            pieChart.clear()
            pieChart.visibility = View.INVISIBLE

            emptyImage.visibility = View.VISIBLE

        } else {

            emptyImage.visibility = View.GONE
            pieChart.visibility = View.VISIBLE

            val dataSet = PieDataSet(entries, "")
            val data = PieData(dataSet)

            pieChart.data = data
            pieChart.invalidate()
        }

        val dataSet = PieDataSet(
            entries,
            currentChartType
        )

        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        dataSet.valueTextColor = Color.WHITE

        dataSet.valueTextSize = 14f

        val data = PieData(dataSet)

        pieChart.data = data

        pieChart.invalidate()
    }

    private fun updateButtonVisuals() {

        if (currentChartType == "Expense") {

            expenseButton.alpha = 1.0f

            incomeButton.alpha = 0.5f
        }

        else {

            expenseButton.alpha = 0.5f

            incomeButton.alpha = 1.0f
        }
    }

    private fun updatePeriod(delta: Int) {

        when (tabLayout.selectedTabPosition) {

            0 -> calendar.add(Calendar.DAY_OF_MONTH, delta)

            1 -> calendar.add(Calendar.MONTH, delta)

            2 -> calendar.add(Calendar.YEAR, delta)
        }
    }

    private fun refreshData() {

        val sdf: SimpleDateFormat

        when (tabLayout.selectedTabPosition) {

            0 -> {

                sdf = SimpleDateFormat(
                    "dd MMM yyyy",
                    Locale.getDefault()
                )

                val day =
                    calendar.get(Calendar.DAY_OF_MONTH)

                val month =
                    calendar.get(Calendar.MONTH) + 1

                val year =
                    calendar.get(Calendar.YEAR)

                val query =
                    "$day/$month/$year"

                TransactionRepository.getTransactionsDaily(query)
            }

            1 -> {

                sdf = SimpleDateFormat(
                    "MMM yyyy",
                    Locale.getDefault()
                )

                val month =
                    calendar.get(Calendar.MONTH) + 1

                val year =
                    calendar.get(Calendar.YEAR)

                val query =
                    "/$month/$year"

                TransactionRepository.getTransactionsMonthly(query)
            }

            2 -> {

                sdf = SimpleDateFormat(
                    "yyyy",
                    Locale.getDefault()
                )

                val year =
                    calendar.get(Calendar.YEAR)

                TransactionRepository.getTransactionsYearly(
                    year.toString()
                )
            }

            else -> return
        }

        dateText.text = sdf.format(calendar.time)
    }
}