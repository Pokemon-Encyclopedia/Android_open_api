package com.example.pokemonencyclopedia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pokemonencyclopedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kantoMedal.setOnClickListener {
            startActivity(Intent(this, KantoActivity::class.java))
        }

        binding.saintMedal.setOnClickListener {
            startActivity(Intent(this, SaintActivity::class.java))
        }

        binding.hoennMedal.setOnClickListener {
            startActivity(Intent(this, HoennActivity::class.java))
        }
    }
}