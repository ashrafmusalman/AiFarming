package com.ashraf.farming.datamodel.chatbot

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_history")

data class ChatResponseHistory(

    @PrimaryKey(autoGenerate = true)
    val  id: Int=0,
    val message: String,
    val isBot:Boolean,
    val timeStamp: Long = System.currentTimeMillis()

)
