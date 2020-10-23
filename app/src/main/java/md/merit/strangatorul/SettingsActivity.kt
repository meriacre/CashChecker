package md.merit.strangatorul

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.add_item.*

class SettingsActivity : AppCompatActivity() {
    companion object {
        lateinit var finalCurency:String

        fun isInitialized(): Boolean {
            if (::finalCurency.isInitialized) {
                return true}
                else{
                return false}

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        lateinit var optionCurency : Spinner
        loadData()

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.title = "Settings"

        optionCurency = findViewById(R.id.spinner_currency)
        val optionsCurency = arrayOf("$","€","MDL", "RON")
        optionCurency.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,optionsCurency)

        var val1=0
        if(isInitialized()){
        when (finalCurency) {
            optionsCurency[1] -> {
                val1 = 1
            }
            optionsCurency[2] -> {
                val1 = 2
            }
            optionsCurency[3] -> {
                val1 = 3
            }
        }}

        optionCurency.setSelection(val1)
        optionCurency.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                finalCurency = optionsCurency[p2]
                saveData()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }

    private fun saveData(){
        val insertedValue = finalCurency

        val sharedPref = getSharedPreferences("sharedPrefCurrency", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply(){
            putString("STRING_KEY2", insertedValue)
        }.apply()

        Toast.makeText(this, "Your currency is $finalCurency!", Toast.LENGTH_SHORT).show()
    }

     private fun loadData() {
        val sharedPref = getSharedPreferences("sharedPrefCurrency", Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("STRING_KEY2",null)
        if (savedString != null) {
            finalCurency = savedString
        }
    }

}