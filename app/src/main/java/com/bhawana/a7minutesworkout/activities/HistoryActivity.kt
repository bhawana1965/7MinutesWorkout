package com.bhawana.a7minutesworkout.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhawana.a7minutesworkout.adapters.HistoryAdapter
import com.bhawana.a7minutesworkout.R
import com.bhawana.a7minutesworkout.database.SqliteOpenHelper
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar_history_activity)

        val actionbar = supportActionBar

        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = "HISTORY"
        }

        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        getAllCompletedDates()
    }

    private fun getAllCompletedDates(){
        val dbHandler = SqliteOpenHelper(this,null)
        val allCompletedDates = dbHandler.getAllCompletedDateList()

       if(allCompletedDates.size > 0){
           tvHistory.visibility = View.VISIBLE
           rvHistory.visibility = View.VISIBLE

           tvNoData.visibility = View.GONE

           rvHistory.layoutManager = LinearLayoutManager(this)
           val historyAdapter = HistoryAdapter(this, allCompletedDates)
           rvHistory.adapter = historyAdapter

       }else{
           tvHistory.visibility = View.GONE
           rvHistory.visibility = View.GONE

           tvNoData.visibility = View.VISIBLE
       }

    }
}