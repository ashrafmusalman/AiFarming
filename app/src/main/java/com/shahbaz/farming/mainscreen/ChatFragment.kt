package com.shahbaz.farming.mainscreen

import ChatHistoryAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shahbaz.farming.adapter.ChatAdapter
import com.shahbaz.farming.databinding.FragmentChatBinding
import com.shahbaz.farming.datamodel.Message
import com.shahbaz.farming.datamodel.chatbot.ChatResponseHistory
import com.shahbaz.farming.util.hideBottomNavigationBar
import com.shahbaz.farming.viewmodel.GrokViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var historyAdapter: ChatHistoryAdapter

    private var greetingShown = false

    private val viewModel: GrokViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupHistoryRecyclerView()

        showGreeting()

        loadSavedChatHistory()
        observeViewModel()

        binding.sendButton.setOnClickListener {
            val prompt = binding.messageBox.text.toString().trim()
            if (prompt.isNotEmpty()) {
                hideKeyboard()

                if (greetingShown) {
                    chatAdapter.removeGreeting()
                    greetingShown = false
                }

                chatAdapter.addMessage(Message(prompt, isBot = false))
                binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
                binding.messageBox.text.clear()

                viewModel.sendPrompt(prompt)
            }
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadSavedChatHistory() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.chatHistory.collectLatest { fullHistory ->

                val userOnly = fullHistory.filter { !it.isBot }
                val recentFive = userOnly.takeLast(5)

                historyAdapter.submitList(recentFive)

                if (recentFive.isEmpty()) {
                    binding.historyContainer.visibility = View.GONE
                } else {
                    binding.historyContainer.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.loading.collectLatest { isLoading ->
                binding.sendButton.isEnabled = !isLoading

                if (isLoading) chatAdapter.showTyping()
                else chatAdapter.hideTyping()

                binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.botResponse.collectLatest { response ->
                if (response.isNotEmpty()) {
                    chatAdapter.hideTyping()
                    chatAdapter.addMessage(Message(response, isBot = true))
                    binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
                }
            }
        }
    }

    private fun showGreeting() {
        greetingShown = true
        chatAdapter.clear()

        val greetingMessage =
            "Hello! 🌾 I'm your FarmerMate. How can I help you today?"

        chatAdapter.addMessage(Message(greetingMessage, isBot = true))
        binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun setupHistoryRecyclerView() {
        historyAdapter = ChatHistoryAdapter { item ->
            showSavedChat(item)
        }

        binding.historyRecyclerView.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun showSavedChat(item: ChatResponseHistory) {
        greetingShown = false
        chatAdapter.clear()

        chatAdapter.addMessage(Message(item.message, isBot = false))

        val botMessage = viewModel.chatHistory.value.firstOrNull {
            it.isBot && it.id == item.id + 1
        }

        botMessage?.let {
            chatAdapter.addMessage(Message(it.message, isBot = true))
        }

        binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(binding.messageBox.windowToken, 0)
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
