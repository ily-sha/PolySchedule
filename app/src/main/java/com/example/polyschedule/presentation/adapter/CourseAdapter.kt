package com.example.polyschedule.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.polyschedule.R

class CourseAdapter: RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(val view: View): ViewHolder(view) {
        val tv = view.findViewById<TextView>(R.id.tv)

    }

    var courses = mutableListOf(Pair("Курс 1", false), Pair("Курс 2", false), Pair("Курс 3", false),
        Pair("Курс 4", false), Pair("Курс 5", false), Pair("Курс 1", false), Pair("Курс 2", false), Pair("Курс 3", false),
        Pair("Курс 4", false), Pair("Курс 5", false))

    var onItemClicked: ((View) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val layout = when(viewType){
            ENABLED_ITEM -> R.layout.enabled_item
            DISABLED_ITEM -> R.layout.enabled_item
            else -> throw java.lang.RuntimeException("Unknown view type $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.tv.text = courses[position].first
        holder.tv.setOnClickListener {
            println(13123123)
            courses[position] = Pair(courses[position].first, true)
            notifyItemChanged(position)
            onItemClicked?.invoke(holder.view)
            Log.d("CourseAdapter", courses[position].first)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (courses[position].second){
            DISABLED_ITEM
        } else ENABLED_ITEM
    }

    companion object {
        const val ENABLED_ITEM = 100
        const val DISABLED_ITEM = 101
    }

}