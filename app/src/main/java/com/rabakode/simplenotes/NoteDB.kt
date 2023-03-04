package com.rabakode.simplenotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ModelNote::class], version = 1, exportSchema = false)
abstract class NoteDB : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object{

        @Volatile
        private var INSTANCE : NoteDB?=null

        @Synchronized
        fun getDatabase(context: Context): NoteDB {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext, NoteDB::class.java, "NoteDB").build()
                INSTANCE = instance
                instance
            }

        }
    }
}