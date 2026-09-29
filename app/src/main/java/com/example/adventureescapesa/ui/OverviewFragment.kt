package com.example.adventureescapesa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adventureescapesa.MainActivity
import com.example.adventureescapesa.R
import com.example.adventureescapesa.adapter.OverviewAdapter
import com.example.adventureescapesa.databinding.FragmentOverviewBinding
import com.example.adventureescapesa.model.BookingManager
import com.google.android.material.tabs.TabLayout

/**
 * Screen 3: The Overview Page
 *
 * Features a two-tab toggle:
 *   - Tab 0: "Adventure Packages" (Multi-day guided excursions)
 *   - Tab 1: "Individual Activities" (Single day / half-day outings)
 * Switching tabs filters the vertical RecyclerView.
 */
class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var overviewAdapter: OverviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupTabToggle()
        // Show the first tab (Adventure Packages) initially
        filterListByTab(0)
    }

    private fun setupRecyclerView() {
        val mainActivity = activity as? MainActivity

        overviewAdapter = OverviewAdapter { adventureItem ->
            mainActivity?.openIndividualDetail(adventureItem.id)
        }

        binding.rvOverviewList.adapter = overviewAdapter
    }

    /** Listens for taps on the Packages / Activities tabs. */
    private fun setupTabToggle() {
        binding.tabLayoutOverview.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                filterListByTab(tab?.position ?: 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    /**
     * Filters the list based on the selected tab.
     * position 0 = Adventure Packages
     * position 1 = Individual Activities
     */
    private fun filterListByTab(position: Int) {
        val isPackagesTab = (position == 0)
        val filtered = BookingManager.sampleAdventures.filter { it.isPackage == isPackagesTab }

        overviewAdapter.submitList(filtered)

        val countText = if (isPackagesTab) {
            R.plurals.overview_count_packages
        } else {
            R.plurals.overview_count_activities
        }
        binding.tvOverviewCount.text =
            resources.getQuantityString(countText, filtered.size, filtered.size)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
