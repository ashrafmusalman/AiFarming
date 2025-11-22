package com.shahbaz.farming.datamodel.chatbot

data class GrokRequest(
    val model: String,
    val messages: List<GrokMessage>
)

data class GrokMessage(
    val role: String,
    val content: String
)
