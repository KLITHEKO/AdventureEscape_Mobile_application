package com.example.adventureescapesa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adventureescapesa.R
import com.example.adventureescapesa.databinding.ItemOverviewCardBinding
import com.example.adventureescapesa.model.AdventureItem

/**
 * RecyclerView adapter for the Overview page.
 * Shows vertically scrolling cards for either Adventure Packages or Individual Activities.
 * Pass a new filtered list with submitList() when the tab changes.
 */
class OverviewAdapter(
    private val onViewDetailsClick: (AdventureItem) -> Unit
) : ListAdapter<AdventureItem, OverviewAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemOverviewCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOverviewCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            imgOverviewPhoto.setImageResource(item.imageResId)
            imgOverviewPhoto.contentDescription = item.title
            tvOverviewTitle.text = item.title
            tvOverviewCategory.text = item.category
            tvOverviewDuration.text = item.duration
            tvOverviewDescription.text = item.description
            tvOverviewPrice.text = root.context.getString(R.string.price_whole_rand, item.price)

            // "View Details" button or the card opens the detail page
            btnOverviewViewDetails.setOnClickListener { onViewDetailsClick(item) }
            root.setOnClickListener { onViewDetailsClick(item) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<AdventureItem>() {
        override fun areItemsTheSame(oldItem: AdventureItem, newItem: AdventureItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AdventureItem, newItem: AdventureItem): Boolean =
            oldItem == newItem
    }
}
