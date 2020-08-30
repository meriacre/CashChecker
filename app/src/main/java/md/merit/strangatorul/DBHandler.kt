package md.merit.strangatorul

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import kotlin.Exception

class DBHandler(context: Context, name : String?, factory : SQLiteDatabase.CursorFactory?, version : Int) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    companion object{
        private val DATABASE_NAME = "MyData.db"
        private val DATABASE_VERSION = 1

        val TRASACTION_TABLE_NAME = "Transactions"
        val COLUMN_TRANSACTIONID = "tansactionid"
        val COLUMN_TRANSACTIONNAME = "tansactionname"
        val COLUMN_TRANSACTIONDESCRIPTION = "tansactiondescription"
        val COLUMN_TRANSACTIONPRICE = "tansactionprice"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TRANSACTION_TABLE = "CREATE TABLE $TRASACTION_TABLE_NAME (" +
                "$COLUMN_TRANSACTIONID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TRANSACTIONNAME TEXT, " +
                "$COLUMN_TRANSACTIONDESCRIPTION TEXT, " +
                "$COLUMN_TRANSACTIONPRICE DOUBLE DEFAULT 0)"
        db?.execSQL(CREATE_TRANSACTION_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun getTransactions(context: Context) : ArrayList<ExampleItem> {
        val qry = "Select * From $TRASACTION_TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val transactions = ArrayList<ExampleItem>()

        if (cursor.count == 0)
            Toast.makeText(context, "No records found", Toast.LENGTH_SHORT).show() else{
            cursor.moveToFirst()
            while (!cursor.isAfterLast){
                val transaction = ExampleItem()
                transaction.itemNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTIONID))
                transaction.itemName = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSACTIONNAME))
                transaction.itemDescription = cursor.getString(cursor.getColumnIndex(
                    COLUMN_TRANSACTIONDESCRIPTION))
                transaction.itemPrice = cursor.getDouble(cursor.getColumnIndex(
                    COLUMN_TRANSACTIONPRICE))
                transactions.add(transaction)
                cursor.moveToNext()
            }
            Toast.makeText(context, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return transactions
    }

    fun addTransaction(context: Context, item: ExampleItem){
        val values = ContentValues()
        values.put(COLUMN_TRANSACTIONNAME, item.itemName)
        values.put(COLUMN_TRANSACTIONDESCRIPTION, item.itemDescription)
        values.put(COLUMN_TRANSACTIONPRICE, item.itemPrice)
        val db = this.writableDatabase
        try {
            db.insert(TRASACTION_TABLE_NAME, null, values)
            Toast.makeText(context, "Transaction Added!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteTransaction(itemNumber : Int): Boolean {
    val qry = "Delete From $TRASACTION_TABLE_NAME where $COLUMN_TRANSACTIONID = $itemNumber"
        val db = this.writableDatabase
        var result = false
        try {
            val cursor = db.execSQL(qry)
            result = true
            } catch (e : Exception){
            Log.e(ContentValues.TAG, "Error deleting")
        }
        db.close()
        return result
    }

    fun updateTransaction(id: String, transactionName: String, transactionDescription: String, transactionPrice: String) : Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        var result = false
        contentValues.put(COLUMN_TRANSACTIONNAME, transactionName)
        contentValues.put(COLUMN_TRANSACTIONDESCRIPTION, transactionDescription)
        contentValues.put(COLUMN_TRANSACTIONPRICE, transactionPrice.toDouble())
        try {
            db.update(TRASACTION_TABLE_NAME, contentValues, "$COLUMN_TRANSACTIONID = ?", arrayOf(id))
            result = true
        } catch (e: Exception){
            Log.e(ContentValues.TAG, "Error Updating")
            result = false
        }
        return result
    }

    fun returnSumPrice(): Double {
        val qry = "Select Sum($COLUMN_TRANSACTIONPRICE) From $TRASACTION_TABLE_NAME"
        val db = this.writableDatabase
        var result: Double = 0.0
        val cursor = db.rawQuery(qry, null)
        if (cursor.moveToFirst()){
            result = cursor.getDouble(0)
        }
        while (cursor.moveToNext())
            return result
        cursor.close()
        db.close()
    return result
    }
}