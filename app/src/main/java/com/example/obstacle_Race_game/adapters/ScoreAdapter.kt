package com.example.obstacle_Race_game.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.obstacle_Race_game.interfaces.Callback_HighScore_clicked

import com.example.obstacle_Race_game.databinding.HighScoreBinding
import com.example.obstacle_Race_game.model.HighScore

class ScoreAdapter(private val scores: List<HighScore>,
                   private val callback: Callback_HighScore_clicked):
    RecyclerView.Adapter<ScoreAdapter.HighScoreViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HighScoreViewHolder {
        val binding = HighScoreBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return HighScoreViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HighScoreViewHolder,
        position: Int
    ) {
        with(holder){
            with(getItem(position)){
                binding.recordLBLName.text = username
                binding.recordLBLScore.text = highScore.toString()
                binding.recordLBLRank.text = "#${position + 1}"
                binding.root.setOnClickListener{
                    callback.highScoreItemClicked(lat,lon)
                }
            }
        }


    }


    fun getItem(position: Int): HighScore = scores[position]


        override fun getItemCount(): Int = scores.size


         class HighScoreViewHolder(val binding: HighScoreBinding) :
            RecyclerView.ViewHolder(binding.root)






}