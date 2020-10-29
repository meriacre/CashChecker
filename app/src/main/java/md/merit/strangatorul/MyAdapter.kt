package md.merit.strangatorul

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.example_item.view.*
import kotlinx.android.synthetic.main.lo_transaction_update.view.*
import java.util.*

class MyAdapter(context: Context, private val exampleList: ArrayList<Transaction>) :
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
        holder.itemName.text = curentItem.itemTitle
        holder.itemPrice.text = curentItem.itemPrice.toString()
        holder.itemDate.text = curentItem.itemDate.toString()

        holder.itemView.setOnClickListener {
            val gItem = exampleList.get(position)
            var gTitle = gItem.itemTitle
            var gDescription = gItem.itemDescription
            var gPrice = gItem.itemPrice
            var gDate = gItem.itemDate

            val intent = Intent(context, DisplayActivity::class.java)
            intent.putExtra("iTitle", gTitle)
            intent.putExtra("iDesc", gDescription)
            intent.putExtra("iPrice", gPrice)
            intent.putExtra("iDate", gDate)

            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            val itemName = curentItem.itemTitle

            var alertDialog = AlertDialog.Builder(context, R.style.MyDialogTheme)
                .setTitle("Warning")
                .setMessage("Are you sure you want to delete $itemName")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (MainActivity.dbHandler.deleteTransaction(curentItem.itemId)) {
                        exampleList.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, exampleList.size)
                        Toast.makeText(context, "Transaction $itemName deleted", Toast.LENGTH_SHORT)
                            .show()
                    } else

                        Toast.makeText(context, "Error deleting", Toast.LENGTH_SHORT).show()
                    val i1 = Intent(context, MainActivity::class.java)
                    context.startActivity(i1)
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                .setIcon(R.drawable.ic_baseline_warning_24)
                .show()

        }

        holder.btnEdit.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.lo_transaction_update, null)

            val txtName = view.findViewById<TextView>(R.id.edt_name_update)
            val txtDescription = view.findViewById<TextView>(R.id.edt_description_update)
            val txtPrice = view.findViewById<TextView>(R.id.edt_price_update)
            val txtDate = view.findViewById<TextView>(R.id.edt_date_update)

            txtName.text = curentItem.itemTitle
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
                    view.edt_date_update.setText(""+ mDay +"/" + correctMonth + "/" + mYear)
                }, year, month, day)
                dpd.show()
            }

            val builder = AlertDialog.Builder(context,R.style.MyDialogTheme)
                .setTitle("Update transaction info")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->
                    val isUpdate = MainActivity.dbHandler.updateTransaction(
                        curentItem.itemId.toString(),
                        view.edt_name_update.text.toString(),
                        view.edt_description_update.text.toString(),
                        view.edt_price_update.text.toString(),
                        view.edt_date_update.text.toString()
                    )
                    if (isUpdate == true) {
                        exampleList[position].itemTitle = view.edt_name_update.text.toString()
                        exampleList[position].itemDescription = view.edt_description_update.text.toString()
                        exampleList[position].itemPrice = view.edt_price_update.text.toString().toDouble()
                        exampleList[position].itemDate = view.edt_date_update.text.toString()
                        notifyDataSetChanged()
                        Toast.makeText(context, "Updated succesfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error updating", Toast.LENGTH_SHORT).show()
                    }
                    val i1 = Intent(context, MainActivity::class.java)
                    context.startActivity(i1)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

                })
            var alert = builder.create()
            alert.show()
        }

    }

    override fun getItemCount() = exampleList.size


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNumber: TextView = itemView.tv_id
        val itemName: TextView = itemView.tv_transaction_name
        val itemPrice: TextView = itemView.tv_price
        val itemDate: TextView = itemView.tv_date

        val btnEdit: ImageButton = itemView.btn_edit
        val btnDelete: ImageButton = itemView.btn_delete
    }

}