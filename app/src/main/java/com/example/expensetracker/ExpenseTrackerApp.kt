package com.example.expensetracker

import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class ExpenseTrackerApp : Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        
        val config = RealmConfiguration.Builder(schema = setOf(TransactionModel::class))
            .name("expense_tracker.realm")
            .schemaVersion(1)
            .build()
            
        realm = Realm.open(config)
    }
}