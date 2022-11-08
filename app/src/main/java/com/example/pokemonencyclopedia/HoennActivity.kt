package com.example.pokemonencyclopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.apollographql.apollo3.ApolloClient
import com.example.graphql.PokemonListQuery
import com.example.pokemonencyclopedia.databinding.ActivityHoennBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HoennActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHoennBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHoennBinding.inflate(layoutInflater)
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

            for (i in 251 until 386) {
                list.add(data!![i]!!)
                nameList.add(strArray[i])
            }

            val adapter = PokemonAdapter(list, this@HoennActivity, nameList)
            binding.hoennRecyclerView.adapter = adapter
            binding.hoennRecyclerView.layoutManager = GridLayoutManager(this@HoennActivity, 3)

            adapter.itemClick = object :PokemonAdapter.ItemClick {
                override fun onClick(view: View, data: PokemonListQuery.FindAll, position: Int) {
                    startActivity(Intent(this@HoennActivity, PokemonInfoActivity::class.java)
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