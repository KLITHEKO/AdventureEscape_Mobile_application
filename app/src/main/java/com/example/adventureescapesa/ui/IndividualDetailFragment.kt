package com.example.adventureescapesa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.adventureescapesa.MainActivity
import com.example.adventureescapesa.R
import com.example.adventureescapesa.adapter.FeaturedPackageAdapter
import com.example.adventureescapesa.databinding.FragmentIndividualDetailBinding
import com.example.adventureescapesa.databinding.ItemIncludedRowBinding
import com.example.adventureescapesa.model.AdventureItem
import com.example.adventureescapesa.model.BookingManager
import com.google.android.material.snackbar.Snackbar

/**
 * Screen 4: An Individual Page (Activity Detail)
 *
 * Pushed on top of the navigation stack when a user selects an activity.
 * Contains:
 *   - Back button & breadcrumb
 *   - Large activity photo
 *   - Price and title
 *   - Short description
 *   - "What's Included" checklist
 *   - "You May Also Like" related packages row
 *   - Fixed bottom "Add to Booking" button
 */
class IndividualDetailFragment : Fragment() {

    private var _binding: FragmentIndividualDetailBinding? = null
    private val binding get() = _binding!!

    private var currentItem: AdventureItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndividualDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemId = arguments?.getString(ARG_ITEM_ID)
        // Find the matching item, or fall back to the first one
        currentItem = BookingManager.sampleAdventures.find { it.id == itemId }
            ?: BookingManager.sampleAdventures.firstOrNull()

        populateItemDetails()
        setupRelatedPackages()
        setupActionButtons()
    }

    /** Puts the activity details into the layout. */
    private fun populateItemDetails() {
        val item = currentItem ?: return

        with(binding) {
            val section = getString(
                if (item.isPackage) R.string.detail_breadcrumb_packages else R.string.detail_breadcrumb_activities
            )
            tvDetailBreadcrumb.text = getString(R.string.detail_breadcrumb_format, section, item.title)

            imgDetailLarge.setImageResource(item.imageResId)
            imgDetailLarge.contentDescription = item.title
            tvDetailBadge.text = item.category

            tvDetailTitle.text = item.title
            tvDetailPrice.text = getString(R.string.price_whole_rand, item.price)
            tvDetailDuration.text = item.duration
            tvBottomPriceVal.text = getString(R.string.price_whole_rand, item.price)

            tvDetailDescription.text = item.description

            // "What's Included" checklist, built from the item's data
            layoutIncludedList.removeAllViews()
            item.includedItems.forEach { included ->
                val row = ItemIncludedRowBinding.inflate(layoutInflater, layoutIncludedList, false)
                row.root.text = included
                layoutIncludedList.addView(row.root)
            }
            cardIncluded.isVisible = item.includedItems.isNotEmpty()
        }
    }

    /** Fills the "You May Also Like" row. */
    private fun setupRelatedPackages() {
        val mainActivity = activity as? MainActivity
        val current = currentItem

        val relatedItems = BookingManager.sampleAdventures.filter { it.id != current?.id }

        val adapter = FeaturedPackageAdapter(relatedItems) { selectedRelated ->
            mainActivity?.openIndividualDetail(selectedRelated.id)
        }

        binding.rvRelatedPackages.adapter = adapter
    }

    /** Back button and "Add to Booking" button. */
    private fun setupActionButtons() {
        val mainActivity = activity as? MainActivity

        binding.btnDetailBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnAddToBooking.setOnClickListener {
            val item = currentItem ?: return@setOnClickListener

            BookingManager.addItem(item)

            Snackbar.make(
                binding.root,
                getString(R.string.detail_added_to_quotation, item.title),
                Snackbar.LENGTH_LONG
            ).setAction(R.string.detail_view_quotation) {
                mainActivity?.selectBottomNavItem(R.id.nav_calculate)
            }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ITEM_ID = "arg_item_id"

        fun newInstance(itemId: String): IndividualDetailFragment {
            return IndividualDetailFragment().apply {
                arguments = bundleOf(ARG_ITEM_ID to itemId)
            }
        }
    }
}
