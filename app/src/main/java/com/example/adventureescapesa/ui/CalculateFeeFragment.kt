package com.example.adventureescapesa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.adventureescapesa.MainActivity
import com.example.adventureescapesa.R
import com.example.adventureescapesa.adapter.CalculateFeeAdapter
import com.example.adventureescapesa.databinding.FragmentCalculateFeeBinding
import com.example.adventureescapesa.model.BookingManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Screen 5: The Calculate Fee Page
 *
 * Calculates a booking quotation from the selected packages/activities:
 *   - Number-of-people stepper (- / +) per booking card
 *   - Discount-tier progress bar based on the number of bookings:
 *       • 1 booking = no discount
 *       • 2 bookings = 5% discount
 *       • 3 bookings = 10% discount
 *       • More than 3 bookings = 15% discount
 *   - Quotation summary card with subtotal, discount and grand total
 *   - "Proceed to Booking" confirmation flow
 */
class CalculateFeeFragment : Fragment() {

    private var _binding: FragmentCalculateFeeBinding? = null
    private val binding get() = _binding!!

    private lateinit var calculateAdapter: CalculateFeeAdapter

    private val bookingListener = {
        updateFeeSummary()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculateFeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupProceedButton()
        updateFeeSummary()

        BookingManager.addListener(bookingListener)
    }

    private fun setupRecyclerView() {
        calculateAdapter = CalculateFeeAdapter(
            onQuantityChanged = { cartItem, newQty ->
                BookingManager.updateQuantity(cartItem.item.id, newQty)
            },
            onItemRemoved = { cartItem ->
                BookingManager.removeItem(cartItem.item.id)
            }
        )

        binding.rvSelectedItems.adapter = calculateAdapter
    }

    /**
     * Recalculates subtotal, discount, progress indicator and total whenever the cart changes.
     */
    private fun updateFeeSummary() {
        val cartItems = BookingManager.cartItems
        val bookings = BookingManager.bookingCount
        val discountPercent = BookingManager.discountPercent

        calculateAdapter.submitList(cartItems)

        // Show/hide the empty state
        binding.tvEmptyCart.isVisible = cartItems.isEmpty()
        binding.rvSelectedItems.isVisible = cartItems.isNotEmpty()

        // ================= DISCOUNT PROGRESS BAR =================
        with(binding) {
            progressDiscountTier.max = MAX_TIER_BOOKINGS
            progressDiscountTier.progress = bookings.coerceAtMost(MAX_TIER_BOOKINGS)

            tvDiscountTierName.setText(
                when {
                    bookings >= MAX_TIER_BOOKINGS -> R.string.calc_tier_trailblazer
                    bookings == 3 -> R.string.calc_tier_adventurer
                    bookings == 2 -> R.string.calc_tier_explorer
                    else -> R.string.calc_tier_standard
                }
            )

            if (discountPercent > 0) {
                tvDiscountBadge.text = getString(R.string.calc_tier_badge_applied, discountPercent)
            } else {
                tvDiscountBadge.setText(R.string.calc_tier_inactive)
            }

            tvProgressCurrentStatus.text =
                resources.getQuantityString(R.plurals.calc_progress_bookings, bookings, bookings)

            if (bookings >= MAX_TIER_BOOKINGS) {
                tvDiscountTierDesc.text = getString(R.string.calc_tier_max_desc, discountPercent)
                tvProgressTargetHint.setText(R.string.calc_tier_highest)
            } else {
                val nextPercent = BookingManager.discountPercentFor(bookings + 1)
                tvDiscountTierDesc.text = getString(R.string.calc_tier_next_desc, nextPercent)
                tvProgressTargetHint.text = getString(R.string.calc_progress_target, nextPercent)
            }

            // ================= SUMMARY CARD =================
            tvSummarySubtotal.text = getString(R.string.price_with_cents, BookingManager.subtotal)
            layoutDiscountRow.isVisible = discountPercent > 0
            if (discountPercent > 0) {
                tvSummaryDiscountLabel.text = getString(R.string.calc_discount_label_format, discountPercent)
                tvSummaryDiscountVal.text =
                    getString(R.string.price_discount_with_cents, BookingManager.discountAmount)
            }
            tvSummaryGrandTotal.text = getString(R.string.price_with_cents, BookingManager.totalCost)
        }
    }

    /**
     * Sets up the "Proceed to Booking" button and its confirmation dialog.
     */
    private fun setupProceedButton() {
        binding.btnProceedBooking.setOnClickListener {
            if (BookingManager.cartItems.isEmpty()) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.calc_empty_dialog_title)
                    .setMessage(R.string.calc_empty_dialog_message)
                    .setPositiveButton(R.string.home_btn_browse) { _, _ ->
                        (activity as? MainActivity)?.selectBottomNavItem(R.id.nav_overview)
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
                return@setOnClickListener
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.calc_confirm_dialog_title)
                .setMessage(buildQuotationMessage())
                .setPositiveButton(R.string.contact_title) { _, _ ->
                    (activity as? MainActivity)?.openContactUs()
                }
                .setNegativeButton(R.string.calc_btn_edit, null)
                .show()
        }
    }

    /** Builds the quotation breakdown shown in the confirmation dialog. */
    private fun buildQuotationMessage(): String = buildString {
        append(getString(R.string.calc_quote_header))
        append("\n\n")
        BookingManager.cartItems.forEach {
            append(getString(R.string.calc_quote_line, it.item.title, it.quantity, it.totalCost))
            append('\n')
        }
        if (BookingManager.discountPercent > 0) {
            append('\n')
            append(
                getString(
                    R.string.calc_quote_discount,
                    BookingManager.discountPercent,
                    BookingManager.discountAmount
                )
            )
            append('\n')
        }
        append('\n')
        append(getString(R.string.calc_quote_total, BookingManager.totalCost))
        append("\n\n")
        append(getString(R.string.calc_quote_question))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BookingManager.removeListener(bookingListener)
        _binding = null
    }

    private companion object {
        /** "More than 3 bookings" is the highest discount tier. */
        const val MAX_TIER_BOOKINGS = 4
    }
}
