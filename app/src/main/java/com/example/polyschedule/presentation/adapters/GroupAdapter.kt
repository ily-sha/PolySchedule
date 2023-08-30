package com.example.polyschedule.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.entity.Group

class GroupAdapter(val groupList: MutableList<Group>): RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tv = itemView.findViewById<TextView>(R.id.schedule_setting_tv)
    }


   var onGroupItemClicked: ((Int, GroupAdapter) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {

        val layout = when(viewType){
            DISABLED_GROUP_ITEM -> R.layout.disabled_group_item
            DISABLED_GROUP_FIRST_ITEM -> R.layout.disabled_group_first_item
            ENABLED_GROUP_FIRST_ITEM -> R.layout.enabled_group_first_item
            ENABLED_GROUP_ITEM -> R.layout.enabled_group_item
            else -> throw java.lang.RuntimeException("Unknown view type $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return GroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.tv.text = groupList[position].name
        holder.tv.setOnClickListener {
            extrudeLastView(position)
            onGroupItemClicked?.invoke(position, this)
        }

    }

    fun extrudeLastView(position: Int) {
        groupList.add(position, groupList[position].copy(selected = !groupList[position].selected))
        groupList.removeAt(position + 1)
        notifyItemChanged(position)
    }


    override fun getItemViewType(position: Int): Int {
        return if (!groupList[position].selected){
            if (position % 2 == 0) DISABLED_GROUP_FIRST_ITEM
            else DISABLED_GROUP_ITEM
        } else
            if (position % 2 == 0) ENABLED_GROUP_FIRST_ITEM
            else ENABLED_GROUP_ITEM
    }
    override fun getItemCount(): Int = groupList.size

    companion object {
        const val ENABLED_GROUP_ITEM = 100
        const val ENABLED_GROUP_FIRST_ITEM = 1000
        const val DISABLED_GROUP_ITEM = 101
        const val DISABLED_GROUP_FIRST_ITEM = 1001
        const val FIRST_CLICKED = -1

    }


}