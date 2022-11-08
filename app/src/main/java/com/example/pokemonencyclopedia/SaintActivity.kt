package com.example.pokemonencyclopedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.apollographql.apollo3.ApolloClient
import com.example.graphql.PokemonListQuery
import com.example.pokemonencyclopedia.databinding.ActivitySaintBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaintActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaintBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySaintBinding.inflate(layoutInflater)
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

            for (i in 151 until 251) {
                list.add(data!![i]!!)
                nameList.add(strArray[i])
            }

            val adapter = PokemonAdapter(list, this@SaintActivity, nameList)
            binding.saintRecyclerView.adapter = adapter
            binding.saintRecyclerView.layoutManager = GridLayoutManager(this@SaintActivity, 3)

            adapter.itemClick = object :PokemonAdapter.ItemClick {
                override fun onClick(view: View, data: PokemonListQuery.FindAll, position: Int) {
                    startActivity(Intent(this@SaintActivity, PokemonInfoActivity::class.java)
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