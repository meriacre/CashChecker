package md.merit.strangatorul

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.add_item.*

class SettingsActivity : AppCompatActivity() {
    companion object {
        lateinit var finalCurency: String

        private lateinit var settingSaveData: SettingsSaveData
        fun isInitialized(): Boolean {
            return ::finalCurency.isInitialized
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        settingSaveData = SettingsSaveData(this)
        if (settingSaveData.loadDarkMode() == true) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.title = "Settings"

        loadData()

        if (settingSaveData.loadDarkMode() == true) {
            switch_theme.isChecked = true
        }

        switch_theme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                settingSaveData.setDarkMode(true)
                restartApplication()
            } else {
                settingSaveData.setDarkMode(false)
                restartApplication()
            }
        }

        var optionCurency: Spinner = findViewById(R.id.spinner_currency)
        val optionsCurency = arrayOf("$", "€", "MDL", "RON")
        optionCurency.adapter = ArrayAdapter<String>(this, R.layout.spinner_item, optionsCurency)


        var val1 = 0
        if (isInitialized()) {
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
            }
        }

        optionCurency.setSelection(val1)
        optionCurency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                finalCurency = optionsCurency[p2]
                saveData()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    fun saveData() {
        val insertedValue = finalCurency
        val sharedPref = getSharedPreferences("sharedPrefCurrency", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.apply() {
            putString("STRING_KEY2", insertedValue)
        }.apply()

        //   Toast.makeText(this, "Your currency is $finalCurency!", Toast.LENGTH_SHORT).show()
    }

    private fun loadData() {
        val sharedPref = getSharedPreferences("sharedPrefCurrency", Context.MODE_PRIVATE)
        val savedString = sharedPref.getString("STRING_KEY2", null)
        finalCurency = savedString.toString()
    }

    private fun restartApplication() {
        val i = Intent(applicationContext, SettingsActivity::class.java)
        startActivity(i)
        finish()
    }

}