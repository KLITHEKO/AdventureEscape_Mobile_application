package com.example.adventureescapesa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adventureescapesa.R
import com.example.adventureescapesa.databinding.ItemFeaturedPackageBinding
import com.example.adventureescapesa.model.AdventureItem

/**
 * RecyclerView adapter for the horizontally scrolling featured package cards.
 * Used on the Home page and in the "You May Also Like" row on the detail page.
 */
class FeaturedPackageAdapter(
    private val items: List<AdventureItem>,
    private val onItemClick: (AdventureItem) -> Unit
) : RecyclerView.Adapter<FeaturedPackageAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemFeaturedPackageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFeaturedPackageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        with(holder.binding) {
            imgFeatured.setImageResource(item.imageResId)
            imgFeatured.contentDescription = item.title
            tvFeaturedTitle.text = item.title
            tvFeaturedCategory.text = item.category
            tvFeaturedDuration.text = item.duration
            tvFeaturedPrice.text = root.context.getString(R.string.price_from_per_person, item.price)

            // Tapping the "View" button or the card opens the detail page
            btnFeaturedView.setOnClickListener { onItemClick(item) }
            root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun getItemCount(): Int = items.size
}
