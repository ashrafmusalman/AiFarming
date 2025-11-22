package com.ashraf.farming.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ashraf.farming.datamodel.chatbot.ChatResponseHistory

@Database(entities = [ChatResponseHistory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}
