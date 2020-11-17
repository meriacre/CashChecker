package md.merit.strangatorul

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.example_pdf.view.*
import java.util.ArrayList

class PdfAdapter(context: Context, private val pdfList: ArrayList<PdfItems>) :
    RecyclerView.Adapter<PdfAdapter.MyViewHolder>() {

    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val pdfitem =
            LayoutInflater.from(parent.context).inflate(R.layout.example_pdf, parent, false)
        return MyViewHolder(pdfitem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val curentItem = pdfList[position]
        val hourPdf = curentItem.fName?.substring(11, 19)
        val datePdf = curentItem.fName?.substring(0, 10)
        holder.pdfDate.text = datePdf
        holder.pdfHour.text = hourPdf

        holder.itemView.setOnClickListener {
            val gitem = pdfList.get(position)
            val gName = gitem.fName

            val intent = Intent(context, PdfActivity::class.java)
            intent.putExtra("iName", gName)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = pdfList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pdfDate: TextView = itemView.tv_pdf_date
        val pdfHour: TextView = itemView.tv_pdf_hour
    }
}

