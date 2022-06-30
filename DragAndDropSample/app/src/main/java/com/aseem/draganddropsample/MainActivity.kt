package com.aseem.draganddropsample

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.aseem.draganddropsample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnDragListener, CallbackItemTouch {
    private lateinit var binding: ActivityMainBinding
    private val listItemGrid = mutableListOf(
        SettingsModel("Setting 5", R.mipmap.ic_launcher, 0),
        SettingsModel("Setting 6", R.mipmap.ic_launcher, 1),
        SettingsModel("Setting 7", R.mipmap.ic_launcher, 2),
        SettingsModel("Setting 8", R.mipmap.ic_launcher, 3),
        SettingsModel("Setting 9", R.mipmap.ic_launcher, 4),
        SettingsModel("Setting 10", R.mipmap.ic_launcher, 5),
        SettingsModel("Setting 11", R.mipmap.ic_launcher, 6),
        SettingsModel("Setting 12", R.mipmap.ic_launcher, 7),
        SettingsModel("Setting 13", R.mipmap.ic_launcher, 8),
    )

    private val listItemSelected = mutableListOf(
        SettingsModel("Setting 1", R.mipmap.ic_launcher, 9),
        SettingsModel("Setting 2", R.mipmap.ic_launcher, 10),
        SettingsModel("Setting 3", R.mipmap.ic_launcher, 11),
        SettingsModel("Setting 4", R.mipmap.ic_launcher, 12),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSelectedSettingsRecyclerView()

        val adapter =
            SettingsAdapter(listItemGrid as ArrayList<SettingsModel>) { view, state, childListIndex ->
                listItemSelected[childListIndex] = state

            }

        binding.rvSettings.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.HORIZONTAL, false)
        binding.rvSettings.adapter = adapter

        binding.rvSelectedSetting.setOnDragListener(this)

    }

    private lateinit var selectedSettingAdapter: SelectedSettingHorizontalAdapter
    @SuppressLint("NotifyDataSetChanged")
    private fun initSelectedSettingsRecyclerView() {
        selectedSettingAdapter =
            SelectedSettingHorizontalAdapter(listItemSelected as ArrayList<SettingsModel>) { model, position ->

            }
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSelectedSetting.layoutManager = linearLayoutManager

        binding.rvSelectedSetting.adapter = selectedSettingAdapter

        selectedSettingAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (positionStart == 0 && positionStart == linearLayoutManager.findFirstCompletelyVisibleItemPosition()) {
                    linearLayoutManager.scrollToPosition(0)
                }
            }
        })

        val callback: ItemTouchHelper.Callback =
            MyItemTouchHelperCallback(this) // create MyItemTouchHelperCallback

        val touchHelper =
            ItemTouchHelper(callback) // Create ItemTouchHelper and pass with parameter the MyItemTouchHelperCallback

        touchHelper.attachToRecyclerView(binding.rvSelectedSetting)

        binding.btnClear.setOnClickListener {
            listItemSelected.clear()
            selectedSettingAdapter.notifyDataSetChanged()
        }


    }

    override fun onDrag(v: View?, event: DragEvent): Boolean {
        val state = event.localState as DragData
        when (event.action) {
            DragEvent.ACTION_DRAG_ENTERED -> {
                binding.rvSelectedSetting.setBackgroundResource(R.drawable.green_dashed_box)
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                binding.rvSelectedSetting.setBackgroundResource(R.drawable.dashed_box)
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                binding.rvSelectedSetting.setBackgroundResource(R.drawable.dashed_box)
            }
            DragEvent.ACTION_DROP -> {
                if (listItemSelected.find { it.id == state.item.id } != null) {
                    Toast.makeText(MainActivity@ this, "Setting already exists!", Toast.LENGTH_LONG)
                        .show()
                    return true
                }
                listItemSelected.add(0, state.item)
                selectedSettingAdapter.notifyItemRangeInserted(0, 1)
            }
            else -> {}
        }
        return true
    }

    override fun itemTouchOnMove(oldPosition: Int, newPosition: Int) {
        listItemSelected.add(newPosition, listItemSelected.removeAt(oldPosition));// change position
        selectedSettingAdapter.notifyItemMoved(
            oldPosition,
            newPosition
        ) //notifies changes in adapter, in this case use the notifyItemMoved
    }

}

