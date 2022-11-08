package com.example.pokemonencyclopedia

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.apollographql.apollo3.ApolloClient
import com.example.graphql.PokemonListQuery
import com.example.pokemonencyclopedia.databinding.ActivityKantoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KantoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKantoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKantoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val strArray = resources.getStringArray(R.array.name)
        val apolloClient = ApolloClient.Builder()
            .serverUrl("http://10.120.74.59:8081/graphql")
            .build()

        lifecycleScope.launch(Dispatchers.Main) {
            val res = apolloClient.query(PokemonListQuery()).execute()

            val data = res.data?.findAll
            val list = mutableListOf<PokemonListQuery.FindAll>()
            val nameList = mutableListOf<String>()

            for (i in 0 until 151) {
                list.add(data!![i]!!)
                nameList.add(strArray[i])
            }

            val adapter = PokemonAdapter(list as List<PokemonListQuery.FindAll>, this@KantoActivity, nameList)
            binding.kantoRecyclerView.adapter = adapter
            binding.kantoRecyclerView.layoutManager = GridLayoutManager(this@KantoActivity, 3)

            adapter.itemClick = object :PokemonAdapter.ItemClick {
                override fun onClick(view: View, data: PokemonListQuery.FindAll, position: Int) {
                    startActivity(Intent(this@KantoActivity, PokemonInfoActivity::class.java)
                        .putExtra("dataId", data.id)
                        .putExtra("dataName", nameList[position])
                        .putExtra("dataImg", data.front_default)
                        .putStringArrayListExtra("dataTypes", data.types as ArrayList<String>)
                    )
                }
            }
        }
    }
}