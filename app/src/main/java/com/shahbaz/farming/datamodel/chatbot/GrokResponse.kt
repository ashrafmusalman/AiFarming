package com.shahbaz.farming.datamodel.chatbot

import com.google.gson.annotations.SerializedName

data class GrokResponse(
    val id: String? = null,
    @SerializedName("object")
    val objectType: String? = null,
    val created: Long? = null,
    val model: String? = null,
    val choices: List<GrokChoice>? = null
)

data class GrokChoice(
    val index: Int? = null,
    val message: GrokMessage,
    @SerializedName("finish_reason")
    val finish_reason: String? = null
)
