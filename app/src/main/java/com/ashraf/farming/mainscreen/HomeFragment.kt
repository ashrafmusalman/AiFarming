// HomeFragment.kt
package com.ashraf.farming.mainscreen

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import com.google.firebase.messaging.FirebaseMessaging
import com.ashraf.farming.adapter.ArticleAdapter
import com.ashraf.farming.datamodel.weahterdatamodel.WeatherRootList
import com.ashraf.farming.permission.checkPermission
import com.ashraf.farming.permmsion.checkImagePermissionForNotifcation
import com.ashraf.farming.permmsion.checkImagePermissionForPhoto
import com.ashraf.farming.repo.apmc.APMCRepo
import com.ashraf.farming.util.Constant.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.ashraf.farming.util.Resources
import com.ashraf.farming.util.showBottomNavigationBar
import com.ashraf.farming.viewmodel.BillingViewmodel
import com.ashraf.farming.viewmodel.HomeViewModel
import com.ashraf.farming.weather.WeatherViewmodel
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val weatherViewmodel by viewModels<WeatherViewmodel>()
    private val billingViewmodle by viewModels<BillingViewmodel>()
    private val homeViewModel by viewModels<HomeViewModel>()
    private val articleAdapter by lazy { ArticleAdapter() }
    private var lat: String? = null
    private var lon: String? = null

    @Inject
    lateinit var priceRepo: APMCRepo

    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            results.entries.forEach {
                if (it.value) {
                } else {
                    Toast.makeText(requireContext(), "${it.key} denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        priceRepo.fetchApmcData()
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            billingViewmodle.updateFCMtoken(it)
            Log.d("Token", it)
        }
        val popAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.bot_pop)
        binding.chatWithBot.startAnimation(popAnim)

        checkImagePermissionForPhoto(requireContext(), requestPermissions)
        checkImagePermissionForNotifcation(requireContext(), requestPermissions)
        fetchLocation()
        observeWeather()
        homeViewModel.getPlants()
        observePlants()
        articleAdapter.onItemClick = { plant ->
            val action = HomeFragmentDirections
                .actionHomeFragmentToArticleDetailsFragment(plant)
            findNavController().navigate(action)
        }


        binding.chatWithBot.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_chatFragment)
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerviewArticle.apply {
            adapter = articleAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observePlants() {
        lifecycleScope.launch {
            homeViewModel.plants.collect { state ->
                when (state) {
                    is Resources.Success -> {
                        val plants = state.data?.data ?: emptyList()
                        articleAdapter.differ.submitList(plants)
                    }
                    is Resources.Error -> {
                        articleAdapter.differ.submitList(emptyList())
                        Log.e("TREFLE_API", "Error: ${state.message}")
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun observeWeather() {
        lifecycleScope.launch {
            weatherViewmodel.weatherState.collect {
                when (it) {
                    is Resources.Loading -> binding.weatherinfo.visibility = View.GONE
                    is Resources.Success -> {
                        binding.weatherinfo.visibility = View.VISIBLE
                        it.data?.let { data -> updateWeatherUI(data) }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun updateWeatherUI(data: WeatherRootList) {
        binding.weatherWind.text = "Wind: ${data.wind.speed} km/hr"
        binding.weatherHumidity.text = "Humidity: ${data.main.humidity} %"
        val iconurl = "https://openweathermap.org/img/w/${data.weather[0].icon}.png"
        Glide.with(requireContext()).load(iconurl).into(binding.weatherIconImage)
        binding.cityName.text = data.name
        weatherViewmodel.setCityName(data.name)
        binding.weatherTemperature.text = "${(data.main.temp - 273).toInt()}\u2103"
    }

    private fun fetchLocation() {
        if (checkPermission(requireContext())) {
            val fused = LocationServices.getFusedLocationProviderClient(requireContext())
            fused.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    lat = it.latitude.toString()
                    lon = it.longitude.toString()
                    weatherViewmodel.getWeather(lat!!, lon!!)
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationBar()
    }
}
