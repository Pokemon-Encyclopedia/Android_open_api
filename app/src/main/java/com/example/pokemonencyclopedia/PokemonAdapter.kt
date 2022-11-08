package com.example.pokemonencyclopedia

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graphql.PokemonListQuery
import com.example.pokemonencyclopedia.databinding.PokemonItemBinding

class PokemonAdapter(private val list: List<PokemonListQuery.FindAll>, private val context: Context, private val name: MutableList<String>): RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = PokemonItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    interface ItemClick {
        fun onClick(view: View, data: PokemonListQuery.FindAll, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], name[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: PokemonItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(findAll: PokemonListQuery.FindAll, name: String) {
            Glide
                .with(context)
                .load(findAll.front_default)
                .into(binding.image)

            val id = findAll.id
            if (id?.toInt()!! < 10)
                binding.name.text = "No.00$id $name"
            else if (id.toInt() < 100)
                binding.name.text = "No.0$id $name"
            else {
                Log.d("ITEM", "bind: $name")
                binding.name.text = "No.$id $name"
            }

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                binding.image.setOnClickListener {
                    itemClick?.onClick(itemView, findAll, pos)
                }
            }
        }
    }
}