package md.merit.strangatorul

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lo_add_money.view.*

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHandler: DBHandler
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DBHandler(this, null, null, 1)
        viewTransactions()
       loadData()

        addItem.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onResume() {
        viewTransactions()
        super.onResume()
        edtRest.setText(returnRest().toString())
    }

    private fun viewTransactions() {
        val transactionList = dbHandler.getTransactions(this)
        transactionList
        val adapter = MyAdapter(this, transactionList.reversed() as ArrayList<ExampleItem>)
        val rv: RecyclerView = findViewById(R.id.recycler_view)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

    private fun returnRest(): Double {
        val sumTransactions = dbHandler.returnSumPrice()
        var totalMoney: Double
        if (edtTotal.text.toString().equals(""))
            totalMoney = 0.0
        else
            totalMoney = edtTotal.text.toString().toDouble()
        return totalMoney - sumTransactions
    }

    fun addMoney(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Please write your money amount!")
        val dialogLayout = inflater.inflate(R.layout.lo_add_money, null)
        dialogLayout.findViewById<EditText>(R.id.edtTotalMoney)

        builder.setView(dialogLayout)
        builder.setIcon(R.drawable.ic_baseline_attach_money_24)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            try {
                edtTotal.text = dialogLayout.edtTotalMoney.text.toString()
                saveData()
                val intent = intent
                finish()
                startActivity(intent)
            }catch (e:Exception){
                Toast.makeText(this, "Data can not be saved!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel") { dialogInterface, i ->

        }
        builder.show()
    }

    private fun saveData(){
        val insertedValue = edtTotal.text.toString()

        val sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply(){
            putString("STRING_KEY", insertedValue)
        }.apply()

        Toast.makeText(this, "Your amount of money are saved!", Toast.LENGTH_SHORT).show()
    }

    private fun loadData(){
        val sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("STRING_KEY",null)
        edtTotal.text = savedString
    }

}