package com.ashraf.farming.mainscreen.ampc

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashraf.farming.adapter.APMCAdapter
import com.ashraf.farming.util.Resources
import com.ashraf.farming.viewmodel.apmc.APMCViewModel
import com.shahbaz.farming.databinding.FragmentAPMCBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class APMCFragment : Fragment() {

    private lateinit var binding: FragmentAPMCBinding
    private val apmcViewModel: APMCViewModel by viewModels<APMCViewModel>()
    private val apmcAdapter by lazy { APMCAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAPMCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpRecyclerView()
        apmcViewModel.fetchAPMCData()
        //apmcViewModel.fetchBasedOnDistrict()
        obServeMarketPrice()

    }

    private fun obServeMarketPrice() {
        lifecycleScope.launch {
            apmcViewModel.apmPriceState.collect {
                when (it) {
                    is Resources.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Resources.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("Data from apmc", it.data.toString())
                        binding.updatedDate.text = "Updated Date: ${it.data?.updated_date}"
                        apmcAdapter.difffer.submitList(it.data?.records)
                    }

                    is Resources.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }


    fun setUpRecyclerView() {
        binding.commodityRecycler.apply {
            adapter = apmcAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

    }
}