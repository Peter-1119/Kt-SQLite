package com.example.kt_sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private var adapter: ArrayAdapter<String>? = null
    private val items = ArrayList<String>()
    private var dbrw: SQLiteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter
        dbrw = MyDBHelper(this).writableDatabase
        btn_query.setOnClickListener(View.OnClickListener {
            val c: Cursor = if (ed_book.length() < 1) dbrw!!.rawQuery("SELECT * FROM myTable", null) else dbrw!!.rawQuery("SELECT * FROM myTable WHERE book LIKE '" + ed_book.text.toString() + "'", null)
            c.moveToFirst()
            items.clear()
            Toast.makeText(this@MainActivity, "共有" + c.count + "筆資料", Toast.LENGTH_SHORT).show()
            for (i in 0 until c.count) {
                items.add("書名" + c.getString(0) + "\t\t\t\t價格:" + c.getString(1))
                c.moveToNext()
            }
            adapter!!.notifyDataSetChanged()
            c.close()
        })
        btn_insert.setOnClickListener(View.OnClickListener {
            if (ed_book.length() < 1 || ed_price.length() < 1) Toast.makeText(this@MainActivity, "欄位請勿留空", Toast.LENGTH_SHORT).show() else {
                try {
                    dbrw!!.execSQL("INSERT INTO myTable(book, price) VALUES(?,?)", arrayOf<Any>(ed_book.text.toString(),
                            ed_price.text.toString()))
                    Toast.makeText(this@MainActivity, "新增書名" + ed_book.text.toString() + "     價格" + ed_price.text.toString(), Toast.LENGTH_SHORT).show()
                    ed_book.setText("")
                    ed_price.setText("")
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "新增失敗$e", Toast.LENGTH_LONG).show()
                }
            }
        })
        btn_update.setOnClickListener(View.OnClickListener {
            if (ed_book.length() < 1 || ed_price.length() < 1) Toast.makeText(this@MainActivity, "欄位請勿留空", Toast.LENGTH_SHORT).show() else {
                try {
                    dbrw!!.execSQL("UPDATE myTable SET price = " + ed_price.text.toString() + " WHERE book LIKE '" + ed_book.text.toString() + "'")
                    Toast.makeText(this@MainActivity, "更新書名" + ed_book.text.toString() + "     價格" + ed_price.text.toString(), Toast.LENGTH_SHORT).show()
                    ed_book.setText("")
                    ed_price.setText("")
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "更新失敗$e", Toast.LENGTH_LONG).show()
                }
            }
        })
        btn_delete.setOnClickListener(View.OnClickListener {
            if (ed_book.length() < 1) Toast.makeText(this@MainActivity, "書名請勿留空", Toast.LENGTH_SHORT).show() else {
                try {
                    dbrw!!.execSQL("DELETE FROM myTable WHERE book LIKE '" + ed_book.text.toString() + "'")
                    Toast.makeText(this@MainActivity, "刪除書名" + ed_book.text.toString(), Toast.LENGTH_SHORT).show()
                    ed_book.setText("")
                    ed_price.setText("")
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "刪除失敗:$e", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    public override fun onDestroy() {
        super.onDestroy()
        dbrw!!.close()
    }
}