package com.mirko.menuapp.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mirko.menuapp.viewmodels.DirectoryViewModel
import com.mirko.menuapp.R
import com.mirko.menuapp.databinding.ItemVenueBinding

class VenuesAdapter(private val itemClick: (Int) -> Unit) :
    ListAdapter<DirectoryViewModel.VenueInfo, VenuesAdapter.VenueViewHolder>(VenueInfoDiffCallback()) {

    class VenueInfoDiffCallback : DiffUtil.ItemCallback<DirectoryViewModel.VenueInfo>() {
        override fun areItemsTheSame(
            oldItem: DirectoryViewModel.VenueInfo,
            newItem: DirectoryViewModel.VenueInfo
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DirectoryViewModel.VenueInfo,
            newItem: DirectoryViewModel.VenueInfo
        ): Boolean {
            return oldItem == newItem
        }

    }

    class VenueViewHolder(val binding: ItemVenueBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        return VenueViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_venue,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        holder.binding.venueInfo = getItem(position)
        holder.binding.executePendingBindings()

        holder.itemView.setOnClickListener {
            itemClick.invoke(position)
        }
    }

}

