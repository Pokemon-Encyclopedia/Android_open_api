package com.example.pokemonencyclopedia.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.graphql.PokemonListQuery
import com.example.pokemonencyclopedia.databinding.PokemonItemBinding

class PokemonAdapter(private val list: List<PokemonListQuery.Result>, private val context: Context, private val name: MutableList<String>
): RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = PokemonItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(view)
    }

    interface ItemClick {
        fun onClick(view: View, result: PokemonListQuery.Result, position: Int)
    }
    var itemClick: ItemClick? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], name[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: PokemonItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: PokemonListQuery.Result, name: String, position: Int) {
            Glide
                .with(context)
                .load(result.artwork)
                .into(binding.image)

            val id = result.id
            if (id!! < 10)
                binding.name.text = "No.00$id $name"
            else if (id < 100)
                binding.name.text = "No.0$id $name"
            else binding.name.text = "No.$id $name"

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                binding.image.setOnClickListener {
                    itemClick?.onClick(itemView, result, position)
                }
            }
        }
    }
}