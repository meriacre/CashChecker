package md.merit.strangatorul

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var dbHandler: DBHandler
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DBHandler(this, null, null, 1)

        viewTransactions()


        addItem.setOnClickListener{
    val intent = Intent(this, AddItemActivity::class.java)
    startActivity(intent)
}
    }

    override fun onResume() {
        viewTransactions()
        super.onResume()
        edtRest.setText(returnRest().toString())
    }

private fun viewTransactions(){
    val transactionList = dbHandler.getTransactions(this)
    val adapter = MyAdapter(this,transactionList)
    val rv : RecyclerView = findViewById(R.id.recycler_view)
    rv.layoutManager = LinearLayoutManager(this)
    rv.adapter = adapter
}

    private fun returnRest(): Double {
        val sumTransactions = dbHandler.returnSumPrice()
        var totalMoney: Double
        if(edtTotal.text.toString().equals(""))
            totalMoney =0.0
        else
            totalMoney = edtTotal.text.toString().toDouble()
        var rest = totalMoney - sumTransactions
        return rest
    }

}