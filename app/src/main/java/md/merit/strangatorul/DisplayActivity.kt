package md.merit.strangatorul

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_display.*

class DisplayActivity : AppCompatActivity() {
    private lateinit var settingsSaveData: SettingsSaveData

    override fun onCreate(savedInstanceState: Bundle?) {
        settingsSaveData = SettingsSaveData(this)
        if (settingsSaveData.loadDarkMode() == true) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setDisplayShowHomeEnabled(true)

        var intent = intent
        var aTitle = intent.getStringExtra("iTitle")
        var aDescription = intent.getStringExtra("iDesc")
        var aPrice = intent.getDoubleExtra("iPrice", 0.0)
        var aDate = intent.getStringExtra("iDate")

        actionBar.setTitle(aTitle)

        tv_name_display.text = aTitle
        tv_description_display.text = aDescription
        tv_price_display.text = aPrice.toString()
        tv_date_display.text = aDate.toString()
        tv_currency_display.text = MainActivity.currencySign
    }

}