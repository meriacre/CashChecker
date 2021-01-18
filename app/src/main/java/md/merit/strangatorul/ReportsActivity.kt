package md.merit.strangatorul

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_reports.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class ReportsActivity : AppCompatActivity() {
    private lateinit var settingsSaveData: SettingsSaveData
    private val STORAGE_CODE: Int = 100
    private lateinit var date1: String
    private lateinit var date2: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        settingsSaveData = SettingsSaveData(this)
        if (settingsSaveData.loadDarkMode() == true) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.title = "Reports"

        viewPDF()

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val correctMonthDefault = month + 1
        if (day < 10 && correctMonthDefault > 9) {
            tv_date1.setText("" + year + "-" + correctMonthDefault + "-" + "0" + day)
            date1 = tv_date1.text.toString()
        } else if (correctMonthDefault < 10 && day > 9) {
            tv_date1.setText("" + year + "-0" + correctMonthDefault + "-" + day)
            date1 = tv_date1.text.toString()
        } else if (day < 10 && correctMonthDefault < 10) {
            tv_date1.setText("" + year + "-0" + correctMonthDefault + "-0" + day)
            date1 = tv_date1.text.toString()
        } else {
            tv_date1.setText("" + year + "-" + correctMonthDefault + "-" + day)
            date1 = tv_date1.text.toString()
        }

        tv_date1.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                    var correctMonth = mMonth + 1
                    if (mDay < 10 && correctMonth > 9) {
                        tv_date1.setText("" + mYear + "-" + correctMonth + "-0" + mDay)
                        date1 = tv_date1.text.toString()
                    } else if (mDay > 10 && correctMonth < 10) {
                        tv_date1.setText("" + mYear + "-0" + correctMonth + "-" + mDay)
                        date1 = tv_date1.text.toString()
                    } else if (mDay < 10 && correctMonth < 10) {
                        tv_date1.setText("" + mYear + "-0" + correctMonth + "-0" + mDay)
                        date1 = tv_date1.text.toString()
                    } else {
                        tv_date1.setText("" + mYear + "-" + correctMonth + "-" + mDay)
                        date1 = tv_date1.text.toString()
                    }
                },
                year,
                month,
                day
            )
            dpd.show()
            dpd.datePicker.maxDate = System.currentTimeMillis()
        }

        if (day < 10 && correctMonthDefault > 9) {
            tv_date2.setText("" + year + "-" + correctMonthDefault + "-" + "0" + day)
            date2 = tv_date2.text.toString()
        } else if (correctMonthDefault < 10 && day > 9) {
            tv_date2.setText("" + year + "-0" + correctMonthDefault + "-" + day)
            date2 = tv_date2.text.toString()
        } else if (day < 10 && correctMonthDefault < 10) {
            tv_date2.setText("" + year + "-0" + correctMonthDefault + "-0" + day)
            date2 = tv_date2.text.toString()
        } else {
            tv_date2.setText("" + year + "-" + correctMonthDefault + "-" + day)
            date2 = tv_date2.text.toString()
        }

        tv_date2.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                    var correctMonth = mMonth + 1
                    if (mDay < 10 && correctMonth > 9) {
                        tv_date2.setText("" + mYear + "-" + correctMonth + "-0" + mDay)
                        date2 = tv_date2.text.toString()
                    } else if (mDay > 10 && correctMonth < 10) {
                        tv_date2.setText("" + mYear + "-0" + correctMonth + "-" + mDay)
                        date2 = tv_date2.text.toString()
                    } else if (mDay < 10 && correctMonth < 10) {
                        tv_date2.setText("" + mYear + "-0" + correctMonth + "-0" + mDay)
                        date2 = tv_date2.text.toString()
                    } else {
                        tv_date2.setText("" + mYear + "-" + correctMonth + "-" + mDay)
                        date2 = tv_date2.text.toString()
                    }
                },
                year,
                month,
                day
            )
            dpd.show()
            dpd.datePicker.maxDate = System.currentTimeMillis()
        }


        btn_add_report.setOnClickListener {
            val date1Compare = LocalDate.parse(date1, DateTimeFormatter.ISO_DATE)
            val date2Compare = LocalDate.parse(date2, DateTimeFormatter.ISO_DATE)
            if (date1Compare > date2Compare) {
                Toast.makeText(this, "Start Date can't be after End Date!!", Toast.LENGTH_LONG)
                    .show()
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                //System OS>Marshmallow
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission was not grated, request it
                    val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permission, STORAGE_CODE)
                } else {
                    //permission already granted call savePdf() method
                    savePdf()
                    val i = Intent(applicationContext, ReportsActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun savePdf() {
        //create object of Document Class
        val mDoc = Document()

        val mListTest = MainActivity.dbHandler.getTransactionsPdf(this, date1, date2)

        // pdf file name
        val mFileName = SimpleDateFormat(
            "yyyy-MM-dd_HH:mm:ss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        // pdf file path
        val mFilePath =
            this.getExternalFilesDir(null)?.absolutePath.toString() + "/" + mFileName + ".pdf"
        val table: PdfPTable = PdfPTable(floatArrayOf(3f, 3f, 3f, 3f, 3f))
        table.defaultCell.horizontalAlignment = Element.ALIGN_CENTER
        table.defaultCell.fixedHeight = 50f
        table.totalWidth = PageSize.A4.width
        table.widthPercentage = 100f
        table.defaultCell.verticalAlignment = Element.ALIGN_MIDDLE
        table.addCell("Invoice ID")
        table.addCell("Title")
        table.addCell("Description")
        table.addCell("Date")
        table.addCell("Price")
        table.headerRows = 1
        val cells = table.getRow(0).cells
        for (element in cells) {
            element.backgroundColor = BaseColor.GRAY
        }
        // Sum of total transactions on invoice
        var totalSpend: Double = 0.0
        // Adding transactions from dbHandler.getTransactionsPdf-ArrayOfTransactionObjects to the invoice table
        for (i in 0 until mListTest.size) {
            table.addCell(mListTest[i].itemId.toString())
            table.addCell(mListTest[i].itemTitle)
            table.addCell(mListTest[i].itemDescription)
            table.addCell(mListTest[i].itemDate)
            table.addCell(mListTest[i].itemPrice.toString())
            totalSpend += mListTest[i].itemPrice!!
        }
        try {
            // Create instance of PDF writer class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            //open document for writing
            mDoc.open()

            // get text from date textviews
            val mText = tv_date1.text.toString()
            val mText2 = tv_date2.text.toString()
            //val mText3 = mListTest[1].itemTitle

            //add author of the document(metadata)
            mDoc.addAuthor("Cash Checker")

            val f = Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.NORMAL, BaseColor.ORANGE)
            val g = Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.NORMAL, BaseColor.BLACK)

            //add paragraphs and table to document
            var paragraph = Paragraph("Cash Checker\n", f)
            paragraph.alignment = Element.ALIGN_CENTER
            mDoc.add(paragraph)
            paragraph = Paragraph("Invoice\n\n", g)
            paragraph.alignment = Element.ALIGN_CENTER
            mDoc.add(paragraph)
            mDoc.add(table)
            paragraph = Paragraph("From: " + mText + " to: " + mText2 + " you spend:")
            paragraph.alignment = Element.ALIGN_RIGHT
            mDoc.add(paragraph)
            paragraph = Paragraph("Total: " + totalSpend.toString() + MainActivity.currencySign)
            paragraph.alignment = Element.ALIGN_RIGHT
            mDoc.add(paragraph)

            mDoc.close()

            // show file saved message with name and path
            //  Toast.makeText(this, "$mFileName.pdf\nis saved to\n$mFilePath", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from pop-up was granted, call savePdf() method
                    savePdf()
                } else {
                    //permision from popup was denied, show error message
                    Toast.makeText(this, "Permision denied..!", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun viewPDF() {
        val pdfListaMea = ArrayList<PdfItems>()
        var mListofFiles: File = File(this.getExternalFilesDir(null)?.absolutePath.toString() + "/")
        var f: File = File(mListofFiles.toString())
        var file: Array<File>? = f.listFiles().sortedArrayDescending()
        if (file != null) {
            for (element in file) {
                val pdfItem = PdfItems(fName = element.name.toString())
                pdfListaMea.add(pdfItem)
            }
        } else {
            Toast.makeText(this, "No reports found!!", Toast.LENGTH_SHORT).show()
        }
        //val pdfListaMea2 = pdfListaMea.sortByDescending { pdfItems.date  }
        val adapter = PdfAdapter(this, pdfListaMea)
        val rv: RecyclerView = findViewById(R.id.rv_pdf)
        rv.layoutManager = GridLayoutManager(this, 2)
        rv.adapter = adapter

    }
}