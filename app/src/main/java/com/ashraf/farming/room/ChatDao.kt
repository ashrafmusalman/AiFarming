package com.ashraf.farming.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ashraf.farming.datamodel.chatbot.ChatResponseHistory

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(chat: ChatResponseHistory)

    @Query("SELECT * FROM chat_history ORDER BY timestamp ASC LIMIT 10")
    suspend fun getLastTenMessages(): List<ChatResponseHistory>

    @Query(
        """
        DELETE FROM chat_history 
        WHERE id NOT IN (SELECT id FROM chat_history ORDER BY timestamp DESC LIMIT 10)
    """
    )
    suspend fun deleteOldMessages()
}
