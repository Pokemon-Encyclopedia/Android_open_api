package com.example.pokemonencyclopedia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.graphql.PokemonListQuery
import com.example.pokemonencyclopedia.adapter.PokemonAdapter
import com.example.pokemonencyclopedia.adapter.SpacesItemDecoration
import com.example.pokemonencyclopedia.databinding.ActivitySaintBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaintActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaintBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySaintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val strArray = resources.getStringArray(R.array.saint_name)
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://graphql-pokeapi.graphcdn.app/#")
            .build()

        lifecycleScope.launch(Dispatchers.Main) {
            val res = apolloClient.query(PokemonListQuery(Optional.present(100), Optional.present(151))).execute()

            val data = res.data?.pokemons?.results
            val list = mutableListOf<PokemonListQuery.Result>()
            val nameList = mutableListOf<String>()

            for (i in strArray.indices) {
                list.add(data!![i]!!)
                nameList.add(strArray[i])
            }

            val adapter = PokemonAdapter(list, this@SaintActivity, nameList)
            binding.saintRecyclerView.adapter = adapter
            binding.saintRecyclerView.layoutManager = GridLayoutManager(this@SaintActivity, 3)
            binding.saintRecyclerView.addItemDecoration(SpacesItemDecoration(20))

            adapter.itemClick = object : PokemonAdapter.ItemClick {
                override fun onClick(view: View, result: PokemonListQuery.Result, position: Int) {
                    startActivity(Intent(this@SaintActivity, PokemonInfoActivity::class.java)
                        .putExtra("dataId", result.id)
                        .putExtra("dataName", nameList[position])
                        .putExtra("dataNameEng", result.name)
                        .putExtra("dataImg", result.artwork)
                    )
                }
            }
        }
    }
}