package com.example.polyschedule.presentation.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.entity.Group

class GroupSectionAdapter: RecyclerView.Adapter<GroupSectionAdapter.GroupSection>() {

    var sections = mapOf<String, List<Group>>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var onGroupClicked: ((Group) -> Unit)? = null
    var onTitleClicked: ((Group) -> Unit)? = null
    private var lastSelected: Pair<Int, GroupAdapter?> = Pair(GroupAdapter.FIRST_CLICKED, null)



    class GroupSection(val view: View) : RecyclerView.ViewHolder(view){
        val rv = view.findViewById<RecyclerView>(R.id.group_rv)
        val arrow_up = view.findViewById<ImageView>(R.id.arrow_up)
        val arrow_down = view.findViewById<ImageView>(R.id.arrow_down)
        val title = view.findViewById<TextView>(R.id.name_group)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupSection {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group_section, parent, false)
        return GroupSection(view)
    }

    override fun getItemCount(): Int {
        return sections.size
    }

    override fun onBindViewHolder(holder: GroupSection, position: Int) {
        val item = sections.entries.toList()[position]
        holder.title.text = item.key
        holder.rv.apply {
            layoutManager = GridLayoutManager(holder.rv.context, 2)
            isNestedScrollingEnabled = false
            adapter = GroupAdapter(item.value.toMutableList()).apply {
                onGroupItemClicked = { nowPosition: Int, groupAdapter: GroupAdapter ->
                    this@GroupSectionAdapter.onGroupClicked?.invoke(groupAdapter.groupList[nowPosition])
                    if (lastSelected.first != GroupAdapter.FIRST_CLICKED) {
                        lastSelected.second?.extrudeLastView(lastSelected.first)
                    }
                    lastSelected = Pair(nowPosition, groupAdapter)
                }

            }

        }
        holder.view.setOnClickListener {
            when (holder.rv.visibility) {
                View.VISIBLE -> {
                    holder.rv.visibility = View.GONE
                    holder.arrow_down.visibility = View.GONE
                    holder.arrow_up.visibility = View.VISIBLE
                }
                View.GONE -> {
                    holder.rv.visibility = View.VISIBLE
                    holder.arrow_up.visibility = View.GONE
                    holder.arrow_down.visibility = View.VISIBLE
                }
                View.INVISIBLE -> {
                    throw java.lang.IllegalStateException("")
                }
            }

        }
    }


    companion object {
        const val MAIN_VIEWTYPE = 0
    }
}