package com.shahbaz.farming.repo

import com.shahbaz.farming.datamodel.chatbot.ChatResponseHistory
import com.shahbaz.farming.room.ChatDao
import javax.inject.Inject

class ChatHistoryRepo @Inject constructor(
    private val chatDao: ChatDao
) {
    suspend fun saveMessage(msg: String, isBot: Boolean) {
        chatDao.insertMessage(ChatResponseHistory(message = msg, isBot = isBot))
        chatDao.deleteOldMessages()
    }

    suspend fun loadMessages() = chatDao.getLastTenMessages()
}
