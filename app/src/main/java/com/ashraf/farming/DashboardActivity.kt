package com.ashraf.farming

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.ashraf.farming.datamodel.User
import com.ashraf.farming.util.Constant.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.ashraf.farming.util.Resources
import com.ashraf.farming.util.progressDialgoue
import com.ashraf.farming.util.showDialogue
import com.ashraf.farming.viewmodel.HomeFragmentViewmodel
import com.ashraf.farming.viewmodel.languageSelectionViewmodel
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val homeFragmentViewmodel by viewModels<HomeFragmentViewmodel>()
    private var selectedProfileUrl = ""
    private lateinit var progressDialog: ProgressDialog
    private val languageChangeViewmodel by viewModels<languageSelectionViewmodel>()


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set system bar colors (Android 14/15 compatible)
        window.statusBarColor = getColor(R.color.green)
        window.navigationBarColor = getColor(R.color.green)

        val controller = WindowCompat.getInsetsController(window, window.decorView)

        // Make status bar icons white
        controller.isAppearanceLightStatusBars = false

        // Make navigation bar icons dark since background is light
        controller.isAppearanceLightNavigationBars = true

        // Fix for Android 14/15 (prevents forced white status bar)
        window.isStatusBarContrastEnforced = false

        setSupportActionBar(binding.myToolbar)

        // Setup Navigation drawer toggle
        setupNavigationdrawer()

        // Set up bottom navigation with navController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.HomeFragment,
                R.id.PostFragment,
                R.id.EcommerceFragment,
                R.id.APMCFragment -> {
                    binding.bottomNavigationView.post {
                        BottomNavigationVisibilityShow()
                    }
                }
                else -> BottomNavigationVisibilityHidden()
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.HomeFragment->binding.appbarLayout.visibility=View.VISIBLE
                else->{
                    binding.appbarLayout.visibility=View.GONE
                }
            }

        }
        binding.myToolbar.setNavigationOnClickListener {
            if (binding.main.isDrawerOpen(GravityCompat.START)) {
                binding.main.closeDrawer(GravityCompat.START)
            } else {
                binding.main.openDrawer(GravityCompat.START)
            }
        }

        // Load current user details
        homeFragmentViewmodel.getCurrentUserDetails()
        observeCurrentUserDetails()
        observeProfileChange()

        progressDialog = ProgressDialog(this)

        // Handle navigation drawer item clicks
        binding.navigationview.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.profile -> navigateToFragmentfromDrawer(R.id.profileFragment)

                R.id.lang -> showDialogueWithRadioButton("Change Language", "Select Language")

                R.id.logout -> {
                    homeFragmentViewmodel.signOut()
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                    goToMainActivity()
                }

                R.id.sellProduct -> navigateToFragmentfromDrawer(R.id.addProductFragment)

                R.id.seeYourProduct -> navigateToFragmentfromDrawer(R.id.yourProductFragment)

                R.id.cartItem -> navigateToFragmentfromDrawer(R.id.cartFragment)

                R.id.orderedItem -> navigateToFragmentfromDrawer(R.id.orderedFragment)

                R.id.orderedrecevied -> navigateToFragmentfromDrawer(R.id.orderReceivedFragment)

                R.id.faq -> navigateToFragmentfromDrawer(R.id.FAQFragment)
            }

            binding.main.closeDrawer(GravityCompat.START)
            true
        }
    }


    private fun observeProfileChange() {
        lifecycleScope.launch {
            homeFragmentViewmodel.updateProfileStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(this@DashboardActivity, it.message, Toast.LENGTH_SHORT)
                            .show()

                        progressDialog.hide()
                    }

                    is Resources.Loading -> {

                    }

                    is Resources.Success -> {
                        val headerView = binding.navigationview.getHeaderView(0)
                        val profile = headerView.findViewById<CircleImageView>(R.id.profile_image)

                        if (it.data != null) {
                            Glide.with(this@DashboardActivity)
                                .load(it.data)
                                .into(profile)
                        }
                        progressDialog.hide()


                    }

                    is Resources.Unspecified -> {

                    }
                }
            }
        }
    }

    private fun observeCurrentUserDetails() {
        lifecycleScope.launch {
            homeFragmentViewmodel.userDetailState.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(this@DashboardActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resources.Loading -> {

                    }

                    is Resources.Success -> {
                        val data = it.data
                        setUpNavHeaderdata(data!!)

                    }

                    is Resources.Unspecified -> {

                    }
                }
            }
        }
    }

    private fun setUpNavHeaderdata(data: User) {
        val headerview = binding.navigationview.getHeaderView(0)
        val name = headerview.findViewById<TextView>(R.id.profileName)
        name.text = data.name
        val email = headerview.findViewById<TextView>(R.id.email)
        email.text = data.email
        val profileImage = headerview.findViewById<CircleImageView>(R.id.profile_image)

        if (data.profileUrl != "") {
            Glide.with(this).load(data.profileUrl).into(profileImage)
        }

        profileImage.setOnClickListener {
            showDialogue(
                this@DashboardActivity,
                "Profile Picture",
                "Choose to change profile picture",
                "Change Photo",
                "Cancel",
                onClick = {
                    selectImage()
                }
            )
        }


    }


    private fun setupNavigationdrawer() {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this@DashboardActivity,
            binding.main,
            R.string.nav_open,
            R.string.nav_close
        )
        actionBarDrawerToggle.drawerArrowDrawable.color = getResources().getColor(R.color.white);

        binding.main.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now fetch the location
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.HomeFragment) // Ensure you navigate to HomeFragment
            } else {
                // Permission denied, show a dialog or take appropriate action
            }
        }
    }


    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun selectImage() {
        selectedProfileLauncher.launch("image/*")
    }


    private val selectedProfileLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedProfileUrl = it.toString()
            progressDialgoue(
                progressDialog,
                "Uploading Image...",
                "Upload In Progress..."
            )
            homeFragmentViewmodel.updateProfile(selectedProfileUrl)
        }

    }

    private fun navigateToFragmentfromDrawer(fragmentId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController
        if (fragmentId == R.id.addProductFragment) {
            val data = Bundle().apply {
                putParcelable("productUpdate", null)
            }
            navController.navigate(fragmentId, data)
        } else {
            navController.navigate(fragmentId)
        }

    }

    private fun showDialogueWithRadioButton(
        title: String,
        message: String,
    ) {
        // List of available languages
        val languages = arrayOf("English", "Hindi")

        val selectedLanguage = languageChangeViewmodel.getSelectedLanguage(this)
        var selectedLanguageIndex = languages.indexOf(selectedLanguage).takeIf { it >= 0 }?: 0

        // Create the AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        // Display languages as radio button list
        builder.setSingleChoiceItems(languages, selectedLanguageIndex) { _, which ->
            selectedLanguageIndex = which // Update the selected language index
        }

        // Handle the OK button click
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(
                this, // Context should be correct here
                "Selected Language: ${languages[selectedLanguageIndex]}",
                Toast.LENGTH_SHORT
            ).show()
            // You can now handle the language change here, for example:
            // changeLanguage(selectedLanguageIndex)
            changeLanguage(selectedLanguageIndex)
            languageChangeViewmodel.saveSelectedLanguage(this, languages[selectedLanguageIndex])
        }

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
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

        // Restart the activity to reflect the changes
        val intent = Intent(this, this::class.java)
        finish()
        startActivity(intent)
    }

    private fun BottomNavigationVisibilityShow() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun BottomNavigationVisibilityHidden() {
        binding.bottomNavigationView.visibility = View.GONE
    }
}