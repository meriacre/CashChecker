package md.merit.strangatorul

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_pdf.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PdfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.title = "Pdf Report"


        val intent = intent
        val aName = intent.getStringExtra("iName")

        pdfViewer.fromStream(FileInputStream(this.getExternalFilesDir(null)?.absolutePath.toString() + "/" + "$aName"))
            .load()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_report -> {
                val aName = intent.getStringExtra("iName")
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.putExtra(
                    Intent.EXTRA_STREAM,
                    uriFromFile(
                        this,
                        File(this.getExternalFilesDir(null)?.absolutePath.toString(), "$aName")
                    )
                )
                shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                shareIntent.type = "application/pdf"
                startActivity(Intent.createChooser(shareIntent, "share.."))
            }
            R.id.delete_report -> {
                val aName = intent.getStringExtra("iName")

                var alertDialog = AlertDialog.Builder(this, R.style.MyDialogTheme)
                    .setTitle("Warning")
                    .setMessage("Are you sure you want to delete $aName")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                        val file =
                            File(this.getExternalFilesDir(null)?.absolutePath.toString(), "$aName")
                        if (file.exists()) {
                            file.delete()
                            val i = Intent(applicationContext, ReportsActivity::class.java)
                            startActivity(i)
                            finish()
                        } else {
                            Toast.makeText(this, "File doesn't exist!!", Toast.LENGTH_SHORT).show()
                        }
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                    .setIcon(R.drawable.ic_baseline_warning_24)
                    .show()


            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun uriFromFile(context: Context, file: File): Uri {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
        } else {
            return Uri.fromFile(file)
        }
    }
}