package com.example.adventureescapesa

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.adventureescapesa.databinding.ActivityMainBinding
import com.example.adventureescapesa.ui.AboutUsFragment
import com.example.adventureescapesa.ui.CalculateFeeFragment
import com.example.adventureescapesa.ui.ContactUsFragment
import com.example.adventureescapesa.ui.HomeFragment
import com.example.adventureescapesa.ui.IndividualDetailFragment
import com.example.adventureescapesa.ui.OverviewFragment

/**
 * Adventure Escape SA - Main Host Activity
 *
 * Hosts the persistent BottomNavigationView with four items:
 *   1. Home
 *   2. Overview
 *   3. Calculate
 *   4. Contact
 *
 * Screens like "About Us" and "Individual Activity Detail" are pushed onto the fragment
 * back stack on top of the current screen.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Draw behind the status bar with dark icons, since the app uses a light theme
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        applySystemBarInsets()
        setupBottomNavigation()

        // Load the Home screen by default on a fresh launch
        if (savedInstanceState == null) {
            loadRootFragment(HomeFragment())
        }

        // Keep the bottom navigation in sync when the user presses back
        supportFragmentManager.addOnBackStackChangedListener {
            syncBottomNavSelection()
        }
    }

    /**
     * Pads the screen so content is not hidden under the status bar.
     * The BottomNavigationView pads itself for the navigation bar.
     */
    private fun applySystemBarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, 0)
            insets
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_overview -> OverviewFragment()
                R.id.nav_calculate -> CalculateFeeFragment()
                R.id.nav_contact -> ContactUsFragment()
                else -> null
            }
            if (fragment != null) {
                clearBackStack()
                loadRootFragment(fragment)
            }
            fragment != null
        }
    }

    /** Selects a bottom navigation tab from code (e.g. a button on the Home screen). */
    fun selectBottomNavItem(navItemId: Int) {
        binding.bottomNavigation.selectedItemId = navItemId
    }

    /** Replaces the main container with a root tab fragment. */
    private fun loadRootFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    /** Pushes a detail or sub-screen on top of the current screen. */
    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    /** Clears any pushed screens back to the current root tab. */
    private fun clearBackStack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    /** Opens the About Us page. */
    fun openAboutUs() {
        pushFragment(AboutUsFragment())
    }

    /** Opens the detail page for one activity. */
    fun openIndividualDetail(activityId: String) {
        pushFragment(IndividualDetailFragment.newInstance(activityId))
    }

    /** Switches to the Contact tab. */
    fun openContactUs() {
        selectBottomNavItem(R.id.nav_contact)
    }

    /** Keeps the highlighted tab correct when the user presses back. */
    private fun syncBottomNavSelection() {
        val navItemId = when (supportFragmentManager.findFragmentById(R.id.fragment_container)) {
            is HomeFragment -> R.id.nav_home
            is OverviewFragment -> R.id.nav_overview
            is CalculateFeeFragment -> R.id.nav_calculate
            is ContactUsFragment -> R.id.nav_contact
            else -> return
        }
        binding.bottomNavigation.menu.findItem(navItemId)?.isChecked = true
    }
}
