package com.cheise_proj.parent_feature.ui.video.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cheise_proj.parent_feature.AdapterClickListener
import com.cheise_proj.parent_feature.R
import com.cheise_proj.presentation.GlideApp
import com.cheise_proj.presentation.model.files.Video
import kotlinx.android.synthetic.main.list_files.view.*

class VideoAdapter :
    ListAdapter<Video, VideoAdapter.VideoVh>(VideoDiffCallback()) {
    private var adapterClickListener: AdapterClickListener<Pair<String?, Boolean>>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVh {
        return VideoVh(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_files,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VideoVh, position: Int) {
        holder.bind(getItem(position), adapterClickListener)
    }

    fun setAdapterClickListener(callback: AdapterClickListener<Pair<String?, Boolean>>) {
        adapterClickListener = callback
    }

    inner class VideoVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Video, adapterClickListener: AdapterClickListener<Pair<String?, Boolean>>?) {
            val header = "From: ${item.teacherName}"
            val subHeader = "Date: ${item.date}"
            itemView.tv_header.text = header
            itemView.tv_sub_header.text = subHeader
            itemView.avatar_image.apply {
                GlideApp.with(this.context)
                    .load(itemView.context.getDrawable(R.drawable.vide_thumbnail)).thumbnail(0.5f)
                    .centerCrop()
                    .into(this)
            }
            itemView.setOnClickListener {
                adapterClickListener?.onClick(Pair(item.photo, false))
            }
            itemView.btn_download.setOnClickListener {
                adapterClickListener?.onClick(Pair(item.photo, true))
            }
        }
    }
}

class VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
    override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
        return oldItem == newItem
    }
}