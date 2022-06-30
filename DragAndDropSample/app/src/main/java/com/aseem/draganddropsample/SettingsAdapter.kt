package com.aseem.draganddropsample

import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.aseem.draganddropsample.databinding.RowSettingsBinding


class SettingsAdapter(
    private val listItems: ArrayList<SettingsModel>,
    private val listener: (view: RowSettingsBinding, state: SettingsModel, selectedChildIndex: Int) -> Unit
) :
    RecyclerView.Adapter<SettingsAdapter.SettingAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingAdapterViewHolder {
        val view = RowSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SettingAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SettingAdapterViewHolder, position: Int) {
        holder.bind(position, listItems[position])
        holder.view.layoutRowSetting.setOnLongClickListener { v ->
            val item: SettingsModel = listItems.get(holder.adapterPosition)
            val state = DragData(
                item,
                holder.view.layoutRowSetting.width,
                holder.view.layoutRowSetting.height,
                isFromGrid = true
            )
            val shadow = DragShadowBuilder(holder.view.layoutRowSetting)
            ViewCompat.startDragAndDrop(holder.view.layoutRowSetting, null, shadow, state, 0)
            holder.view.innerParent.visibility = View.INVISIBLE
            true
        }

        holder.view.layoutRowSetting.setOnDragListener { view, event ->
            val state = event.localState as DragData
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    if (!state.isFromGrid) {
                        holder.view.layoutRowSetting.setBackgroundResource(R.drawable.bg_white_black_outline_box)
                        holder.view.innerParent.visibility = View.INVISIBLE
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    notifyItemRangeChanged(view.tag as Int, 1)
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    if (!state.isFromGrid) {
                        holder.view.layoutRowSetting.setBackgroundResource(R.drawable.bg_white_black_outline_box)
                        holder.view.innerParent.visibility = View.VISIBLE
                    }
                }

                DragEvent.ACTION_DROP -> {
                    if (!state.isFromGrid) {
                        // Update grid list and selected list by swapping items
                        listener.invoke(
                            holder.view,
                            listItems[view.tag as Int],
                            state.selectedChildDragPosition
                        )
                        listItems[view.tag as Int] = state.item
                        notifyItemRangeChanged(position, 1)

                    }
                }

                else -> {

                }
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return 9
    }


    class SettingAdapterViewHolder(val view: RowSettingsBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(position: Int, settingsModel: SettingsModel) {
            view.layoutRowSetting.tag = position
            view.tvSettingName.text = settingsModel.settingName
            view.innerParent.visibility = View.VISIBLE
            view.layoutRowSetting.setBackgroundResource(R.drawable.bg_white_black_outline_box)
        }
    }
}


data class DragData(
    val item: SettingsModel,
    val width: Int,
    val height: Int,
    val isFromGrid: Boolean,
    val selectedChildDragPosition: Int = -1
)