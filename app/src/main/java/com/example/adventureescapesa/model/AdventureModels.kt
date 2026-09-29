package com.example.adventureescapesa.model

/**
 * Data model for an adventure experience or multi-day package.
 * Add or modify activities in [BookingManager.sampleAdventures].
 *
 * @param id Unique identifier for the activity/package
 * @param title Display name of the experience
 * @param category Category tag shown as a badge (e.g. "Adventure Package")
 * @param price Base price per person in ZAR (South African Rand)
 * @param imageResId Drawable resource ID for the preview image
 * @param duration Estimated duration or number of days
 * @param description Detailed description of the excursion
 * @param isPackage True if multi-activity package, false if single activity
 * @param includedItems Items/services included in this booking
 */
data class AdventureItem(
    val id: String,
    val title: String,
    val category: String,
    val price: Double,
    val imageResId: Int,
    val duration: String,
    val description: String,
    val isPackage: Boolean = false,
    val includedItems: List<String> = emptyList()
)

/**
 * An item selected in the customer's quotation, with the chosen number of people.
 * Immutable: [BookingManager] replaces it with a copy when the quantity changes.
 */
data class BookingCartItem(
    val item: AdventureItem,
    val quantity: Int = 1
) {
    val totalCost: Double
        get() = item.price * quantity
}
