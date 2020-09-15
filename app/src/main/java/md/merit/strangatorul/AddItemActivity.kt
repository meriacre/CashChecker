package md.merit.strangatorul

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_item.*
import java.util.*

class AddItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        btnCalendar.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                var correctMonth = mMonth + 1
                tv_addDate.setText(""+ mDay +"/" + correctMonth + "/" + mYear)
            }, year, month, day)
            dpd.show()
        }

        btnSave.setOnClickListener{
            if(edtDenumirea.text.isEmpty()){
                Toast.makeText(this, "Please enter transaction name!", Toast.LENGTH_SHORT).show()
                edtDenumirea.requestFocus()
            } else{
                val transaction = ExampleItem()
                transaction.itemName = edtDenumirea.text.toString()
                transaction.itemDate =tv_addDate.text.toString()
                if(edtDetails.text.toString().isEmpty()){
                    transaction.itemDescription = ""
                } else{
                    transaction.itemDescription = edtDetails.text.toString()
                    if(edtPrice.text.isEmpty())
                    {
                        transaction.itemPrice = 0.0
                    } else{
                        transaction.itemPrice = edtPrice.text.toString().toDouble()
                    }
                }
                MainActivity.dbHandler.addTransaction(this,transaction)
                clearEdits()
                edtDenumirea.requestFocus()
            }
        }

        btnCancel.setOnClickListener {
            clearEdits()
            finish()
        }

    }


    private fun clearEdits(){
        edtDenumirea.text.clear()
        edtDetails.text.clear()
        edtPrice.text.clear()
    }


}