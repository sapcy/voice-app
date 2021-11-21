package com.sychoi.melodyapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sychoi.melodyapp.R
import com.sychoi.melodyapp.data.model.Voice
import com.sychoi.melodyapp.databinding.ItemLayoutBinding

class MainAdapter(private val voices: ArrayList<Voice>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(voice: Voice) {
            binding.ivThumbnail.setImageResource(voice.img)
            binding.tvTitle.text = voice.title
            binding.tvDesc.text = voice.desc
            binding.tvPlayTime.text = voice.playtime

//            Glide.with(binding.ivThumbnail.context)
//                .load(music.avatar)
//                .into(binding.ivThumbnail)

            binding.tvOptions.setOnClickListener {
                val popupMenu: PopupMenu = PopupMenu(binding.root.context, binding.tvOptions)
                popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.action_analysis ->
                            Toast.makeText(binding.root.context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        R.id.action_detail ->
                            Toast.makeText(binding.root.context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        R.id.action_remove ->
                            Toast.makeText(binding.root.context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    true
                })
                popupMenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return voices.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(voices[position])
    }

    fun addData(list: List<Voice>) {
        voices.addAll(list)
    }

}