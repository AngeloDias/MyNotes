package br.com.training.android.mynotes

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotesActivity : AppCompatActivity() {
    private var id = 0
    private val dbManager = DatabaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try {
            val bundle: Bundle = intent.extras!!
            id = bundle.getInt("ID")

            editTextTitle.setText(bundle.getString("name"))
            editTxtDesc.setText(bundle.getString("desc"))
        } catch (e: Exception){}
    }

    fun btnAdd(view: View) {
        val values = ContentValues()

        values.put(DatabaseManager.COL_TITLE, editTextTitle.text.toString())
        values.put(DatabaseManager.COL_DESC, editTxtDesc.text.toString())

        val tempID : Long?

        if (id==0) {
            tempID = dbManager.insertIntoDatabase(values)

        } else {
            val selectionArgs = arrayOf(id.toString())
            tempID = dbManager.update(values, "ID=?", selectionArgs).toLong()
        }

        if (tempID > 0) {
            Toast.makeText(this, " note was added", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "can't add note", Toast.LENGTH_LONG).show()
        }
    }

}
