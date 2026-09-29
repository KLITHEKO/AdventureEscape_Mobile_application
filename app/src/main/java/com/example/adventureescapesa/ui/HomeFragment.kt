package com.example.adventureescapesa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.adventureescapesa.MainActivity
import com.example.adventureescapesa.R
import com.example.adventureescapesa.adapter.FeaturedPackageAdapter
import com.example.adventureescapesa.databinding.FragmentHomeBinding
import com.example.adventureescapesa.model.BookingManager

/**
 * Screen 1: Home Page
 *
 * Displays the top branding bar, hero showcase, "Browse Adventures" CTA,
 * and a horizontally scrolling list of featured eco-adventure packages.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHeroAndNavigation()
        setupFeaturedPackages()
    }

    /** Click listeners for the hero button, "See All" link and About Us button. */
    private fun setupHeroAndNavigation() {
        val mainActivity = activity as? MainActivity

        binding.btnBrowseAdventures.setOnClickListener {
            mainActivity?.selectBottomNavItem(R.id.nav_overview)
        }

        binding.tvSeeAll.setOnClickListener {
            mainActivity?.selectBottomNavItem(R.id.nav_overview)
        }

        binding.btnViewAbout.setOnClickListener {
            mainActivity?.openAboutUs()
        }
    }

    /** Fills the horizontal list of featured packages. */
    private fun setupFeaturedPackages() {
        val mainActivity = activity as? MainActivity

        val featuredList = BookingManager.sampleAdventures.filter { it.isPackage }

        val adapter = FeaturedPackageAdapter(featuredList) { adventureItem ->
            mainActivity?.openIndividualDetail(adventureItem.id)
        }

        binding.rvFeaturedPackages.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}