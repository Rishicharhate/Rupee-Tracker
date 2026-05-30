package com.example.expensetracker

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class TransactionModel() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var category: String = ""
    var amount: String = ""
    var date: String = ""
    var mode: String = ""
    var image: Int = 0
    var type: String = "" // "Expense" or "Income"

    constructor(category: String, amount: String, date: String, mode: String, image: Int, type: String) : this() {
        this.category = category
        this.amount = amount
        this.date = date
        this.mode = mode
        this.image = image
        this.type = type
    }
}