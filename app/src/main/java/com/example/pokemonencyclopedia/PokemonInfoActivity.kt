package com.example.pokemonencyclopedia

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.bumptech.glide.Glide
import com.example.graphql.FindPokemonByNameQuery
import com.example.graphql.PokemonListQuery
import com.example.pokemonencyclopedia.databinding.ActivityPokemonInfoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class PokemonInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPokemonInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val strArray = resources.getStringArray(R.array.name)
        val id = intent.getIntExtra("dataId", 0)
        val name = intent.getStringExtra("dataName")
        val img = intent.getStringExtra("dataImg")
        val nameEng = intent.getStringExtra("dataNameEng")

        val apolloClient = ApolloClient.Builder()
            .serverUrl("https://graphql-pokeapi.graphcdn.app/#")
            .build()

        lifecycleScope.launch(Dispatchers.Main) {
            val resFront = apolloClient.query(PokemonListQuery(Optional.present(1), Optional.present(id))).execute()
            val resBack = apolloClient.query(PokemonListQuery(Optional.present(1), Optional.present(id-2))).execute()
            val typeRes = apolloClient.query(FindPokemonByNameQuery(nameEng!!)).execute()
            Log.d("TAG", "onCreate type res: ${typeRes.data}")

            if (id -1 == 0) {
                binding.pokemonInfoFront.visibility = View.INVISIBLE
                setViewBack(resFront, img)
            } else if (id +1 == 494) {
                binding.pokemonInfoBack.visibility = View.INVISIBLE
                setViewFront(resBack, img)
            } else {
                setViewFront(resBack, img)
                setViewBack(resFront, img)
            }


            binding.goFrontId.setOnClickListener {
                finish()
                startActivity(intent
                    .putExtra("dataId", resBack.data?.pokemons?.results!![0]?.id)
                    .putExtra("dataName", strArray[resBack.data?.pokemons?.results!![0]?.id!!-1])
                    .putExtra("dataNameEng", resBack.data?.pokemons?.results!![0]?.name)
                    .putExtra("dataImg", resBack.data?.pokemons?.results!![0]?.artwork)
                )
            }

            // ???????????? ?????????
            binding.goBackId.setOnClickListener {
                finish()
                startActivity(intent
                    .putExtra("dataId", resFront.data?.pokemons?.results!![0]?.id)
                    .putExtra("dataName", strArray[resFront.data?.pokemons?.results!![0]?.id!!-1])
                    .putExtra("dataNameEng", resFront.data?.pokemons?.results!![0]?.name)
                    .putExtra("dataImg", resFront.data?.pokemons?.results!![0]?.artwork)
                )
            }

            // ????????? ?????? ??????
            setPokeInfo(img, name, id)

            // ?????? ?????????
            getPokemonType(typeRes)

            // ???, ????????? ?????????
            setPokemonBody(typeRes)
        }
    }

    private fun setPokemonBody(typeRes: ApolloResponse<FindPokemonByNameQuery.Data>) {
        val weight = typeRes.data?.pokemon?.weight.toString()
        val height = typeRes.data?.pokemon?.height.toString()

        if (weight.toInt() < 10) {
            val weightRes = "0." + weight.substring(weight.length-1)
            binding.pokemonWeight.text = "?????????: $weightRes kg"
        } else {
            val weightRes = weight.substring(0, weight.length - 1) + "." + weight.substring(weight.length - 1)
            binding.pokemonWeight.text = "?????????: $weightRes kg"
        }

        if (height.toInt() < 10) {
            val heightRes = "0." + height.substring(height.length-1)
            binding.pokemonHeight.text = "???: $heightRes m"
        } else {
            val heightRes = height.substring(0, height.length-1) + "." + height.substring(height.length-1)
            binding.pokemonHeight.text = "???: $heightRes m"
        }
    }

    private fun getPokemonType(typeRes: ApolloResponse<FindPokemonByNameQuery.Data>) {
        if (typeRes.data?.pokemon?.types?.size == 1)
            setType(pokemonType(typeRes.data?.pokemon, 1)[0])
        else {
            binding.type2CardView.visibility = View.VISIBLE

            setType(pokemonType(typeRes.data?.pokemon, 2)[0])
            setType2(pokemonType(typeRes.data?.pokemon, 2)[1])
        }
    }

    private fun setPokeInfo(img: String?, name: String?, id: Int) {
        Glide.with(this@PokemonInfoActivity)
            .load(img)
            .into(binding.pokemonImg)
        binding.pokemonName.text = "No." + setId(id) + " " + name
    }

    private fun pokemonType(pokemon: FindPokemonByNameQuery.Pokemon?, size: Int): MutableList<String> {
        val typeList = mutableListOf<String>()
        repeat(size) { i ->
            val type = pokemon?.types!![i]?.type?.name
            if (type != null) {
                typeList.add(type)
            }
        }
        return typeList
    }

    private fun setId(id: Int): String {
        return if (id < 10) {
            "00$id"
        } else if (id < 100) {
            "0$id"
        } else "$id"
    }

    private fun setViewFront(resBack: ApolloResponse<PokemonListQuery.Data>, img: String?) {
        Glide
            .with(this@PokemonInfoActivity)
            .load(resBack.data?.pokemons?.results!![0]?.artwork)
            .into(binding.pokemonImgFront)
        binding.pokemonIdFront.text = "No." + setId(resBack.data?.pokemons?.results!![0]?.id!!)
    }

    private fun setViewBack(resFront: ApolloResponse<PokemonListQuery.Data>, img: String?) {
        Glide
            .with(this@PokemonInfoActivity)
            .load(resFront.data?.pokemons?.results!![0]?.artwork)
            .into(binding.pokemonImgBack)
        binding.pokemonIdBack.text = "No." + setId(resFront.data?.pokemons?.results!![0]?.id!!)
    }

    private fun setType(s: String) {
        when (s) {
            "grass" -> {
                binding.pokemonType1.text = "???"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.grass))
            }
            "poison" -> {
                binding.pokemonType1.text = "???"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.poison))
            }
            "normal" -> {
                binding.pokemonType1.text = "??????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.normal))
            }
            "fire" -> {
                binding.pokemonType1.text = "??????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.fire))
            }
            "water" -> {
                binding.pokemonType1.text = "???"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.water))
            }
            "electric" -> {
                binding.pokemonType1.text = "??????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.electric))
            }
            "ice" -> {
                binding.pokemonType1.text = "??????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.ice))
            }
            "fighting" -> {
                binding.pokemonType1.text = "??????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.fighting))
            }
            "ground" -> {
                binding.pokemonType1.text = "???"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.ground))
            }
            "flying" -> {
                binding.pokemonType1.text = "??????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.flying))
            }
            "fairy" -> {
                binding.pokemonType1.text = "?????????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.fairy))
            }
            "psychic" -> {
                binding.pokemonType1.text = "?????????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.psychic))
            }
            "bug" -> {
                binding.pokemonType1.text = "??????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.bug))
            }
            "rock" -> {
                binding.pokemonType1.text = "??????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.rock))
            }
            "ghost" -> {
                binding.pokemonType1.text = "?????????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.ghost))
            }
            "dragon" -> {
                binding.pokemonType1.text = "?????????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.dragon))
            }
            "dark" -> {
                binding.pokemonType1.text = "???"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.dark))
            }
            "steel" -> {
                binding.pokemonType1.text = "??????"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.steel))
            }
        }
    }
    private fun setType2(s: String) {
        when (s) {
            "grass" -> {
                binding.pokemonType2.text = "???"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.grass))
            }
            "poison" -> {
                binding.pokemonType2.text = "???"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.poison))
            }
            "normal" -> {
                binding.pokemonType2.text = "??????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.normal))
            }
            "fire" -> {
                binding.pokemonType2.text = "??????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.fire))
            }
            "water" -> {
                binding.pokemonType2.text = "???"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.water))
            }
            "electric" -> {
                binding.pokemonType2.text = "??????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.electric))
            }
            "ice" -> {
                binding.pokemonType2.text = "??????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.ice))
            }
            "fighting" -> {
                binding.pokemonType2.text = "??????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.fighting))
            }
            "ground" -> {
                binding.pokemonType2.text = "???"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.ground))
            }
            "flying" -> {
                binding.pokemonType2.text = "??????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.flying))
            }
            "fairy" -> {
                binding.pokemonType2.text = "?????????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.fairy))
            }
            "psychic" -> {
                binding.pokemonType2.text = "?????????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.psychic))
            }
            "bug" -> {
                binding.pokemonType2.text = "??????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.bug))
            }
            "rock" -> {
                binding.pokemonType2.text = "??????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.rock))
            }
            "ghost" -> {
                binding.pokemonType2.text = "?????????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.ghost))
            }
            "dragon" -> {
                binding.pokemonType2.text = "?????????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.dragon))
            }
            "dark" -> {
                binding.pokemonType2.text = "???"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.dark))
            }
            "steel" -> {
                binding.pokemonType2.text = "??????"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this,
                    R.color.steel))
            }
        }
    }
}