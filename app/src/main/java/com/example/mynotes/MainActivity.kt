package com.example.mynotes
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var notes:MutableList<NoteData>
    lateinit var recyclerView:RecyclerView
    lateinit var noteAdapter:NoteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val notes = mutableListOf<NoteData>()
        val files : Array<String> = this.fileList()
        for (ftitle in files) {
            if (ftitle!="profileInstalled") {
                val title: String = ftitle
                val description: String = this.openFileInput(title).bufferedReader().readText()
                notes.add(NoteData(title = title, description = description))
            }
        }
        val noteAdapter = NoteAdapter(notes,this)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=noteAdapter
        noteAdapter.setOnClickListener(object : NoteAdapter.OnClickListener{
            override fun onItemClick(item: NoteData) {

                val builder : AlertDialog = AlertDialog.Builder(this@MainActivity)
                    .create()
                val view = layoutInflater.inflate(R.layout.edit_dialog_layout,null)
                val  cancelbtn = view.findViewById<Button>(R.id.icancel)
                val savebtn= view.findViewById<Button>(R.id.isave)
                val delbtn= view.findViewById<ImageButton>(R.id.delbtn)
                val edittitle = view.findViewById<EditText>(R.id.edit_title)
                edittitle.setText(item.title)
                val editdescription = view.findViewById<EditText>(R.id.edit_description)
                editdescription.setText(item.description)

                builder.setView(view)
                savebtn.setOnClickListener {
                    editnote(notes,view,item,recyclerView,noteAdapter)
                    Toast.makeText(this@MainActivity, "Note Saved", Toast.LENGTH_SHORT).show()
                    builder.dismiss()
                }
                delbtn.setOnClickListener {
                    val builder1  = AlertDialog.Builder(this@MainActivity)
                    builder1.setTitle("Are You sure?")
                    builder1.setMessage("Delete note "+item.title)
                    builder1.setPositiveButton(android.R.string.yes) { dialog, which ->
                        val dtitle = item.title
                        this@MainActivity.deleteFile(dtitle)
                        notes.remove(item)
                        recyclerView.swapAdapter(noteAdapter, false)
                        builder.dismiss()
                        Toast.makeText(applicationContext,
                            "Deleted", Toast.LENGTH_SHORT).show()
                    }
                    builder1.setNeutralButton(android.R.string.cancel){dialog, which ->
                        
                    }
                    builder1.show()

                }
                cancelbtn.setOnClickListener {
                    builder.dismiss()
                }
                builder.setCanceledOnTouchOutside(false)
                builder.show()
            }
        })

        val addbtn : ImageButton = findViewById(R.id.addbtn)

        addbtn.setOnClickListener {
            showdialog(notes,recyclerView,noteAdapter)
        }

    }
    private fun showdialog( notes: MutableList<NoteData>,recyclerView: RecyclerView,noteAdapter: NoteAdapter){
        val builder : AlertDialog = AlertDialog.Builder(this)
            .create()
        val view = layoutInflater.inflate(R.layout.add_dialog_layout,null)
        val  cancelbtn = view.findViewById<Button>(R.id.icancel)
        val savebtn= view.findViewById<Button>(R.id.isave)
        builder.setView(view)
        savebtn.setOnClickListener {
            savenote(notes,view,recyclerView, noteAdapter)
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
            builder.dismiss()
        }
        cancelbtn.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()

    }
    fun savenote(notes: MutableList<NoteData>, view: View,recyclerView: RecyclerView, noteAdapter : NoteAdapter) {
        val edittitle = view.findViewById<EditText>(R.id.edit_title)
        val ititle = edittitle.text.toString()
        val editdescription = view.findViewById<EditText>(R.id.edit_description)
        val idescription = editdescription.text.toString()
        val note = NoteData(title = ititle , description = idescription)
        notes.add(index = 0, note )
        val filename = ititle
        this.openFileOutput(filename, MODE_PRIVATE).use {
            it.write(idescription.toByteArray())
        }
        recyclerView.swapAdapter(noteAdapter, false)
    }


    fun editnote(notes: MutableList<NoteData>, view: View,item: NoteData,recyclerView: RecyclerView, noteAdapter : NoteAdapter) {
        val dtitle = item.title
        this.deleteFile(dtitle)
        notes.remove(item)
        val edittitle = view.findViewById<EditText>(R.id.edit_title)
        val ititle = edittitle.text.toString()
        val editdescription = view.findViewById<EditText>(R.id.edit_description)
        val idescription = editdescription.text.toString()
        val note = NoteData(title = ititle , description = idescription)
        notes.add(index = 0, note )
        val filename = ititle
        this.openFileOutput(filename, MODE_PRIVATE).use {
            it.write(idescription.toByteArray())
        }
        recyclerView.swapAdapter(noteAdapter, false)
    }


}