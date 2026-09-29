package com.example.adventureescapesa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.adventureescapesa.R
import com.example.adventureescapesa.databinding.ItemCalculateCardBinding
import com.example.adventureescapesa.model.BookingCartItem

/**
 * RecyclerView adapter for the Calculate Fee page.
 * Shows each selected activity/package with a quantity stepper (- / +),
 * unit price, line subtotal and a remove button.
 */
class CalculateFeeAdapter(
    private val onQuantityChanged: (BookingCartItem, Int) -> Unit,
    private val onItemRemoved: (BookingCartItem) -> Unit
) : ListAdapter<BookingCartItem, CalculateFeeAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemCalculateCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalculateCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = getItem(position)
        with(holder.binding) {
            val context = root.context
            tvCalcItemTitle.text = cartItem.item.title
            tvCalcUnitPrice.text = context.getString(R.string.price_per_person, cartItem.item.price)
            tvCalcQuantity.text = context.getString(R.string.quantity_value, cartItem.quantity)
            tvCalcLineTotal.text = context.getString(R.string.price_whole_rand, cartItem.totalCost)

            // Decrease quantity (removes the item when it reaches zero)
            btnCalcMinus.setOnClickListener {
                val newQty = cartItem.quantity - 1
                if (newQty > 0) {
                    onQuantityChanged(cartItem, newQty)
                } else {
                    onItemRemoved(cartItem)
                }
            }

            // Increase quantity
            btnCalcPlus.setOnClickListener {
                onQuantityChanged(cartItem, cartItem.quantity + 1)
            }

            // Remove item
            btnCalcDelete.setOnClickListener {
                onItemRemoved(cartItem)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<BookingCartItem>() {
        override fun areItemsTheSame(oldItem: BookingCartItem, newItem: BookingCartItem): Boolean =
            oldItem.item.id == newItem.item.id

        override fun areContentsTheSame(oldItem: BookingCartItem, newItem: BookingCartItem): Boolean =
            oldItem == newItem
    }
}
