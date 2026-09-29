package com.example.adventureescapesa.model

import com.example.adventureescapesa.R

/**
 * Shared booking state and catalogue for Adventure Escape SA.
 * Keeps the customer's quotation in memory while the app is open.
 */
object BookingManager {

    /**
     * Catalogue of Adventure Packages and Individual Activities from the client brief.
     * Change a price here and every screen and total updates automatically.
     */
    val sampleAdventures = listOf(
        AdventureItem(
            id = "pkg_ultimate_day",
            title = "Ultimate Adventure Day",
            category = "Adventure Package",
            price = 1500.0,
            imageResId = R.drawable.placeholder_hiking,
            duration = "Full Day • Multi-Activity",
            description = "A full day outdoor adventure featuring multiple exciting activities.",
            isPackage = true,
            includedItems = listOf(
                "Guided hiking trail",
                "Zip lining",
                "Kayaking",
                "Lunch",
                "Safety briefing and equipment"
            )
        ),
        AdventureItem(
            id = "pkg_family_explorer",
            title = "Family Explorer Package",
            category = "Adventure Package",
            price = 1500.0,
            imageResId = R.drawable.placeholder_team,
            duration = "Full Day • All Ages",
            description = "A fun-filled outdoor experience designed for families.",
            isPackage = true,
            includedItems = listOf(
                "Nature walk",
                "Obstacle course",
                "Picnic area",
                "Family games",
                "Guided wildlife spotting"
            )
        ),
        AdventureItem(
            id = "pkg_mountain_adventure",
            title = "Mountain Adventure Package",
            category = "Adventure Package",
            price = 1500.0,
            imageResId = R.drawable.placeholder_abseil,
            duration = "Full Day • Mountain",
            description = "A guided mountain adventure for outdoor enthusiasts.",
            isPackage = true,
            includedItems = listOf(
                "Mountain hiking",
                "Scenic viewpoints",
                "Rock scrambling",
                "Safety equipment",
                "Professional guide"
            )
        ),
        AdventureItem(
            id = "pkg_corporate_challenge",
            title = "Corporate Team Challenge",
            category = "Adventure Package",
            price = 1500.0,
            imageResId = R.drawable.placeholder_team,
            duration = "Full Day • Team Building",
            description = "Team-building activities designed for businesses and organisations.",
            isPackage = true,
            includedItems = listOf(
                "Team obstacle course",
                "Orienteering challenge",
                "Raft building activity",
                "Leadership exercises",
                "Team awards"
            )
        ),
        AdventureItem(
            id = "act_zip lining",
            title = "Zip lining Adventure",
            category = "Individual Activity",
            price = 750.0,
            imageResId = R.drawable.placeholder_hiking,
            duration = "Half Day • Forest",
            description = "Experience breathtaking views while zip lining through the forest.",
            isPackage = false,
            includedItems = listOf(
                "Safety briefing",
                "Equipment hire",
                "Professional instructors"
            )
        ),
        AdventureItem(
            id = "act_kayaking",
            title = "Kayaking Experience",
            category = "Individual Activity",
            price = 750.0,
            imageResId = R.drawable.placeholder_kayaking,
            duration = "Half Day • Rivers & Lakes",
            description = "Paddle through scenic rivers and lakes.",
            isPackage = false,
            includedItems = listOf(
                "Kayak and paddle",
                "Safety equipment",
                "Guided route"
            )
        ),
        AdventureItem(
            id = "act_rock_climbing",
            title = "Rock Climbing Session",
            category = "Individual Activity",
            price = 750.0,
            imageResId = R.drawable.placeholder_abseil,
            duration = "Half Day • Rock Faces",
            description = "Learn climbing techniques on natural rock faces.",
            isPackage = false,
            includedItems = listOf(
                "Climbing equipment",
                "Safety instructions",
                "Professional guide"
            )
        )
    )

    // Current in-memory cart items for the customer's quotation
    private val _cartItems = mutableListOf<BookingCartItem>()

    /** A snapshot copy of the cart, safe to hand to a list adapter. */
    val cartItems: List<BookingCartItem> get() = _cartItems.toList()

    // Callbacks for cart updates
    private val listeners = mutableListOf<() -> Unit>()

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyChanged() {
        listeners.forEach { it.invoke() }
    }

    /** Adds one person for [adventure], or adds the item if it is not in the cart yet. */
    fun addItem(adventure: AdventureItem) {
        val index = _cartItems.indexOfFirst { it.item.id == adventure.id }
        if (index >= 0) {
            val existing = _cartItems[index]
            _cartItems[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            _cartItems.add(BookingCartItem(adventure, 1))
        }
        notifyChanged()
    }

    fun updateQuantity(itemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeItem(itemId)
            return
        }
        val index = _cartItems.indexOfFirst { it.item.id == itemId }
        if (index >= 0) {
            _cartItems[index] = _cartItems[index].copy(quantity = newQuantity)
            notifyChanged()
        }
    }

    fun removeItem(itemId: String) {
        _cartItems.removeAll { it.item.id == itemId }
        notifyChanged()
    }

    /** Number of different packages/activities in the quote. This drives the discount. */
    val bookingCount: Int
        get() = _cartItems.size

    val subtotal: Double
        get() = _cartItems.sumOf { it.totalCost }

    /**
     * Discount rules from the client brief:
     * 1 booking: no discount
     * 2 bookings: 5% discount
     * 3 bookings: 10% discount
     * More than 3 bookings: 15% discount
     */
    val discountPercent: Int
        get() = discountPercentFor(bookingCount)

    fun discountPercentFor(bookings: Int): Int = when {
        bookings > 3 -> 15
        bookings == 3 -> 10
        bookings == 2 -> 5
        else -> 0
    }

    val discountAmount: Double
        get() = subtotal * (discountPercent / 100.0)

    val totalCost: Double
        get() = subtotal - discountAmount

    /** Empties the quote. */
    fun clear() {
        _cartItems.clear()
        notifyChanged()
    }
}
