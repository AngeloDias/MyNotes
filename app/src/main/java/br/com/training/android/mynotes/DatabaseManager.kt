package br.com.training.android.mynotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DatabaseManager(context: Context) {

    companion object {
        const val DB_NAME = "MyNotes"
        const val DB_TABLE_NAME = "Notes"
        const val COL_ID = "ID"
        const val COL_TITLE = "Title"
        const val COL_DESC = "description"
        const val DB_VERSION = 1
        const val sqlCreateTable =
            "CREATE TABLE IF NOT EXISTS $DB_TABLE_NAME ($COL_ID INTEGER PRIMARY KEY, " +
                    "$COL_TITLE TEXT, $COL_DESC TEXT);"
    }

    private var sqlDB:SQLiteDatabase? = null

    init {
        val db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes(ctx: Context) :
        SQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION) {
        private var context: Context? = ctx

        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTable)
            Toast.makeText(context, " database was created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("DROP TABLE IF EXISTS $DB_TABLE_NAME")
        }

    }

    fun insertIntoDatabase(values: ContentValues): Long {
        return sqlDB!!.insert(DB_TABLE_NAME, "", values)
    }

    fun query(projection: Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor {
        val qBuilder = SQLiteQueryBuilder()
        val cursor = qBuilder.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder)

        qBuilder.tables = DB_TABLE_NAME

        return cursor
    }

}
