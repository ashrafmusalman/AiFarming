package com.shahbaz.farming.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahbaz.farming.datamodel.chatbot.ChatResponseHistory
import com.shahbaz.farming.repo.ChatHistoryRepo
import com.shahbaz.farming.repository.GrokRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GrokViewModel @Inject constructor(
    private val repository: GrokRepo,
    private val chatHistoryRepo: ChatHistoryRepo
) : ViewModel() {

    private val _botResponse = MutableStateFlow("")
    val botResponse = _botResponse.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _chatHistory = MutableStateFlow<List<ChatResponseHistory>>(emptyList())
    val chatHistory = _chatHistory.asStateFlow()

    init {
        loadSavedChats()
    }

    fun sendPrompt(prompt: String) {
        Log.d("GrokVM", "sendPrompt called with: $prompt")

        viewModelScope.launch {

            // Show loader
            _loading.value = true

            //  Save USER message
            saveUserMessage(prompt)

            // Make API call
            val response = repository.getChatResponse(prompt)
            Log.d("GrokVM", "Received response: $response")

            // Update LiveResponse
            _botResponse.value = response

            //  Save BOT message
            saveBotMessage(response)

            // Hide loader
            _loading.value = false
        }
    }

    private fun loadSavedChats() {
        viewModelScope.launch {
            _chatHistory.value = chatHistoryRepo.loadMessages()
        }
    }

    private fun saveUserMessage(msg: String) {
        viewModelScope.launch {
            chatHistoryRepo.saveMessage(msg, false)  // false = user
            loadSavedChats()
        }
    }

    private fun saveBotMessage(msg: String) {
        viewModelScope.launch {
            chatHistoryRepo.saveMessage(msg, true)   // true = bot
            loadSavedChats()
        }
    }
}
