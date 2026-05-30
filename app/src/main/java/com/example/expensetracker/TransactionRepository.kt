package com.example.expensetracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

object TransactionRepository {

    private val realm =
        ExpenseTrackerApp.realm

    private val _filteredTransactionsLiveData =
        MutableLiveData<List<TransactionModel>>()

    val filteredTransactionsLiveData:
            LiveData<List<TransactionModel>>
            = _filteredTransactionsLiveData

    private val _allTransactionsLiveData =
        MutableLiveData<List<TransactionModel>>()

    val allTransactionsLiveData:
            LiveData<List<TransactionModel>>
            = _allTransactionsLiveData

    init {

        // Observe all transaction changes
        CoroutineScope(Dispatchers.IO).launch {

            realm.query<TransactionModel>()
                .asFlow()
                .collect { changes ->

                    val data =
                        changes.list.toList()

                    _allTransactionsLiveData.postValue(data)
                }
        }
    }

    fun addTransaction(
        transaction: TransactionModel
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            realm.write {

                copyToRealm(transaction)
            }
        }
    }

    // DAILY
    fun getTransactionsDaily(
        date: String
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            val results =
                realm.query<TransactionModel>(
                    "date == $0",
                    date
                ).find()

            _filteredTransactionsLiveData.postValue(
                results.toList()
            )
        }
    }

    // MONTHLY
    fun getTransactionsMonthly(
        monthYear: String
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            val results =
                realm.query<TransactionModel>(
                    "date ENDSWITH $0",
                    monthYear
                ).find()

            _filteredTransactionsLiveData.postValue(
                results.toList()
            )
        }
    }

    // YEARLY
    fun getTransactionsYearly(
        year: String
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            val results =
                realm.query<TransactionModel>(
                    "date ENDSWITH $0",
                    "/$year"
                ).find()

            _filteredTransactionsLiveData.postValue(
                results.toList()
            )
        }
    }
}