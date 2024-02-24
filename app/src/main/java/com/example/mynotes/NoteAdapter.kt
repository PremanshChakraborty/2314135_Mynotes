package com.example.mynotes
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat

class NoteAdapter(private val notes:MutableList<NoteData>,private val context: Context):
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var listener: OnClickListener? = null
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title:TextView=itemView.findViewById(R.id.titleview)
        val del : ImageButton=itemView.findViewById(R.id.delbtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.recycler_view_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note=notes[position]
        holder.title.text=note.title
        holder.itemView.setOnClickListener {
            listener?.onItemClick(note)
        }
        holder.del.setOnClickListener {
            val builder1  = AlertDialog.Builder(context)
            builder1.setTitle("Are You sure?")
            builder1.setMessage("Delete note "+note.title)
            builder1.setPositiveButton(android.R.string.yes) { dialog, which ->
                val dtitle = note.title
                context.deleteFile(dtitle)
                notes.remove(note)
                notifyDataSetChanged()
                Toast.makeText(context,
                    "Deleted", Toast.LENGTH_SHORT).show()
            }
            builder1.setNeutralButton(android.R.string.cancel){dialog, which ->

            }
            builder1.show()

        }

    }
    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }


    interface OnClickListener {
        fun onItemClick(item: NoteData,)
    }


}


