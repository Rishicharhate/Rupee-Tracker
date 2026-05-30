package com.example.expensetracker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        val bottomNavigationView =
            findViewById<BottomNavigationView>(
                R.id.bottomNavigationView
            )

        // Default Fragment

        if (savedInstanceState == null) {

            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragmentContainerView,
                    TransactionFragment()
                )
                .commit()
        }

        // Bottom Navigation Click

        bottomNavigationView
            .setOnItemSelectedListener { item ->

                when (item.itemId) {

                    R.id.transaction -> {

                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.fragmentContainerView,
                                TransactionFragment()
                            )
                            .commit()

                        supportActionBar?.title = "Transaction"
                        true
                    }

                    R.id.stats -> {

                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.fragmentContainerView,
                                StatsFragment()
                            )
                            .commit()

                        supportActionBar?.title = "Statistics"
                        true
                    }

                    R.id.more -> {

                        supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.fragmentContainerView,
                                MoreFragment()
                            )
                            .commit()

                        supportActionBar?.title = "Menu"
                        true
                    }

                    else -> false
                }
            }
    }
}