package com.rabakode.simplenotes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), NoteAdapter.NoteClickInterface {

    companion object{
        private const val EXTRA_NOTE = "EXTRA_NOTE"
    }
    private var noteAdapter: NoteAdapter?= null
    private lateinit var fabCreateNote: FloatingActionButton
    private lateinit var rvListNote: RecyclerView
    private lateinit var viewModel : NoteViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NoteViewModel::class.java]

        rvListNote = findViewById(R.id.rvListNote)
        noteAdapter = NoteAdapter(this)
        rvListNote.adapter = noteAdapter
        rvListNote.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        viewModel.allNotes.observe(this) { list ->
            list?.let {
                noteAdapter!!.updatelist(it)
            }
        }

        fabCreateNote = findViewById(R.id.fabCreateNote)
        fabCreateNote.setOnClickListener {
            startActivity(Intent(this@MainActivity, CreateNoteActivity::class.java))
            this.finish()
        }

    }

    override fun onNoteClicked(note: ModelNote){
        val intent = Intent(this@MainActivity, CreateNoteActivity::class.java)
        intent.putExtra(EXTRA_NOTE, note)
        intent.putExtra("noteMode", "Edit")
        /*intent.putExtra("title",note.title)
        intent.putExtra("note",note.note)
        intent.putExtra("date",note.date)
        intent.putExtra("image",note.imagePath)
        intent.putExtra("id",note.id)*/
        startActivity(intent)
        this.finish()

    }


}