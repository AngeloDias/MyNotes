package br.com.training.android.mynotes

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
    }

    fun btnAdd(view: View) {
        val dbManager = DatabaseManager(this)
        val values = ContentValues()

        values.put(DatabaseManager.COL_TITLE, editTextTitle.text.toString())
        values.put(DatabaseManager.COL_DESC, editTxtDesc.text.toString())

        val id = dbManager.insertIntoDatabase(values)

        if(id > 0) {
            Toast.makeText(this, " note was added", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "can't add note", Toast.LENGTH_LONG).show()
        }
    }

}
