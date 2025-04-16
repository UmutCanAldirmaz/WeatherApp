package com.hopecoding.weatherapp.presentation.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hopecoding.weatherapp.R
import com.hopecoding.weatherapp.databinding.ItemWeatherCardBinding
import com.hopecoding.weatherapp.domain.model.WeatherCard

class WeatherCardAdapter(
    private val onCardClick: (Int) -> Unit
) : RecyclerView.Adapter<WeatherCardAdapter.WeatherCardViewHolder>() {

    private var cards: List<WeatherCard> = emptyList()

    inner class WeatherCardViewHolder(
        private val binding: ItemWeatherCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && cards.isNotEmpty()) {
                    cards.forEachIndexed { index, card ->
                        card.isSelected = index == position
                    }
                    notifyDataSetChanged() // Performans için daha sonra optimize edilebilir
                    onCardClick(position)
                }
            }
        }

        fun bind(card: WeatherCard) {
            binding.apply {
                timeText.text = card.time
                temperatureText.text = card.temperature

                val iconUrl = "https://openweathermap.org/img/wn/${card.iconCode}@2x.png"
                weatherIcon.load(iconUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                    error(R.drawable.ic_launcher_foreground)
                }

                val backgroundColor = if (card.isSelected) {
                    R.color.orange
                } else {
                    R.color.darkGray
                }

                root.setCardBackgroundColor(
                    ContextCompat.getColor(root.context, backgroundColor)
                )

                val params = root.layoutParams
                params.height = root.context.resources.getDimensionPixelSize(
                    if (card.isSelected) R.dimen.card_height_selected else R.dimen.card_height_normal
                )
                root.layoutParams = params
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherCardViewHolder {
        val binding = ItemWeatherCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeatherCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherCardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount() = cards.size

    fun updateCards(newCards: List<WeatherCard>) {
        Log.d("WeatherCardAdapter", "updateCards: newCards size = ${newCards.size}")
        val diffCallback = WeatherCardDiffCallback(cards, newCards)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        cards = newCards
        diffResult.dispatchUpdatesTo(this)
    }

    private class WeatherCardDiffCallback(
        private val oldList: List<WeatherCard>,
        private val newList: List<WeatherCard>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].time == newList[newItemPosition].time
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}