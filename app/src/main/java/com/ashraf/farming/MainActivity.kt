package com.ashraf.farming

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.ashraf.farming.viewmodel.languageSelectionViewmodel
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val languageChangeViewmodel by viewModels<languageSelectionViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // enableEdgeToEdge()


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostContainer) as NavHostFragment
        navHostFragment.navController

        val lang = languageChangeViewmodel.getSelectedLanguage(this)
        if (lang == "Hindi") {
            changeLanguage(1)
        } else {
            changeLanguage(0)
        }

    }

    private fun changeLanguage(languageIndex: Int) {
        val locale = when (languageIndex) {
            1 -> Locale("hi") // Hindi
            else -> Locale("en") // English
        }

        // Update the app's locale
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config) // Update the configuration context
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}