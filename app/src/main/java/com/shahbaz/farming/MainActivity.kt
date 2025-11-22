package com.shahbaz.farming

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.shahbaz.farming.databinding.ActivityMainBinding
import com.shahbaz.farming.viewmodel.languageSelectionViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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