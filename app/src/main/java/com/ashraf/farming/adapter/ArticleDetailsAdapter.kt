package com.ashraf.farming.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.ashraf.farming.datamodel.article.Data
import com.shahbaz.farming.R
import com.shahbaz.farming.databinding.ArticleDetailsLayoutBinding

class ArticleDetailsAdapter : RecyclerView.Adapter<ArticleDetailsAdapter.CropViewHolder>() {

    inner class CropViewHolder(val binding: ArticleDetailsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: Data) = with(binding) {
            textCropName.text = currentItem.name
            textScientificName.text = "(${currentItem.scientific_name})"
            textCategory.text = currentItem.category
            textGrowingSeason.text = currentItem.growing_season
            textMaturity.text = "Maturity: ${currentItem.maturity_days} days"
            textWater.text = "Water: ${currentItem.water_requirements}"

            Glide.with(imageCrop.context)
                .load(currentItem.image_url)
                .placeholder(R.drawable.tomato)
                .error(R.drawable.tomato)
                .centerCrop()
                .into(imageCrop)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Data, newItem: Data) = oldItem == newItem
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Data>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CropViewHolder(ArticleDetailsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CropViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener { onItemClick?.invoke(currentItem) }
    }

    override fun getItemCount() = differ.currentList.size

    var onItemClick: ((Data) -> Unit)? = null
}
