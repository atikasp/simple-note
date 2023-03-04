package com.rabakode.simplenotes

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "notes")
class ModelNote(
    @ColumnInfo(name = "title")
    var title:String?="",

    @ColumnInfo(name = "note")
    var note:String?="",

    @ColumnInfo(name = "date")
    var date:String?="",

    @ColumnInfo(name = "image_path")
    var imagePath: String?="",

    @PrimaryKey(autoGenerate = true)
    var id: Int =0

): Parcelable
