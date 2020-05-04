package com.fatihucarci.routedrawingsample.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fatihucarci.routedrawingsample.*
import com.fatihucarci.routedrawingsample.room.RunActivity
import kotlinx.android.synthetic.main.run_item.view.*

/*
* Copyright (C) Fatih UCARCI - All Rights Reserved
* Unauthorized copying of this file, via any medium is strictly prohibited
* Proprietary and confidential
* Written by Fatih UCARCI <fatih.ucarci@gmail.com>, April 2020 
*
* Created by Fatih UCARCI at 4/10/2020
*/


class RunItemAdapter(private val runList: List<RunActivity>) : RecyclerView.Adapter<RunItemAdapter.RunItemViewHolder>(){


    class RunItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvDistance : TextView = itemView.tv_run_distance
        val tvDuration : TextView = itemView.tv_run_duration
        val tvRunDate  : TextView = itemView.tv_run_date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.run_item, parent,false)
        return RunItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RunItemViewHolder, position: Int) {
        val currentRunActivity = runList[position]

        holder.tvDistance.text = "%.2f".format(calculateDistance(currentRunActivity.pathPoints)/1000) + " km"
        holder.tvDuration.text = calculateDuration(currentRunActivity)
        holder.tvRunDate.text = convertLongToSimpleString(currentRunActivity.startTimeMilli!!)

    }

    override fun getItemCount(): Int {
        return runList.size
    }

}