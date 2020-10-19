package md.merit.strangatorul

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        btn_add_item.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.resetApp -> {
                var alertDialog = android.app.AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("Are you sure you want to delete all data?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                        try {dbHandler.deleteAllData()

                        } catch (e:Exception){
                            Toast.makeText(this, "Unable to delete data", Toast.LENGTH_SHORT).show()
                        }
                        val intent = intent
                        finish()
                        startActivity(intent)
                            Toast.makeText(this, "All data was deleted", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .show()
            }
            R.id.aboutApp -> Toast.makeText(this, "aboutt what?", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        viewTransactions()
        super.onResume()
        edt_rest.setText(returnRest().toString())
    }

    private fun viewTransactions() {
        val transactionList = dbHandler.getTransactions(this)
        if(transactionList != null) {
            if (transactionList.size > 1) {
                val adapter = MyAdapter(this, transactionList.reversed() as ArrayList<Transaction>)
                val rv: RecyclerView = findViewById(R.id.recycler_view_main)
                rv.layoutManager = LinearLayoutManager(this)
                rv.adapter = adapter
            } else {
                val adapter = MyAdapter(this, transactionList)
                val rv: RecyclerView = findViewById(R.id.recycler_view_main)
                rv.layoutManager = LinearLayoutManager(this)
                rv.adapter = adapter
            }
        }

    }

    private fun returnRest(): Double {
        val sumTransactions = dbHandler.returnSumPrice()
        var totalMoney: Double
        if (edt_total.text.toString().equals(""))
            totalMoney = 0.0
        else
            totalMoney = edt_total.text.toString().toDouble()
        return totalMoney - sumTransactions
    }

    fun addMoney(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Please write your money amount!")
        val dialogLayout = inflater.inflate(R.layout.lo_add_money, null)
        dialogLayout.findViewById<EditText>(R.id.edt_total_money)

        builder.setView(dialogLayout)
        builder.setIcon(R.drawable.ic_baseline_attach_money_24)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            try {
                edt_total.text = dialogLayout.edt_total_money.text.toString()
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
        val insertedValue = edt_total.text.toString()

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
        edt_total.text = savedString
    }

}