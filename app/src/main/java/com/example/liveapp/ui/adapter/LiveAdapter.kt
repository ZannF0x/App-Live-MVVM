package com.example.liveapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.liveapp.R
import com.example.liveapp.databinding.LiveItemLayoutBinding
import com.example.liveapp.model.Live

class LiveAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfLives: List<Live> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = LiveItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return LiveViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is LiveViewHolder -> {
                holder.bind(listOfLives[position])
            }
        }
    }

    override fun getItemCount(): Int = listOfLives.size

    fun setList(list: List<Live>) {
        this.listOfLives = list
    }

    inner class LiveViewHolder(itemBinding: LiveItemLayoutBinding): RecyclerView.ViewHolder(itemBinding.root) {

        private val title = itemBinding.textLiveTitle
        private val author = itemBinding.textLiveAuthor
        private val thumbNail = itemBinding.imageLive
        private val container = itemBinding.liveContainer

        fun bind(live: Live) {
            title.text = live.title
            author.text = live.author
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.loading_icon)
                .error(R.drawable.error_icon)
            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(live.thumbnailUrl)
                .into(thumbNail)
            container.setOnClickListener {
                onClickListener.onLiveClick(live)
            }
        }
    }

    interface OnClickListener {
        fun onLiveClick(itemClicked: Live)
    }
}