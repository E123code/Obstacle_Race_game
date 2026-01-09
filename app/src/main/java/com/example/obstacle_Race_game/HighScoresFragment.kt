package com.example.obstacle_Race_game


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.obstacle_Race_game.adapters.ScoreAdapter
import com.example.obstacle_Race_game.interfaces.Callback_HighScore_clicked
import com.example.obstacle_Race_game.model.RecordManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView


class HighScoresFragment : Fragment() {

    companion object {
        private  lateinit var HighScore_Title_LeaderBoard : MaterialTextView
        private lateinit var HighScore_BTN_mainMenu : MaterialButton

        private  lateinit var  HighScore_RV_List : RecyclerView

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       var v : View= inflater.inflate(
        R.layout.fragment_high_scores,
        container,
        false
       )

        findViews(v)
        initViews(v)
        return  v
    }

    private fun findViews(v:View){
        HighScore_Title_LeaderBoard= v.findViewById(R.id.record_Title_leaderBoard)
        HighScore_BTN_mainMenu = v.findViewById(R.id.record_BTN_mainMenu)
        HighScore_RV_List = v.findViewById(R.id.LeaderBoard_RV_List)
    }

    private fun initViews(v: View){

        val records = RecordManager.getRecords()
        val adapter = ScoreAdapter(records, activity as Callback_HighScore_clicked)
        HighScore_RV_List.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        HighScore_RV_List.layoutManager = linearLayoutManager

        HighScore_BTN_mainMenu.setOnClickListener {
            val intent = Intent(requireActivity(), StartActivity::class.java)
            startActivity(intent)

        }


    }

}