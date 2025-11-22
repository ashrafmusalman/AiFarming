package com.ashraf.farming.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Constant @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {

        const val ONBOARDING_PREFERENCES = "my_pref"
        const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        const val WAETHER_API_KEY = "a4b16fd186efbbcad51d1fbcb7353798"
        const val WEATHER_BASE_URL = "https://api.openweathermap.org/"
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        const val MARKET_PRICE_BASE_URL = "https://api.data.gov.in/"
        const val API_KEY_FOR_MARKET_PRICE =
            "579b464db66ec23bdd000001987c65666f9c49656f0f9ef4fa3650e7"
        const val NOTIFICATION_URL = "https://fcm.googleapis.com/v1/projects/kotlin-project-aef9a/"
        const val ARTICLE_BASE_URL = "https://api.riwe.io/"
        const val GROK_API_KEY="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"
        const val TREFLE_TOKEN = "usr-vxufVsB-ZcFwymrJc8ZJVxSziRIdXxtIYqDCVsOMV20"
         const val TREFLE_BASEURL = "https://trefle.io/api/v1/"
        const val GROK_BASE_URL = "https://api.groq.com/openai/v1/"



        val CATEGORY_LIST = ArrayList<String>(
            listOf(
                "All",
                "Fertilizers",
                "Pesticides",
                "Seeds",
                "Machinery"
            )
        )

    }
}


