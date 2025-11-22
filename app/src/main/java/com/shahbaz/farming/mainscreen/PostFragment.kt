package com.shahbaz.farming.mainscreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shahbaz.farming.R
import com.shahbaz.farming.adapter.PostAdapter
import com.shahbaz.farming.databinding.FragmentPostBinding
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.showBottomNavigationBar
import com.shahbaz.farming.viewmodel.AddPostViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private val addPostViewmodel by viewModels<AddPostViewmodel>()
    private val postAdapter: PostAdapter by lazy {
        PostAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addPost.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment2_to_addPostFragment)
        }
        val fab = binding.addPost
        val recyclerView = binding.postRecyclerview

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fab.isExtended) {
                    // Scrolling down -> shrink FAB
                    fab.shrink()
                } else if (dy < 0 && !fab.isExtended) {
                    // Scrolling up -> extend FAB
                    fab.extend()
                }
            }
        })

        addPostViewmodel.fetchPost()
        observeFetchPost()
        setUpRecyclerview()
    }

    private fun observeFetchPost() {

        lifecycleScope.launch {
            addPostViewmodel.fetchPost.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.progress.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Loading -> {
                        binding.progress.visibility = View.VISIBLE
                        // Toast.makeText(requireContext(),"Loading",Toast.LENGTH_SHORT).show()
                    }

                    is Resources.Success -> {
                        binding.progress.visibility = View.GONE
                        Log.d("Data", it.data.toString())
                        val data = it.data
                        data?.let { postItem ->
                            postAdapter.differ.submitList(postItem)
                        }
                    }

                    is Resources.Unspecified -> Unit
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationBar()
    }

    private fun setUpRecyclerview() {
        binding.postRecyclerview.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = postAdapter
            setHasFixedSize(true)
        }

    }
}