package com.shahbaz.farming.repository

import android.util.Log
import com.shahbaz.farming.datamodel.chatbot.GrokMessage
import com.shahbaz.farming.datamodel.chatbot.GrokRequest
import com.shahbaz.farming.network.GrokApi
import com.shahbaz.farming.room.ChatDao
import javax.inject.Inject

class GrokRepo @Inject constructor(
    private val grokApi: GrokApi,
) {

    suspend fun getChatResponse(prompt: String): String {
        return try {
            val systemRule = """
You are an expert farming assistant 🌾. Always answer in a friendly and simple way, like a helpful farmer talking to a friend.  
- Use simple, clear English.  
-your name is FarmerMate
- Add just a little emoji here and there to feel natural, not too many.  
- Give short, practical advice.  
- If the user asks anything not related to farming, agriculture, crops, fertilizers, soil, weather, farm machines, irrigation, seeds, livestock, or plant diseases, politely refuse with a message like:  
  "Oops! I can only talk about farming stuff. Please ask a farming-related question."  
- Be cheerful, encouraging, and realistic in your responses.
""".trimIndent()


            Log.d("GrokRepo", "User Prompt: $prompt")

            val request = GrokRequest(
                model = "llama-3.1-8b-instant",
                messages = listOf(
                    GrokMessage(
                        role = "system",
                        content = systemRule
                    ),
                    GrokMessage(
                        role = "user",
                        content = prompt
                    )
                )
            )

            Log.d("GrokRepo", "Sending Request: $request")

            val response = grokApi.generateResponse(request)

            Log.d("GrokRepo", "Raw Response: $response")

            val choice = response.choices?.firstOrNull() ?: return "No response from Groq"
            val output = choice.message.content

            Log.d("GrokRepo", "Final Output: $output")

            return output
        } catch (e: Exception) {
            Log.e("GrokRepo", "Error: ${e.localizedMessage}")
            return "Error: ${e.localizedMessage}"
        }
    }
}
