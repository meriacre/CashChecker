package md.merit.strangatorul

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_item.*

class AddItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item)

        btnSave.setOnClickListener{
            if(edtDenumirea.text.isEmpty()){
                Toast.makeText(this, "Please enter transaction name!", Toast.LENGTH_SHORT).show()
                edtDenumirea.requestFocus()
            } else{
                val transaction = ExampleItem()
                transaction.itemName = edtDenumirea.text.toString()
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