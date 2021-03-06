package br.com.training.android.mynotes

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
    private var listNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listNotes.add(Note(1, "Meeting with professor", "To discuss the goals of the work"))
        listNotes.add(Note(2, "Meeting with psychologist", "To talk about myself and my life"))
        listNotes.add(Note(3, "Meeting with personal trainer", "To discuss the way to become a real body builder"))

        loadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        loadQuery("%")
    }

    private fun loadQuery(title: String) {
        val dbManager = DatabaseManager(this)
        val projections = arrayOf(DatabaseManager.COL_ID, DatabaseManager.COL_TITLE, DatabaseManager.COL_DESC)
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.query(projections, DatabaseManager.COL_TITLE + " LIKE ?",
            selectionArgs, DatabaseManager.COL_TITLE)

        listNotes.clear()

        if(cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(DatabaseManager.COL_ID))
                val colTitle = cursor.getString(cursor.getColumnIndex(DatabaseManager.COL_TITLE))
                val desc = cursor.getString(cursor.getColumnIndex(DatabaseManager.COL_DESC))

                listNotes.add(Note(id, colTitle, desc))
            } while (cursor.moveToNext())

            listViewNotes.adapter = MyNotesAdapter(listNotes, this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Toast.makeText(applicationContext, p0!!, Toast.LENGTH_LONG).show()
                loadQuery("% %$p0 %")

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                TODO("Not yet implemented")
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.addNote -> {
                val intent = Intent(this, AddNotesActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    inner class MyNotesAdapter(notes: ArrayList<Note>, ctx: Context) : BaseAdapter() {
        var listNotes = notes
        var context = ctx

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val myView = layoutInflater.inflate(R.layout.ticket, null)
            val myNote = listNotes[p0]

            myView.txtViewTitle.text = myNote.nodeName
            myView.txtViewContent.text = myNote.nodeDesc
            myView.imgViewIconDelete.setOnClickListener {
                val dbManager = DatabaseManager(context)
                val selectionArgs = arrayOf(myNote.nodeID.toString())

                dbManager.delete("ID=?", selectionArgs)
                loadQuery("%")
            }
            myView.imgViewIconEdit.setOnClickListener {
                val intent = Intent(applicationContext, AddNotesActivity::class.java)

                intent.putExtra("ID", myNote.nodeID)
                intent.putExtra("name", myNote.nodeName)
                intent.putExtra("desc", myNote.nodeDesc)
                startActivity(intent)
            }

            return myView
        }

        override fun getItem(p0: Int): Any {
            return this.listNotes[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return this.listNotes.size
        }

    }

}
