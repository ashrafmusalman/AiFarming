package com.ashraf.farming.mainscreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashraf.farming.adapter.FAQAdapter
import com.ashraf.farming.datamodel.FAQ
import com.ashraf.farming.util.hideBottomNavigationBar
import com.shahbaz.farming.databinding.FragmentFAQBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FAQFragment : Fragment() {

    private lateinit var binding: FragmentFAQBinding
    private val faqAdapter by lazy { FAQAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFAQBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(requireContext())
    }

    override fun onStart() {
        super.onStart()
        hideBottomNavigationBar()
    }

    private fun setupRecyclerView(context: Context) {
        val questionAnswerList = listOf(
            FAQ(1, "How do I add a new product to sell?", "Go to the 'My Products' section and tap on the 'Add Product' button. Fill in the details such as name, price, image, and description."),
            FAQ(2, "How do I edit or update my product details?", "Open your product from the list and tap the edit icon. You can update price, image, stock, and description."),
            FAQ(3, "How do I delete a product?", "Go to your product details and tap on the delete option. Once deleted, it cannot be recovered."),
            FAQ(4, "How can I contact a seller?", "Open the product details and tap the call icon next to the seller's contact number."),
            FAQ(5, "Why is the product not showing in search?", "Make sure your product is approved, has proper details, and is not marked as out of stock."),
            FAQ(6, "How do I report a problem with a seller?", "Use the 'Report Issue' option under the seller's profile or contact support from the app menu."),
            FAQ(7, "How do I check my cart?", "Tap the cart icon on the top menu. All added products will be listed there."),
            FAQ(8, "How do I increase or decrease quantity while buying?", "Inside product details, use the + and - buttons to adjust quantity before adding to cart or buying."),
            FAQ(9, "Can I buy multiple items from different sellers?", "Yes, your cart supports items from multiple sellers."),
            FAQ(10, "How do I track my orders?", "Go to the 'Orders' section in the app menu to view your order history and status."),
            FAQ(11, "Is my phone number visible to buyers?", "Only buyers who visit your product details can see your contact number."),
            FAQ(12, "How do I update my profile details?", "Go to the Profile section and use the edit button to update your name, address, and contact number."),
            FAQ(13, "What type of crops or products can I upload?", "You can upload any farming-related products such as seeds, fertilizers, plants, tools, and fresh produce."),
            FAQ(14, "Why can't I upload an image?", "Make sure you have granted storage or camera permissions in your device settings."),
            FAQ(15, "How do I search for a specific product?", "Use the search bar on the home screen and type the product name."),
            FAQ(16, "How do I refresh the product list?", "Swipe down on the home product page to refresh the list."),
            FAQ(17, "What happens if a seller does not respond?", "You can try calling again or choose an alternative seller. If needed, report the seller using the support option."),
            FAQ(18, "Can I save products to view later?", "Yes, use the 'Save' or 'Add to Wishlist' button to keep a list of products for later."),
            FAQ(19, "Are the prices negotiable?", "Price negotiation depends on the seller. You may contact them directly for inquiries."),
            FAQ(20, "How do I get app updates?", "Visit the Play Store and enable auto-updates for the app to receive new features and improvements.")
        )

        binding.faqRecyclerView.apply {
            adapter = faqAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            faqAdapter.differ.submitList(questionAnswerList)
        }
    }
}
