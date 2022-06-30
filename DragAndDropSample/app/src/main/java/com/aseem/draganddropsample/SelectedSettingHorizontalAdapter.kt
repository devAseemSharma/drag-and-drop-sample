package com.aseem.draganddropsample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aseem.draganddropsample.databinding.RowSelectedSettingsHorizontalBinding

class SelectedSettingHorizontalAdapter(
    private val listSetting: ArrayList<SettingsModel>,
    private val dragListener: (selectedModel: SettingsModel, selectedChildIndex: Int) -> Unit
) :
    RecyclerView.Adapter<SelectedSettingHorizontalAdapter.SelectedSettingHorizontalViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedSettingHorizontalViewHolder {
        val view = RowSelectedSettingsHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SelectedSettingHorizontalViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedSettingHorizontalViewHolder, position: Int) {
        holder.bind(position, listSetting[position])
    }

    override fun getItemCount(): Int {
        return listSetting.size
    }


    class SelectedSettingHorizontalViewHolder(private val binding: RowSelectedSettingsHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, settingsModel: SettingsModel) {
            binding.layoutRowSettingThird.tag = position
            binding.tvSettingName.text = settingsModel.settingName

            if (settingsModel.isEmpty){
                binding.childLayout.visibility = View.INVISIBLE
                binding.add.visibility = View.VISIBLE

            }else{
                binding.childLayout.visibility = View.VISIBLE
                binding.add.visibility = View.GONE
            }
        }
    }
}