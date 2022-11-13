package com.example.pokemonencyclopedia.localFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.graphql.PokemonListQuery
import com.example.pokemonencyclopedia.PokemonInfoActivity
import com.example.pokemonencyclopedia.R
import com.example.pokemonencyclopedia.adapter.PokemonAdapter
import com.example.pokemonencyclopedia.adapter.SpacesItemDecoration
import com.example.pokemonencyclopedia.databinding.FragmentHanaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HanaFragment : Fragment() {
    private lateinit var binding: FragmentHanaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHanaBinding.inflate(layoutInflater)

        val strArray = resources.getStringArray(R.array.hana_name)
        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://graphql-pokeapi.graphcdn.app/#")
            .build()

        lifecycleScope.launch(Dispatchers.Main) {
            val res = apolloClient.query(PokemonListQuery(Optional.present(156), Optional.present(493))).execute()

            val data = res.data?.pokemons?.results
            val list = mutableListOf<PokemonListQuery.Result>()
            val nameList = mutableListOf<String>()

            for (i in strArray.indices) {
                list.add(data!![i]!!)
                nameList.add(strArray[i])
            }

            val adapter = PokemonAdapter(list, requireContext(), nameList)
            binding.hanaRecyclerView.adapter = adapter
            binding.hanaRecyclerView.layoutManager = GridLayoutManager(context, 3)
            binding.hanaRecyclerView.addItemDecoration(SpacesItemDecoration(10))

            adapter.itemClick = object : PokemonAdapter.ItemClick {
                override fun onClick(view: View, result: PokemonListQuery.Result, position: Int) {
                    startActivity(Intent(context, PokemonInfoActivity::class.java)
                        .putExtra("dataId", result.id)
                        .putExtra("dataName", nameList[position])
                        .putExtra("dataNameEng", result.name)
                        .putExtra("dataImg", result.artwork)
                    )
                }
            }
        }

        return binding.root
    }
}