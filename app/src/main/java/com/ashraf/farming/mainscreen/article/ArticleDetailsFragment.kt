package com.ashraf.farming.mainscreen.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.shahbaz.farming.databinding.FragmentArticleDetailsBinding

class ArticleDetailsFragment : Fragment() {

    private var _binding: FragmentArticleDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: ArticleDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentArticleDetailsBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plant = args.plant
Log.d("ArticleDetailsFragment", "Plant: $plant")
        Glide.with(requireContext())
            .load(plant.image_url)
            .into(binding.plantImage)

        binding.commonName.text = plant.common_name
        binding.scientificName.text = plant.scientific_name
        binding.family.text = "Family: ${plant.family}"
        binding.familyCommonName.text = "Family Common Name: ${plant.family_common_name}"
        binding.genus.text = "Genus: ${plant.genus}"
        binding.author.text = "Author: ${plant.author}"
        binding.bibliography.text = "Bibliography: ${plant.bibliography}"
        binding.rank.text = "Rank: ${plant.rank}"
        binding.status.text = "Status: ${plant.status}"
        binding.year.text = "Year: ${plant.year}"
        binding.synonyms.text = "Synonyms: ${plant.synonyms?.joinToString(", ")}"
        binding.slug.text = "Slug: ${plant.slug}"
        binding.links.text = "Links: ${plant.links}" // customize links if needed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
