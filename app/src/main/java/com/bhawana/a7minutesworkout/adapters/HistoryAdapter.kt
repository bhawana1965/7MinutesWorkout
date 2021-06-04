package com.bhawana.a7minutesworkout.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bhawana.a7minutesworkout.R
import kotlinx.android.synthetic.main.single_history_row.view.*

class HistoryAdapter(val context : Context, val items : ArrayList<String>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    class ViewHolder(view : View): RecyclerView.ViewHolder(view){
        val llHistoryItem = view.llHistoryItem
        val tvPosition = view.tvPosition
        val tvItem = view.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(context).
       inflate(R.layout.single_history_row,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = items[position]

        holder.tvPosition.text = (position + 1).toString()
        holder.tvItem.text = date

        if(position % 2 ==0){
            holder.llHistoryItem.setBackgroundColor(Color.parseColor("#EBEBEB"))
        }else{
            holder.llHistoryItem.setBackgroundColor(
                ContextCompat.getColor(context, R.color.colorWhite)
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}