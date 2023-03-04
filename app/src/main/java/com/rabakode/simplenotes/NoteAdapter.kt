package com.rabakode.simplenotes

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView

class NoteAdapter(private val noteClickedInterface: NoteClickInterface): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var  listNote = ArrayList<ModelNote>()


    inner class NoteViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvNote: TextView = itemView.findViewById(R.id.tvNote)
        var ivNote: RoundedImageView = itemView.findViewById(R.id.ivNote)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val modelListNotes = listNote[position]
        holder.tvTitle.text = modelListNotes.title
        holder.tvNote.text = modelListNotes.note
        if(modelListNotes.imagePath != null){
            holder.ivNote.setImageBitmap(BitmapFactory.decodeFile(modelListNotes.imagePath))
            holder.ivNote.visibility = View.VISIBLE
        }
        else{
            holder.ivNote.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            noteClickedInterface.onNoteClicked(listNote[position])
        }
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatelist(newList: List<ModelNote>){
        this.listNote.clear()
        listNote.addAll(newList)
        notifyDataSetChanged()
    }


    interface NoteClickInterface{
        fun onNoteClicked(note: ModelNote)
    }
}