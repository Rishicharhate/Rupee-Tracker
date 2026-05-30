package com.example.expensetracker

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class MoreFragment : Fragment(R.layout.fragment_more) {

    private lateinit var themeLayout: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themeLayout = view.findViewById(R.id.theme)

        themeLayout.setOnClickListener {

            val currentMode =
                AppCompatDelegate.getDefaultNightMode()

            if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {

                // Switch to Light Mode
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )

            } else {

                // Switch to Dark Mode
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }
        }
    }
}