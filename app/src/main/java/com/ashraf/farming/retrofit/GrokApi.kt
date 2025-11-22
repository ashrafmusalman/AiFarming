package com.ashraf.farming.network

import com.ashraf.farming.datamodel.chatbot.GrokRequest
import com.ashraf.farming.datamodel.chatbot.GrokResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface GrokApi {

    @POST("chat/completions")
    suspend fun generateResponse(
        @Body request: GrokRequest
    ): GrokResponse
}
