package com.harush.david.discoapp.ui.feed


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harush.david.discoapp.R
import com.harush.david.discoapp.databinding.ListItemBinding
import com.harush.david.discoapp.ui.model.FeedItem
import kotlin.random.Random


class FeedAdapter(val callback: (FeedItem) -> Unit) :
    ListAdapter<FeedItem, FeedAdapter.FeedItemHolder>(
        MovieDiffCallback()
    ) {

    private lateinit var mInflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemHolder {
        if (!::mInflater.isInitialized) {
            mInflater = LayoutInflater.from(parent.context)
        }

        return FeedItemHolder(mInflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: FeedItemHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    inner class FeedItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemBinding.bind(itemView)

        fun onBind(dataItem: FeedItem) = with(binding) {
            itemView.tag = dataItem
            title.text = dataItem.title
            subtitle.text = dataItem.description

            itemView.setOnClickListener {
                callback(dataItem)
            }

            val random = Random(dataItem.link.hashCode())
            val color = Color.argb(
                150,
                random.nextInt(0, 100),
                random.nextInt(0, 100),
                random.nextInt(0, 100)
            )
            (binding.root as? CardView)?.setCardBackgroundColor(color)
        }
    }
}

// DiffUtil is a utility class that calculates the difference between two lists
// and outputs a list of update operations that converts the first list into the second one.
class MovieDiffCallback : DiffUtil.ItemCallback<FeedItem>() {

    override fun areItemsTheSame(
        oldArticleItem: FeedItem,
        newArticleItem: FeedItem
    ): Boolean {
        return oldArticleItem.hashCode() == newArticleItem.hashCode()
    }

    override fun areContentsTheSame(
        oldArticleItem: FeedItem,
        newArticleItem: FeedItem
    ): Boolean {
        return oldArticleItem == newArticleItem
    }
}


