package com.rabakode.simplenotes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.makeramen.roundedimageview.RoundedImageView
import java.text.SimpleDateFormat
import java.util.*


class CreateNoteActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_PERMISSION = 1
        private const val REQUEST_SELECT = 2
        private const val EXTRA_NOTE = "EXTRA_NOTE"
    }

    private lateinit var etTitle: EditText
    private lateinit var etNote: EditText
    private lateinit var ivSave: ImageView
    private lateinit var tvDate: TextView
    private lateinit var ivMenu: ImageView
    private lateinit var imageNote: RoundedImageView
    private lateinit var ivBack: ImageView
    private lateinit var viewModel: NoteViewModel
    private var noteID = 1
    private var imagePath: String? = ""


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_note)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NoteViewModel::class.java]

        imagePath=""

        etTitle = findViewById(R.id.etTitle)
        etNote = findViewById(R.id.etNote)
        tvDate = findViewById(R.id.tvDate)
        ivSave = findViewById(R.id.ivSave)
        imageNote = findViewById(R.id.imageNote)
        ivBack = findViewById(R.id.ivBack)
        ivMenu = findViewById(R.id.ivMenu)

        ivBack.setOnClickListener {
            startActivity(Intent(this@CreateNoteActivity, MainActivity::class.java))
            this.finish()
        }

        ivMenu.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.menu, null)
            val tvShare = view.findViewById<TextView>(R.id.tvShare)
            val tvImage = view.findViewById<TextView>(R.id.tvImage)
            val tvDel = view.findViewById<TextView>(R.id.tvDelete)
            tvShare.setOnClickListener {
                val noteTitle = etTitle.text.toString()
                val noteDesc = etNote.text.toString()
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Share\n--------------------\n$noteTitle\n$noteDesc"
                    )
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Share via : ")
                startActivity(shareIntent)
                dialog.dismiss()
            }
            tvDel.setOnClickListener {
                try {
                    val deletedNote = ModelNote()
                    deletedNote.id = noteID
                    viewModel.deleteNote(deletedNote)
                    Toast.makeText(this@CreateNoteActivity, "Note has been deleted", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this@CreateNoteActivity, "Fail to delete note", Toast.LENGTH_SHORT)
                        .show()
                }
                startActivity(Intent(applicationContext, MainActivity::class.java))
                this.finish()
                dialog.dismiss()
            }
            tvImage.setOnClickListener {
                imageNote.visibility = View.VISIBLE
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this@CreateNoteActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION
                    )
                } else {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, REQUEST_SELECT)
                }
                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.setContentView(view)
            dialog.show()
        }


       val noteMode = intent.getStringExtra("noteMode")
        if (noteMode.equals("Edit")) {
            val data = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(EXTRA_NOTE, ModelNote::class.java)
            } else {
                intent.getParcelableExtra(EXTRA_NOTE) as? ModelNote
            }
            /*val title = intent.getStringExtra("title")
            val text = intent.getStringExtra("note")
            val date = intent.getStringExtra("date")
            val img = intent.getStringExtra("image")
            noteID = intent.getIntExtra("id", -1)*/
            noteID = data!!.id
            etTitle.setText(data.title)
            etNote.setText(data.note)
            tvDate.text = data.date

            if (data.imagePath != null && data.imagePath!!.trim().isEmpty())
            {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(data.imagePath)) //keeping img still show when you click the note
            }
            else {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(data.imagePath))
                imageNote.visibility = View.VISIBLE
                imagePath = data.imagePath
            }

        }


        ivSave.setOnClickListener {
            val noteTitle = etTitle.text.toString()
            val noteDesc = etNote.text.toString()

            if (noteTitle.isEmpty()) {
                etTitle.error = "Fill the title"
                return@setOnClickListener
            }
            if (noteDesc.isEmpty()) {
                etNote.error = "Fill the note"
                return@setOnClickListener
            }

            if (noteMode.equals("Edit")) {
                try {
                    tvDate.text = "Last updated: " + SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
                    val dateNote = tvDate.text.toString()
                    val updatedNote = ModelNote(noteTitle, noteDesc, dateNote, imagePath)
                    updatedNote.id = noteID
                    viewModel.updateNote(updatedNote)
                    Toast.makeText(this@CreateNoteActivity, "$noteTitle updated", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@CreateNoteActivity,
                        "Fail to update note",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                try {
                    tvDate.text = "Last updated: " + SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())
                    val dateNote = tvDate.text.toString()
                    viewModel.insertNote(ModelNote(noteTitle, noteDesc, dateNote, imagePath))
                    Toast.makeText(this@CreateNoteActivity, "$noteTitle Added", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    Toast.makeText(this@CreateNoteActivity, "Fail to add note", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        startActivity(Intent(this@CreateNoteActivity, MainActivity::class.java))
        this.finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, REQUEST_SELECT)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("RestrictedApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT && resultCode == RESULT_OK) {
            if (data != null) {
                val selectImageUri = data.data
                if (selectImageUri != null) {
                    try {
                        val inputStream = contentResolver.openInputStream(selectImageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imageNote.setImageBitmap(bitmap)
                        imageNote.visibility = View.VISIBLE
                        imagePath = getPathFromUri(selectImageUri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        val filePath: String?
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

}