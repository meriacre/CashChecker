package md.merit.strangatorul

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_item.*
import kotlinx.android.synthetic.main.example_item.view.*
import kotlinx.android.synthetic.main.lo_transaction_update.view.*
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(context: Context, private val exampleList: ArrayList<ExampleItem>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.example_item, parent, false)
        return MyViewHolder(item)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val curentItem = exampleList[position]
        val n = position + 1
        holder.itemNumber.text = n.toString()
        holder.itemName.text = curentItem.itemName
        holder.itemPrice.text = curentItem.itemPrice.toString()
        holder.itemDate.text = curentItem.itemDate.toString()

        holder.itemView.setOnClickListener {
            val exampleItem = exampleList.get(position)
            var gTitle = exampleItem.itemName
            var gDescription = exampleItem.itemDescription
            var gPrice = exampleItem.itemPrice
            var gDate = exampleItem.itemDate

            val intent = Intent(context, DisplayActivity::class.java)
            intent.putExtra("iTitle", gTitle)
            intent.putExtra("iDesc", gDescription)
            intent.putExtra("iPrice", gPrice)
            intent.putExtra("iDate", gDate)

            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            val itemName = curentItem.itemName

            var alertDialog = AlertDialog.Builder(context)
                .setTitle("Warning")
                .setMessage("Are you sure you want to delete $itemName")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (MainActivity.dbHandler.deleteTransaction(curentItem.itemNumber)) {
                        exampleList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, exampleList.size)
                        Toast.makeText(context, "Transaction $itemName deleted", Toast.LENGTH_SHORT)
                            .show()
                    } else

                        Toast.makeText(context, "Error deleting", Toast.LENGTH_SHORT).show()
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                .setIcon(R.drawable.ic_baseline_warning_24)
                .show()

        }

        holder.btnEdit.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.lo_transaction_update, null)

            val txtName = view.findViewById<TextView>(R.id.edtName2)
            val txtDescription = view.findViewById<TextView>(R.id.edtDescription2)
            val txtPrice = view.findViewById<TextView>(R.id.edtPrice2)
            val txtDate = view.findViewById<TextView>(R.id.edt_DateUpdate)

            txtName.text = curentItem.itemName
            txtDescription.text = curentItem.itemDescription
            txtPrice.text = curentItem.itemPrice.toString()
            txtDate.text = curentItem.itemDate



            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            view.btn_calendarEdit.setOnClickListener {
                val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                    var correctMonth = mMonth + 1
                    view.edt_DateUpdate.setText(""+ mDay +"/" + correctMonth + "/" + mYear)
                }, year, month, day)
                dpd.show()
            }
            val builder = AlertDialog.Builder(context)
                .setTitle("Update transaction info")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->
                    val isUpdate = MainActivity.dbHandler.updateTransaction(
                        curentItem.itemNumber.toString(),
                        view.edtName2.text.toString(),
                        view.edtDescription2.text.toString(),
                        view.edtPrice2.text.toString(),
                        view.edt_DateUpdate.text.toString()
                    )
                    if (isUpdate == true) {
                        exampleList[position].itemName = view.edtName2.text.toString()
                        exampleList[position].itemDescription = view.edtDescription2.text.toString()
                        exampleList[position].itemPrice = view.edtPrice2.text.toString().toDouble()
                        exampleList[position].itemDate = view.edt_DateUpdate.text.toString()
                        notifyDataSetChanged()
                        Toast.makeText(context, "Updated succesfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error updating", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

                })
            var alert = builder.create()
            alert.show()
        }

    }

    override fun getItemCount() = exampleList.size


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNumber: TextView = itemView.indexNumber
        val itemName: TextView = itemView.name
        val itemPrice: TextView = itemView.price
        val itemDate: TextView = itemView.date

        val btnEdit = itemView.btnEdit
        val btnDelete = itemView.btnDelete
    }

}