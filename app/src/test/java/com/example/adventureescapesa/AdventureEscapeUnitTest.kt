package com.example.adventureescapesa

import com.example.adventureescapesa.model.BookingManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for Adventure Escape SA business logic:
 * - Catalogue matches the client brief
 * - Discount tiers (2 = 5%, 3 = 10%, more than 3 = 15%)
 * - Quotation totals
 */
class AdventureEscapeUnitTest {

    @Before
    fun resetQuote() {
        BookingManager.clear()
    }

    @Test
    fun catalogueMatchesBrief() {
        val packages = BookingManager.sampleAdventures.filter { it.isPackage }
        val activities = BookingManager.sampleAdventures.filter { !it.isPackage }

        assertEquals(4, packages.size)
        assertEquals(3, activities.size)
        packages.forEach { assertEquals(1500.0, it.price, 0.001) }
        activities.forEach { assertEquals(750.0, it.price, 0.001) }
    }

    @Test
    fun discountTiersMatchBrief() {
        assertEquals(0, BookingManager.discountPercentFor(0))
        assertEquals(0, BookingManager.discountPercentFor(1))
        assertEquals(5, BookingManager.discountPercentFor(2))
        assertEquals(10, BookingManager.discountPercentFor(3))
        assertEquals(15, BookingManager.discountPercentFor(4))
        assertEquals(15, BookingManager.discountPercentFor(7))
    }

    @Test
    fun quotationTotalsAreCalculatedPerBooking() {
        val ultimateDay = BookingManager.sampleAdventures.first { it.id == "pkg_ultimate_day" }
        val kayaking = BookingManager.sampleAdventures.first { it.id == "act_kayaking" }

        // Ultimate Adventure Day for 2 people + Kayaking for 1 person = 2 bookings
        BookingManager.addItem(ultimateDay)
        BookingManager.addItem(ultimateDay)
        BookingManager.addItem(kayaking)

        assertEquals(2, BookingManager.bookingCount)
        assertEquals(5, BookingManager.discountPercent)

        val expectedSubtotal = (2 * 1500.0) + 750.0 // R3750
        assertEquals(expectedSubtotal, BookingManager.subtotal, 0.01)
        assertEquals(expectedSubtotal * 0.05, BookingManager.discountAmount, 0.01)
        assertEquals(expectedSubtotal * 0.95, BookingManager.totalCost, 0.01)
    }
}
