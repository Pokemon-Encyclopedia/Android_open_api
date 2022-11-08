package com.example.pokemonencyclopedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.bumptech.glide.Glide
import com.example.graphql.FindPokemonByIdQuery
import com.example.pokemonencyclopedia.databinding.ActivityPokemonInfoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPokemonInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val strArray = resources.getStringArray(R.array.name)
        val id = intent.getStringExtra("dataId")
        val name = intent.getStringExtra("dataName")
        val img = intent.getStringExtra("dataImg")
        val type = intent.getStringArrayListExtra("dataTypes")

        val apolloClient = ApolloClient.Builder()
            .serverUrl("http://10.120.74.59:8081/graphql")
            .build()

        lifecycleScope.launch(Dispatchers.Main) {
            val resFront = apolloClient.query(FindPokemonByIdQuery("${id?.toInt()?.plus(1)}")).execute()
            val resBack = apolloClient.query(FindPokemonByIdQuery("${id?.toInt()?.minus(1)}")).execute()

            binding.goFrontId.setOnClickListener {
                finish()
                startActivity(intent
                    .putExtra("dataId", resBack.data?.findPokemonById?.id)
                    .putExtra("dataName", strArray[resBack.data?.findPokemonById?.id!!.toInt()-1])
                    .putExtra("dataImg", resBack.data?.findPokemonById?.front_default)
                    .putExtra("dataTypes", resBack.data?.findPokemonById?.types as ArrayList<String>)
                )
            }
            binding.goBackId.setOnClickListener {
                finish()
                startActivity(intent
                    .putExtra("dataId", resFront.data?.findPokemonById?.id)
                    .putExtra("dataName", strArray[resFront.data?.findPokemonById?.id!!.toInt()-1])
                    .putExtra("dataImg", resFront.data?.findPokemonById?.front_default)
                    .putExtra("dataTypes", resFront.data?.findPokemonById?.types as ArrayList<String>)
                )
            }

            if (id!!.toInt()-1 == 0) {
                binding.pokemonInfoFront.visibility = View.INVISIBLE
                setViewBack(resFront)
            } else if (id.toInt()+1 == 387) {
                binding.pokemonInfoBack.visibility = View.INVISIBLE
                setViewFront(resBack)
            } else {
                setViewFront(resBack)
                setViewBack(resFront)
            }
        }

        Glide.with(this)
            .load(img)
            .into(binding.pokemonImg)
        binding.pokemonName.text = "No." + setId(id) + " " + name
        if (type?.size == 1) {
            setType(type[0])
        } else {
            binding.type2CardView.visibility = View.VISIBLE

            setType(type!![0])
            setType2(type[1])
        }
    }

    private fun setId(id: String?): String {
        return if (id?.toInt()!! < 10) {
            "00$id"
        } else if (id.toInt() < 100) {
            "0$id"
        } else "$id"
    }

    private fun setViewFront(resBack: ApolloResponse<FindPokemonByIdQuery.Data>) {
        Glide
            .with(this@PokemonInfoActivity)
            .load(resBack.data?.findPokemonById?.front_default)
            .into(binding.pokemonImgFront)
        binding.pokemonIdFront.text = "No."+setId(resBack.data?.findPokemonById?.id)
    }

    private fun setViewBack(resFront: ApolloResponse<FindPokemonByIdQuery.Data>) {
        Glide
            .with(this@PokemonInfoActivity)
            .load(resFront.data?.findPokemonById?.front_default)
            .into(binding.pokemonImgBack)
        binding.pokemonIdBack.text = "No."+setId(resFront.data?.findPokemonById?.id)
    }

    private fun setType(s: String) {
        when (s) {
            "grass" -> {
                binding.pokemonType1.text = "풀"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grass))
            }
            "poison" -> {
                binding.pokemonType1.text = "독"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.poison))
            }
            "normal" -> {
                binding.pokemonType1.text = "노말"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.normal))
            }
            "fire" -> {
                binding.pokemonType1.text = "불꽃"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.fire))
            }
            "water" -> {
                binding.pokemonType1.text = "물"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.water))
            }
            "electric" -> {
                binding.pokemonType1.text = "전기"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.electric))
            }
            "ice" -> {
                binding.pokemonType1.text = "얼음"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ice))
            }
            "fighting" -> {
                binding.pokemonType1.text = "격투"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.fighting))
            }
            "ground" -> {
                binding.pokemonType1.text = "땅"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ground))
            }
            "flying" -> {
                binding.pokemonType1.text = "비행"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.flying))
            }
            "fairy" -> {
                binding.pokemonType1.text = "페어리"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.fairy))
            }
            "psychic" -> {
                binding.pokemonType1.text = "에스퍼"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.psychic))
            }
            "bug" -> {
                binding.pokemonType1.text = "벌레"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.bug))
            }
            "rock" -> {
                binding.pokemonType1.text = "바위"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.rock))
            }
            "ghost" -> {
                binding.pokemonType1.text = "고스트"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ghost))
            }
            "dragon" -> {
                binding.pokemonType1.text = "드래곤"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.dragon))
            }
            "dark" -> {
                binding.pokemonType1.text = "악"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.dark))
            }
            "steel" -> {
                binding.pokemonType1.text = "강철"
                binding.type1CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.steel))
            }
        }
    }
    private fun setType2(s: String) {
        when (s) {
            "grass" -> {
                binding.pokemonType2.text = "풀"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.grass))
            }
            "poison" -> {
                binding.pokemonType2.text = "독"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.poison))
            }
            "normal" -> {
                binding.pokemonType2.text = "노말"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.normal))
            }
            "fire" -> {
                binding.pokemonType2.text = "불꽃"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.fire))
            }
            "water" -> {
                binding.pokemonType2.text = "물"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.water))
            }
            "electric" -> {
                binding.pokemonType2.text = "전기"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.electric))
            }
            "ice" -> {
                binding.pokemonType2.text = "얼음"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ice))
            }
            "fighting" -> {
                binding.pokemonType2.text = "격투"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.fighting))
            }
            "ground" -> {
                binding.pokemonType2.text = "땅"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ground))
            }
            "flying" -> {
                binding.pokemonType2.text = "비행"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.flying))
            }
            "fairy" -> {
                binding.pokemonType2.text = "페어리"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.fairy))
            }
            "psychic" -> {
                binding.pokemonType2.text = "에스퍼"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.psychic))
            }
            "bug" -> {
                binding.pokemonType2.text = "벌레"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.bug))
            }
            "rock" -> {
                binding.pokemonType2.text = "바위"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.rock))
            }
            "ghost" -> {
                binding.pokemonType2.text = "고스트"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.ghost))
            }
            "dragon" -> {
                binding.pokemonType2.text = "드래곤"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.dragon))
            }
            "dark" -> {
                binding.pokemonType2.text = "악"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.dark))
            }
            "steel" -> {
                binding.pokemonType2.text = "강철"
                binding.type2CardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.steel))
            }
        }
    }
}
