package com.shahbaz.farming.network

import com.shahbaz.farming.datamodel.chatbot.GrokRequest
import com.shahbaz.farming.datamodel.chatbot.GrokResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GrokApi {

    @POST("chat/completions")
    suspend fun generateResponse(
        @Body request: GrokRequest
    ): GrokResponse
}
