package com.example.samojlov_av_homework_module_13_number_6

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "PERSON_DATABASE"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "person_table"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_PRICE = "price"
        const val KEY_WEIGHT = "weight"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " +
                KEY_PRICE + " TEXT, " +
                KEY_WEIGHT + " TEXT" + ")")

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_PRICE, product.price)
        contentValues.put(KEY_WEIGHT, product.weight)
        try {
            db.insert(TABLE_NAME, null, contentValues)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        db.close()
    }

    @SuppressLint("Range", "Recycle")
    fun readProduct(): MutableList<Product> {
        val productList = mutableListOf<Product>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return productList
        }

        var productId: Int
        var productName: String
        var productPrice: String
        var productWeight: String
        if (cursor.moveToFirst()) {
            do {
                productId = cursor.getInt(cursor.getColumnIndex("id"))
                productName = cursor.getString(cursor.getColumnIndex("name"))
                productPrice = cursor.getString(cursor.getColumnIndex("price"))
                productWeight = cursor.getString(cursor.getColumnIndex("weight"))
                val product = Product(productId, productName, productPrice, productWeight)
                productList.add(product)
            } while (cursor.moveToNext())
        }
        return productList
    }

    fun removeAll() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    fun updateProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        contentValues.put(KEY_NAME, product.name)
        contentValues.put(KEY_PRICE, product.price)
        contentValues.put(KEY_WEIGHT, product.weight)
        try {
            db.update(TABLE_NAME, contentValues, "id=" + product.id, null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        db.close()
    }

    fun deleteProduct(product: Product) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, product.id)
        try {
            db.delete(TABLE_NAME, "id=" + product.id, null)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
        db.close()
    }
}